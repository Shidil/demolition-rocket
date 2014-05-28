package com.mobezer.jet;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mobezer.jet.objects.Bob;
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
									
	// Others
	public static final Random random=new Random();
	public int state;

	// Lists
	public ArrayList<Enemey> enemies;
	//public static ArrayList<Package> packages;
	// Game Charaters and core objects
	public Bob bob;
	public float leveledSoFar = 0;
	public float heightSoFar;

	// a list of points that define path of the heroBall
	float stateTime,scoreTime=0;
	private TextureWrapper backTexture;
	public GameWorld(OrthographicCamera cam) {
		GameWorld.camera = cam;
		bob = new Bob(180, 80);
		enemies = new ArrayList<Enemey>();
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
		int right = 0,left=0;
		float diff=200;
		float x;
		while (y < leveledSoFar + WORLD_WIDTH * 2) {
			float off=20;
			if(random.nextBoolean()==true){
				off=-20+WORLD_WIDTH/2;
			}
			x = off+random.nextFloat()* (WORLD_WIDTH/2 - Enemey.ENEMEY_WIDTH)
					+ Enemey.ENEMEY_WIDTH / 2;
			if(x>WORLD_WIDTH/2){ 
				left = 0;
				right++;
			}
			else{
				right = 0;
				left++;
			}
			if(right>=2){
				right = 0;
				off = 20;
				x = off+random.nextFloat()* (WORLD_WIDTH/2 - Enemey.ENEMEY_WIDTH)
						+ Enemey.ENEMEY_WIDTH / 2;
			}
			if(left>=2){
				left = 0;
				off=-20+WORLD_WIDTH/2;
				x = off+random.nextFloat()* (WORLD_WIDTH/2 - Enemey.ENEMEY_WIDTH)
						+ Enemey.ENEMEY_WIDTH / 2;
			}
			Enemey ene = new Enemey(x, y);
			enemies.add(ene);
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
		bob.Update(delta);
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
		// TODO Auto-generated method stub
		
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
		bob.Draw(batch);
		renderEnemy(batch);
		renderClouds(batch);
		//drawDebug(batch);
	}

	
	@SuppressWarnings("unused")
	private void drawDebug(SpriteBatch batch) {
		batch.end();
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer .setProjectionMatrix(GameWorld.camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.polygon(bob.polyBounds.getTransformedVertices());
		int size = enemies.size();
		if(size>0)
			for(int i = 0;i<size;i++){
				Enemey enemey = enemies.get(i);
				shapeRenderer.polygon(enemey.polyBounds.getTransformedVertices());
			}
		shapeRenderer.end();
		batch.begin();
	}

	private void renderClouds(SpriteBatch batch) {

	}

	private void renderEnemy(SpriteBatch batch) {
		int size = enemies.size();
		if(size>0)
			for(int i = 0;i<size;i++){
				enemies.get(i).Draw(batch);
			}
	}

	public void dispose() {
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
	private void checkCollisions() {
		checkEnemeyCollisions();
		
	}

	private void checkEnemeyCollisions() {
		int size = enemies.size();
		if(size>0)
			for(int i = 0;i<size;i++){
				Enemey enemey = enemies.get(i);
				if (Intersector.overlapConvexPolygons(bob.polyBounds, enemey.polyBounds)) {
					bob.hitStorm();
					WorldListner.hit();
				}
			}
	}

	private void checkGameOver() {
		// TODO Auto-generated method stub
		
	}



}
