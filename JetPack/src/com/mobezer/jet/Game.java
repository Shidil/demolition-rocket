package com.mobezer.jet;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mobezer.jet.screens.BaseScreen;
import com.mobezer.jet.screens.GameScreen;
import com.mobezer.jet.screens.MainMenu;
import com.mobezer.jet.screens.SettingsScreen;
import com.mobezer.jet.screens.SplashScreen;
import com.mobezer.tween.ActorAccessor;
import com.mobezer.tween.TextureAccessor;

public class Game implements ApplicationListener {

	public static final String LOG = Game.class.getSimpleName();
	// a libgdx helper class that logs the current FPS each second
	@SuppressWarnings("unused")
	private FPSLogger fpsLogger;
	public AssetManager manager = new AssetManager();
	public static TweenManager tweenManager;

	BaseScreen _currentScreen;

	OrthographicCamera _camera;

	TouchHandler _touchHandler;

	boolean _isInitialized = false;

	public static float CAMWIDTH;
	public static float CAMHEIGHT;
	public static float CAMSTARTX;
	public static float CAMSTARTY;

	public static final int EXIT = 0;
	public static final int GAMELOOP = 1;
	public static final int GAMESCREEN = 2;
	public static final int MENUSCREEN = 3;
	public static final int SPLASHSCREEN = 4;
	public static final int SETTINGS_SCREEN = 5;


	@Override
	public void create() {
		// TODO Auto-generated method stub
		Gdx.app.log(Game.LOG, "Creating game");
		//fpsLogger = new FPSLogger();
		setTweenManager(new TweenManager()); Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		Tween.registerAccessor(TextureWrapper.class, new TextureAccessor());
		if (!_isInitialized) {
			_camera = CameraHelper.GetCamera(GlobalSettings.VIRTUAL_WIDTH,
					GlobalSettings.VIRTUAL_HEIGHT);
			SetupZoom();
			_camera.update();
			_currentScreen = new SplashScreen(EXIT, _camera);
			//_isInitialized=true;
		}
	}

	void SetupZoom() {
		int virtualAsp = GlobalSettings.VIRTUAL_WIDTH
				/ GlobalSettings.VIRTUAL_HEIGHT;
		int realAsp = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		if (realAsp == virtualAsp) {
			CAMWIDTH = Gdx.graphics.getWidth();
			CAMHEIGHT = Gdx.graphics.getHeight();
			CAMSTARTX = 0;
			CAMSTARTY = 0;
		} else if (realAsp < virtualAsp) {
			CAMWIDTH = Gdx.graphics.getWidth();
			CAMHEIGHT = GlobalSettings.VIRTUAL_HEIGHT
					* (Gdx.graphics.getWidth() / GlobalSettings.VIRTUAL_WIDTH);
			CAMSTARTX = 0;
			CAMSTARTY = (Gdx.graphics.getHeight() - CAMHEIGHT) / 2f;

		} else if (realAsp > virtualAsp) {
			CAMWIDTH = GlobalSettings.VIRTUAL_WIDTH
					* (Gdx.graphics.getHeight() / GlobalSettings.VIRTUAL_HEIGHT);
			CAMHEIGHT = Gdx.graphics.getHeight();
			CAMSTARTY = 0;
			CAMSTARTX = (Gdx.graphics.getWidth() - CAMWIDTH) / 2f;
		}
	}

	@Override
	public void dispose() {
		GlobalSettings.store();
		Assets.Dispose();
		Gdx.app.log(Game.LOG, "Disposing game");
	}

	@Override
	public void pause() {
		Gdx.app.log(Game.LOG, "Pausing game");
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		Update(dt);
		//fpsLogger.log();
	}

	public void Update(float dt) {
		tweenManager.update(dt);
		UpdateTouch();
		UpdateScreen(dt);
	}

	public void UpdateTouch() {
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		if (_isInitialized && !_currentScreen.IsDone) {
			_currentScreen.UnProjectCamera(x, y, _camera, CAMSTARTX, CAMSTARTY,
					CAMWIDTH, CAMHEIGHT);
		
			_currentScreen.TouchHandler.UpdateTouch(
					(int) _currentScreen.TouchPoint.x,
					(int) _currentScreen.TouchPoint.y, Gdx.input.isTouched());
		}

	}

	public void UpdateScreen(float dt) {
		_currentScreen.update(dt);

		_currentScreen.render();

		if (_currentScreen.isDone()) {
			int previousScreenID = _currentScreen.GetScreenID();

			_currentScreen.dispose();

			if (previousScreenID == EXIT) {
				ExitApplication();
				return;
			}
			if (previousScreenID == MENUSCREEN) {
				_currentScreen = new MainMenu(EXIT, _camera);_isInitialized=true;
				return;
			}
			if (previousScreenID == GAMESCREEN) {
				_currentScreen = new GameScreen(MENUSCREEN, _camera);
				return;
			}
			if (previousScreenID == SETTINGS_SCREEN) {
				_currentScreen = new SettingsScreen(MENUSCREEN, _camera);
				return;
			}
		}

	}

	public void ExitApplication() {
		dispose();
		Gdx.app.exit();
	}

	@Override
	public void resize(int width, int height) {
		_camera = CameraHelper.GetCamera(GlobalSettings.VIRTUAL_WIDTH,
				GlobalSettings.VIRTUAL_HEIGHT);
		SetupZoom();
		_camera.update();
		Gdx.app.log(Game.LOG, "Resizing game to: " + width + " x " + height);
		Gdx.app.log("Camera", "startx: " + CAMSTARTX + " starty " + CAMSTARTY+"width: " + CAMWIDTH + " height " + CAMHEIGHT);

	}

	@Override
	public void resume() {
		Gdx.app.log(Game.LOG, "Resuming game");

	}
	public static TweenManager getTweenManager() {
		return tweenManager;
	}

	public static void setTweenManager(TweenManager tweenManager) {
		Game.tweenManager = tweenManager;
	}
}
