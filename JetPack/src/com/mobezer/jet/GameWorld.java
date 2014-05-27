package com.mobezer.jet;

import java.util.ArrayList;
import java.util.Random;

import com.mobezer.jet.objects.BoxUserData;
import com.mobezer.jet.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mobezer.jet.objects.BoxObjectManager;
import com.mobezer.jet.objects.Bob;
import com.mobezer.jet.objects.Cloud;
import com.mobezer.jet.objects.Enemey;

public class GameWorld {
	// World constants
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final int GAME_UNIT = 48;
	public static final int WORLD_WIDTH = GlobalSettings.VIRTUAL_WIDTH;
	public static final int WORLD_HEIGHT = GlobalSettings.VIRTUAL_HEIGHT;
	public static final Vector2 gravity = new Vector2(0, 0);
	public static OrthographicCamera camera; // camera to obtain projection
	// particles
	ParticleEffectPool smokeEffectPool;
	Array<PooledEffect> effects = new Array<PooledEffect>();
	// Physics
	public static BoxObjectManager boxManager;	
	public static BodyEditorLoader shapeLoader = new BodyEditorLoader(Gdx.files.internal("shapes/shape.json"));
	// Others
	public static final Random random=new Random();
	public int state;

	// Lists
	public ArrayList<Enemey> enemies;
	public ArrayList<Cloud> clouds;
	//public static ArrayList<Package> packages;
	// Game Charaters and core objects
	public Bob bob;
	public float leveledSoFar = 0;
	public float heightSoFar;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	Matrix4 debugMatrix;
	// a list of points that define path of the heroBall
	float stateTime,scoreTime=0;
	private TextureWrapper backTexture;
	public GameWorld(OrthographicCamera cam) {
		GameWorld.camera = cam;
		boxManager = new BoxObjectManager();
		bob = new Bob(180, 80);
		boxManager.AddObject(bob);
		debugMatrix = new Matrix4(cam.combined.cpy());
		debugMatrix.scale(BoxObjectManager.BOX_TO_WORLD,
				BoxObjectManager.BOX_TO_WORLD, 1f);
		createCollisionListener();
		enemies = new ArrayList<Enemey>();
		clouds = new ArrayList<Cloud>();
		leveledSoFar = 400;
		backTexture = new TextureWrapper(Assets.backgroundRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		backTexture.SetDimension(cam.viewportWidth, cam.viewportHeight);
		ParticleEffect smokeEffect = new ParticleEffect();
		smokeEffect.load(Gdx.files.internal("particles/smoke.p"), Assets.getAtlas("game"));
		smokeEffectPool = new ParticleEffectPool(smokeEffect, 1, 1);
		PooledEffect effect = smokeEffectPool.obtain();
		effect.setPosition(bob.position.x-50, bob.position.y-50);
		effects.add(effect);
		this.state = WORLD_STATE_RUNNING;
	}

	public void update(float delta) {
		if (state == WORLD_STATE_RUNNING) {
			stateTime+=delta;
			updateLevel(delta);
			updateBob(delta);
			updateEnemy(delta);
			updateClouds(delta);
			boxManager.Update(delta);
			heightSoFar = Math.max(bob.position.y, heightSoFar);
			if (bob.state != Bob.BOB_STATE_HIT)
				checkCollisions();
			checkGameOver();
		}
	}


	private void updateLevel(float delta){
		if(bob.position.y+1200<leveledSoFar)
			return;
		float y = leveledSoFar+20;
		float diff=220;
		while (y < leveledSoFar + WORLD_WIDTH * 2) {
			float x = random.nextFloat()* (WORLD_WIDTH - Enemey.ENEMEY_WIDTH)
					+ Enemey.ENEMEY_WIDTH / 2;

			Cloud cloud = new Cloud(x, y);
			clouds.add(cloud);
			boxManager.AddObject(cloud);
			/*oneItem = false;
			platforms.add(platform);
			createMashrooms(platform);
			createSheilds(platform);
			createWings(platform);
			createDiamonds(platform);
			createEnemies(platform);*/

			y += (diff / 1.4f);
			y += random.nextFloat() * .4;
		}
		leveledSoFar = y;
	}
	private void updateBob(float delta) {
		camera.position.y = bob.position.y + 160f;
		camera.update();
		if(scoreTime>4){
			Bob.SCORE += 1;
			scoreTime = 0;
		}
		scoreTime++;
		//bob.Update(delta);
	}

	private void updateEnemy(float delta) {
		int size = enemies.size();
		for(int i = 0;i<size;i++){
			Enemey item = enemies.get(i);
			if(item.position.y<camera.position.y-500){
				enemies.remove(i);
				size = enemies.size();
				continue;
			}
			item.Update(delta);
		}
	}

	private void updateClouds(float delta) {
		int size = clouds.size();
		for(int i = 0;i<size;i++){
			Cloud item = clouds.get(i);
			if(item.position.y<camera.position.y-500){
				boxManager.removeObject(item);
				clouds.remove(i);
				size = clouds.size();
				continue;
			}
		}
	}

	public static OrthographicCamera getCamera() {
		return camera;
	}

	public void render(SpriteBatch batch) {
		backTexture.setPosition(camera.position.x,camera.position.y);
		batch.disableBlending();
		backTexture.Draw(batch);
		batch.enableBlending();
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
			effect.setPosition(bob.position.x, bob.position.y);
		    effect.draw(batch, Gdx.graphics.getDeltaTime()/2);
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		    }
		}
		boxManager.Draw(batch);
		//bob.Draw(batch);
		//renderEnemy(batch);
		//renderClouds(batch);
		//drawDebug(batch);
	}

	
	public void drawDebug(SpriteBatch batch) {
		batch.disableBlending();
		debugMatrix = new Matrix4(camera.combined.cpy());
		debugMatrix.scale(BoxObjectManager.BOX_TO_WORLD,
				BoxObjectManager.BOX_TO_WORLD, 1f);
		debugRenderer.render(BoxObjectManager.GetWorld(), debugMatrix);
		batch.enableBlending();
	}

	@SuppressWarnings("unused")
	private void renderClouds(SpriteBatch batch) {

	}

	@SuppressWarnings("unused")
	private void renderEnemy(SpriteBatch batch) {
		int size = enemies.size();
		if(size>0)
			for(int i = 0;i<size;i++){
				enemies.get(i).Draw(batch);
			}
	}

	public void dispose() {
		boxManager.Dispose();
		for (int i = effects.size - 1; i >= 0; i--)
		    effects.get(i).free();
		effects.clear();
	}

	public void tap() {
		//bird.fly();
	}

	public void bobMove(float delta, float accel) {	
		if (bob.state != Bob.BOB_STATE_HIT){
			ApplicationType appType = Gdx.app.getType();
			// should work also with
			// Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
			if (appType == ApplicationType.Android
					|| appType == ApplicationType.iOS) {
				bob.velocity.x = -accel / 2 * Bob.BOB_MOVE_VELOCITY;
			} else {
				bob.velocity.x = -accel / 10 * Bob.BOB_MOVE_VELOCITY;
			}
		}
			

	}
	private void createCollisionListener() {
		BoxObjectManager.GetWorld().setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				contact.setEnabled(false);
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				BoxUserData itemA =  ((BoxUserData) fixtureA.getBody().getUserData());
				BoxUserData itemB = ((BoxUserData) fixtureB.getBody().getUserData());
				Gdx.app.log("Contact", "between " + itemA.name + " and " + itemB.name);
				// bob catches package
				if((itemA.name=="bob"&&itemB.name=="cloud_storm")){
					((Bob) itemA.obj).hitStorm();
				}
				if(itemA.name=="cloud_storm"&&itemB.name=="bob"){	
					((Bob) itemB.obj).hitStorm();
				}
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}

		});
	}
	private void checkCollisions() {
		//checkEnemeyCollisions();
		
	}

	/*private void checkEnemeyCollisions() {
		int size = enemies.size();
		if(size>0)
			for(int i = 0;i<size;i++){
				Enemey enemey = enemies.get(i);
				if (OverlapTester.overlapRectangles(bob.bounds, enemey.bounds)) {
					bob.hitStorm();
					WorldListner.hit();
				}
			}
	}*/

	private void checkGameOver() {
		// TODO Auto-generated method stub
		
	}



}
