package com.mobezer.jet.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mobezer.jet.Assets;
import com.mobezer.jet.CameraHelper;
import com.mobezer.jet.Game;
import com.mobezer.jet.GlobalSettings;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;
import com.mobezer.jet.WorldListner;
import com.mobezer.jet.ui.Button;
import com.mobezer.jet.ui.TouchListner;
import com.mobezer.jet.ui.WidgetPool;
import com.mobezer.tween.TextureAccessor;

public class MainMenu extends BaseScreen implements InputProcessor{
	
	
	TextureWrapper backTexture,titleTexture;
	private WidgetPool widgetPool = new WidgetPool();
	public Button Play,Settings;
	private OrthographicCamera cam;
	private TextureWrapper playTexture,settingsTexture;
	private ShapeRenderer shapes = new ShapeRenderer();
	private TextureWrapper whiteMask;
	
	public MainMenu(int screenId,OrthographicCamera cam){
		super("MenuScreen");
		//Assets.loadMenu();
		this.BackScreenID=screenId;
		batch.setProjectionMatrix(cam.combined);
		IsDone=false;
		TouchPoint=new Vector3(0,0,0);
		TouchHandler=new com.mobezer.jet.TouchHandler();
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		this.cam=cam;
		cam.position.set(CameraHelper.camX, CameraHelper.camY, 0);
		cam.update();
		Init();
	}
	
	private void Init() {

		widgetPool.setGuiCam(cam);
		backTexture = new TextureWrapper(Assets.backgroundRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		backTexture.SetDimension(GlobalSettings.VIRTUAL_WIDTH,
				GlobalSettings.VIRTUAL_HEIGHT);
		titleTexture = new TextureWrapper(Assets.titleRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2+500));
		titleTexture.SetDimension(TextureDimensions.TITLE_WIDTH, TextureDimensions.TITLE_HEIGHT);
		playTexture=new TextureWrapper(Assets.playRegion,null);
		playTexture.SetDimension(TextureDimensions.PLAY_WIDTH, TextureDimensions.PLAY_HEIGHT);
		settingsTexture =new TextureWrapper(Assets.settingsRegion, null);
		// Create ui widgets
		Play=new Button(playTexture, playTexture, null, new Vector2(GlobalSettings.VIRTUAL_WIDTH / 2, 200));
		Play.setOnTapListner(new TouchListner() {
			@Override
			public void tap() {
				WorldListner.click();
				BackScreenID=Game.GAMESCREEN;
				IsDone = true;
				dispose();
			}
		});	
		Settings=new Button(settingsTexture, settingsTexture, null, new Vector2(GlobalSettings.VIRTUAL_WIDTH / 2, 200));
		Settings.setOnTapListner(new TouchListner() {
			@Override
			public void tap() {
				WorldListner.click();
				BackScreenID=Game.SETTINGS_SCREEN;
				IsDone = true;
				dispose();
			}
		});	
		Timeline.createSequence()
		.beginParallel()
		.push(Tween
				.to(titleTexture,
						TextureAccessor.POS_XY, 0.8f)
				.target(GlobalSettings.VIRTUAL_WIDTH / 2,
						GlobalSettings.VIRTUAL_HEIGHT / 2+100)
				.ease(Circ.OUT))
		.push(Tween
				.to(playTexture,
						TextureAccessor.POS_XY, 1f)
				.target(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT/2-40)
				.ease(Quart.OUT))
		.push(Tween
				.to(settingsTexture,
						TextureAccessor.POS_XY, 1f)
				.target(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT/2-100)
				.ease(Quart.OUT))
		.end()
		.setCallbackTriggers(TweenCallback.COMPLETE)
		.start(Game.tweenManager);
		
		// Add widgets to pool
		widgetPool.add(Settings);
		widgetPool.add(Play);
		// Play Music
		//WorldListner.startMusic();
		fadeIn();
	}


	@Override
	public void update(float dt) {
		widgetPool.update(dt);
		super.update(dt);
	}

	@Override
	public void render() {
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		//2. clear our depth buffer with 1.0
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		
		//3. set the function to LESS
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		
		//4. enable depth writing
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		//5. Enable depth writing, disable RGBA color writing 
		Gdx.gl.glDepthMask(true);
		Gdx.gl.glColorMask(false, false, false, false);
		shapes .setProjectionMatrix(cam.combined);
		shapes .begin(ShapeType.Filled);	 
		shapes.setColor(0f, 1f, 0f, 0.5f);
		shapes.rect(cam.position.x-(GlobalSettings.VIRTUAL_WIDTH/2), cam.position.y-(GlobalSettings.VIRTUAL_HEIGHT/2), GlobalSettings.VIRTUAL_WIDTH, GlobalSettings.VIRTUAL_HEIGHT);	
		shapes.end();
		///////////// Draw sprite(s) to be masked		
		batch.begin();
		//8. Enable RGBA color writing
		//   (SpriteBatch.begin() will disable depth mask)
		Gdx.gl.glColorMask(true, true, true, true);
		
		//9. Make sure testing is enabled.
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		//10. Now depth discards pixels outside our masked shapes
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
		batch.setProjectionMatrix(cam.combined);
		batch.disableBlending();
		backTexture.Draw(batch);
		batch.enableBlending();
		titleTexture.Draw(batch);
		widgetPool.draw(batch);
		if (whiteMask != null)
			whiteMask.Draw(batch);
		batch.end();
		super.render();		
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
					BackScreenID = Game. MENUSCREEN;
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
	@Override
	public boolean isDone() {
		return IsDone;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			BackScreenID=Game.EXIT;
			IsDone=true;
			System.exit(0);
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
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		widgetPool.listenTouch(screenX, screenY);
		//Gdx.app.log(Game.LOG, "touch X:"+TouchPoint.x+" Y:"+TouchPoint.y);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		widgetPool.listenTap(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
