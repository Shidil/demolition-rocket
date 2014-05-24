package com.mobezer.jet.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.mobezer.jet.Assets;
import com.mobezer.jet.Game;
import com.mobezer.jet.GameWorld;
import com.mobezer.jet.GlobalSettings;
import com.mobezer.jet.TextWrapper;
import com.mobezer.jet.TextureWrapper;
import com.mobezer.jet.WorldListner;
import com.mobezer.jet.objects.BoxObjectManager;
import com.mobezer.tween.TextureAccessor;

public class GameScreen extends BaseScreen implements InputProcessor,
		GestureListener {
	// constants
	public static final int GAME_INITIALIZE = 0;
	public static final int GAME_READY = 1;
	public static final int GAME_RUNNING = 2;
	public static final int GAME_PAUSED = 3;
	public static final int GAME_OVER = 4;
	static int state;
	Vector3 touchPoint = new Vector3(0, 0, 0);
	// Variables
	// ////////////////////////////////////////
	private OrthographicCamera cam;
	GameWorld world;
	TextureWrapper backTexture,whiteMask;
	FPSLogger fps=new FPSLogger();
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	Matrix4 debugMatrix;
	public GameScreen(int screenId, OrthographicCamera cam) {
		// Assets.loadGame();
		this.cam = cam;
		this.BackScreenID = screenId;
		batch.setProjectionMatrix(cam.combined);
		IsDone = false;
		state = GAME_INITIALIZE;
		TouchPoint = new Vector3(0, 0, 0);
		TouchHandler = new com.mobezer.jet.TouchHandler();
		// Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		world = new GameWorld(cam);
		debugMatrix = new Matrix4(cam.combined.cpy());
		debugMatrix.scale(BoxObjectManager.BOX_TO_WORLD,
				BoxObjectManager.BOX_TO_WORLD, 1f);
		Init();
	}

	private void Init() {
		/*backTexture = new TextureWrapper(Assets.backgroundRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		backTexture.SetDimension(cam.viewportWidth, cam.viewportHeight);*/
		fadeIn();
		state = GAME_RUNNING;
		cam.update();
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(this);
		mux.addProcessor(new GestureDetector(0, 0, 0, 0.5f, this));
		Gdx.input.setInputProcessor(mux);
	}

	@Override
	public void update(float delta) {
		if (delta > 0.1f)
			delta = 0.1f;

		switch (state) {
		case GAME_INITIALIZE:
			updateInit();
			break;
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(delta);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateInit() {
		// TODO Auto-generated method stub

	}

	private void updateGameOver() {
		// TODO code when game is over

	}

	private void updatePaused() {
		// TODO code when game is paused ( pause menu)

	}

	private void updateRunning(float delta) {
		world.update(delta);
		// TODO check game over
		
	}

	private void fadeIn() {
		whiteMask = new TextureWrapper(Assets.test, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		whiteMask.SetDimension(cam.viewportWidth, cam.viewportHeight);
		TweenCallback callback = new TweenCallback() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onEvent(int type, BaseTween source) {
				switch (type) {
				case COMPLETE:
					whiteMask = null;
					break;
				}
			}
		};
		Timeline.createSequence()
				.push(Tween.to(whiteMask, TextureAccessor.OPACITY, 0.5f)
						.target(0).ease(Quart.INOUT)).setCallback(callback)
				.setCallbackTriggers(TweenCallback.COMPLETE)
				.start(Game.tweenManager);
	}

	private void exit() {
		BackScreenID = Game.MENUSCREEN;
		IsDone = true;
	}

	private void restart() {
		whiteMask = new TextureWrapper(Assets.test, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		whiteMask.SetDimension(cam.viewportWidth, cam.viewportHeight);
		TweenCallback callback = new TweenCallback() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onEvent(int type, BaseTween source) {
				switch (type) {
				case COMPLETE:
					BackScreenID = Game.GAMESCREEN;
					IsDone = true;
					break;
				}
			}
		};
		Timeline.createSequence()
				.setCallback(callback)
				.setCallbackTriggers(TweenCallback.COMPLETE)
				.start(Game.tweenManager);
		WorldListner.restart();
	}

	public static void LevelUp() {
		
	}

	private void updateReady() {
		
	}

	@Override
	public void render() {
		cam.update();
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		//batch.disableBlending();
		//backTexture.Draw(batch);
		batch.enableBlending();
		world.render(batch);
		if (whiteMask != null)
			whiteMask.Draw(batch);
		TextWrapper fp = new TextWrapper("fps "+Gdx.graphics.getFramesPerSecond(), Assets.Shemlock, new Vector2(600,340));
		fp.Draw(batch);
		//batch.disableBlending();
		//debugRenderer.render(BoxObjectManager.GetWorld(), debugMatrix);
		//batch.enableBlending();
		batch.end();

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return IsDone;
	}

	@Override
	public void dispose() {
		world.dispose();
	}

	@Override
	public void OnPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			exit();
			return true;
		}
		if (keycode == Keys.R || keycode == Keys.MENU) {
			restart();
			return true;
		}
		if (keycode == Keys.RIGHT) {
			world.bobRight();
			return true;
		}
		if (keycode == Keys.LEFT ) {
			world.bobLeft();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.RIGHT) {
			world.bobStop();
			return true;
		}
		if (keycode == Keys.LEFT ) {
			world.bobStop();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		UnProjectCamera(x, y, cam, Game.CAMSTARTX, Game.CAMSTARTY,
				Game.CAMWIDTH, Game.CAMHEIGHT);
		x = (int) TouchPoint.x;
		y = (int) TouchPoint.y;
		if(x<Game.CAMWIDTH/2){ // touch left half,move bob left
			world.bobLeft();
		}
		else{	// right half
			world.bobRight();
		}
		Gdx.app.log("", "" + TouchPoint);
		return false;

	};

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		
		return false;
	};

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		world.bobStop();
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// cam.zoom +=(distance-initialDistance)*0.005f;
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// cam.zoom=1f;
		return false;
	}

}
