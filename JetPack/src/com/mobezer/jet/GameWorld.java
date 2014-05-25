package com.mobezer.jet;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
	public static final Vector2 gravity = new Vector2(0, -100);
	public static OrthographicCamera camera; // camera to obtain projection
									
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
	float stateTime;
	private TextureWrapper backTexture;
	public GameWorld(OrthographicCamera cam) {
		GameWorld.camera = cam;
		bob = new Bob(180, 80);
		enemies = new ArrayList<Enemey>();
		backTexture = new TextureWrapper(Assets.backgroundRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		backTexture.SetDimension(cam.viewportWidth, cam.viewportHeight);
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

			Enemey ene = new Enemey(x, y);
			enemies.add(ene);
			/*oneItem = false;
			platforms.add(platform);
			createMashrooms(platform);
			createSheilds(platform);
			createWings(platform);
			createDiamonds(platform);
			createEnemies(platform);*/

			y += (diff / 1.5f);
			y += random.nextFloat() * .5;
		}
		leveledSoFar = y;
	}
	private void updateBob(float delta) {
		camera.position.y = bob.position.y + 240f;
		camera.update();
		Bob.SCORE += 4;
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
		bob.Draw(batch);
		renderEnemy(batch);
		renderClouds(batch);
	}

	
	private void renderClouds(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	private void renderEnemy(SpriteBatch batch) {
		int size = enemies.size();
		if(size>0)
			for(int i = 0;i<size;i++){
				enemies.get(i).Draw(batch);
			}
	}

	public void dispose() {
		
	}

	public void tap() {
		//bird.fly();
	}

	public void bobMove(float delta, float accel) {	
		if (bob.state != Bob.BOB_STATE_HIT)
			bob.velocity.x = -accel / 10 * Bob.BOB_MOVE_VELOCITY;

	}



}
