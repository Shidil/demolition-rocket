package com.mobezer.jet.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
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

public class SettingsScreen extends BaseScreen implements InputProcessor{
	
	
	TextureWrapper backTexture,titleTexture;
	private WidgetPool widgetPool = new WidgetPool();
	public Button Play;
	private OrthographicCamera cam;
	private ShapeRenderer shapes = new ShapeRenderer();
	private TextureWrapper whiteMask;
	private TextureWrapper boxTexture;
	
	public SettingsScreen(int screenId,OrthographicCamera cam){
		super("SettingsScreen");
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
		boxTexture = new TextureWrapper(Assets.boxRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				500+GlobalSettings.VIRTUAL_HEIGHT / 2));
		boxTexture.SetDimension(TextureDimensions.BOX_WIDTH, TextureDimensions.BOX_HEIGHT);
		final TextureWrapper soundTexture = new TextureWrapper(GlobalSettings.isSoundEnabled()?Assets.soundEnabled:Assets.soundDisabled,null);
		soundTexture.SetDimension(TextureDimensions.SOUND_WIDTH, TextureDimensions.SOUND_HEIGHT);
		Button sound=new Button(soundTexture, soundTexture, null, new Vector2(GlobalSettings.VIRTUAL_WIDTH / 2,GlobalSettings.VIRTUAL_HEIGHT / 2));
		sound.setOnTapListner(new TouchListner() {
			@Override
			public void tap() {
				WorldListner.click();
				GlobalSettings.toggleSound();
				soundTexture.SetTexture(GlobalSettings.isSoundEnabled()?Assets.soundEnabled:Assets.soundDisabled,TextureDimensions.SOUND_WIDTH, TextureDimensions.SOUND_HEIGHT);
			}
		});	
		// Add widgets to pool
		widgetPool.add(sound);	
		
		//create Tweens
		Timeline.createSequence()
		.beginParallel()
		.push(Tween.set(soundTexture, TextureAccessor.SCALE_XY).target(0,0))
		.push(Tween
				.to(boxTexture,
						TextureAccessor.POS_XY, 0.5f)
				.target(GlobalSettings.VIRTUAL_WIDTH / 2,
						GlobalSettings.VIRTUAL_HEIGHT / 2)
				.ease(Quart.OUT))
		.push(Tween
				.to(soundTexture,
						TextureAccessor.SCALE_XY, 0.7f)
				.target(1,1)
				.ease(Quart.OUT))
		.end()
		.setCallbackTriggers(TweenCallback.COMPLETE)
		.start(Game.tweenManager);
		
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
		boxTexture.Draw(batch);
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
					BackScreenID = Game.SETTINGS_SCREEN;
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
			BackScreenID=Game.MENUSCREEN;
			IsDone=true;
			//System.exit(0);
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
