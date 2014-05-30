package com.mobezer.jet.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mobezer.jet.Assets;
import com.mobezer.jet.CameraHelper;
import com.mobezer.jet.Game;
import com.mobezer.jet.GameWorld;
import com.mobezer.jet.GlobalSettings;
import com.mobezer.jet.TextureWrapper;
import com.mobezer.jet.WorldListner;
import com.mobezer.jet.objects.Bob;
import com.mobezer.jet.ui.Label;
import com.mobezer.jet.ui.WidgetPool;
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
	private WidgetPool widgetPool = new WidgetPool();
	// ////////////////////////////////////////
	int lastScore;
	String scoreString="";
	private OrthographicCamera cam,guiCam;
	GameWorld world;
	TextureWrapper backTexture,whiteMask;
	FPSLogger fps=new FPSLogger();
	// Widgets
	Label fpsLabel,scoreLabel;
	public GameScreen(int screenId, OrthographicCamera cam) {
		// Assets.loadGame();
		this.cam = cam;
		guiCam = CameraHelper.GetCamera(GlobalSettings.VIRTUAL_WIDTH , GlobalSettings.VIRTUAL_HEIGHT);
		this.BackScreenID = screenId;
		batch.setProjectionMatrix(cam.combined);
		IsDone = false;
		state = GAME_INITIALIZE;
		TouchPoint = new Vector3(0, 0, 0);
		TouchHandler = new com.mobezer.jet.TouchHandler();
		// Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		world = new GameWorld(cam);
		//cam.position.set(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT / 2, 0);
		cam.position.set(CameraHelper.camX, CameraHelper.camY, 0);
		
		Init();
		Gdx.app.log("camera", "pos:"+cam.position+" width:"+cam.viewportWidth+"height:"+cam.viewportHeight);
	}

	private void Init() {
		fadeIn();
		widgetPool.setGuiCam(guiCam);
		state = GAME_READY;
		cam.update();
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(this);
		mux.addProcessor(new GestureDetector(0, 0, 0, 0.5f, this));
		Gdx.input.setInputProcessor(mux);
		fpsLabel = new Label("fps", Assets.Shemlock, new Vector2(50,470));
		scoreLabel = new Label("score", Assets.Shemlock, new Vector2(260,470));
		widgetPool.add(fpsLabel);
		widgetPool.add(scoreLabel);
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
		if (Gdx.input.justTouched()) {
			exit();
		}
	}

	private void updatePaused() {
		if (Gdx.input.justTouched()) {
			state = GAME_RUNNING;
		}
	}

	private void updateRunning(float delta) {
		
		ApplicationType appType = Gdx.app.getType();
		// should work also with
		// Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
		if (appType == ApplicationType.Android
				|| appType == ApplicationType.iOS) {
			world.bobMove(delta, Gdx.input.getAccelerometerX() * 0.25f);
		} else {
			float accel = 0;
			if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT))
				accel = 6f;
			if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT))
				accel = -6f;
			world.bobMove(delta, accel);
		}
		world.update(delta);
		if (Bob.SCORE != lastScore) {
			lastScore = Bob.SCORE;
			scoreString = "Score " + lastScore;
		}
		if (world.state == GameWorld.WORLD_STATE_GAME_OVER) 
			state = GAME_OVER;
		
	}

	private void updateReady() {
		if (Gdx.input.justTouched()) {
			state = GAME_RUNNING;
		}
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
		//cam.position.set(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT / 2, 0);
		cam.position.set(CameraHelper.camX, CameraHelper.camY, 0);
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

	@Override
	public void render() {
		cam.update();
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		world.render(batch);
		draw(batch);
		if (whiteMask != null)
			whiteMask.Draw(batch);
		batch.end();
	}

	private void draw(SpriteBatch batch) {
		scoreLabel.setText(scoreString);
		fpsLabel.setText("fps "+Gdx.graphics.getFramesPerSecond());
		widgetPool.draw(batch);
		batch.setProjectionMatrix(cam.combined);
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
			//exit();
			state = GAME_PAUSED;
			return true;
			
		}
		if (keycode == Keys.R || keycode == Keys.MENU) {
			restart();
			return true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.RIGHT) {
			//world.bobStop();
			return true;
		}
		if (keycode == Keys.LEFT ) {
			//world.bobStop();
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
		widgetPool.listenTouch(x, y);
		Gdx.app.log("Touch", "x:" + x+" y:"+y);
		UnProjectCamera(x, y, cam, Game.CAMSTARTX, Game.CAMSTARTY,
				Game.CAMWIDTH, Game.CAMHEIGHT);
		x = (int) TouchPoint.x;
		y = (int) TouchPoint.y;
		/*if(x<Game.CAMWIDTH/2){ // touch left half,move bob left
			world.bobLeft();
		}
		else{	// right half
			world.bobRight();
		}*/
		
		return false;

	};

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		
		return false;
	};

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		widgetPool.listenTap(x, y);
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
