package com.mobezer.jet.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mobezer.jet.Assets;
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
	public Button Play;
	private OrthographicCamera cam;
	private TextureWrapper playTexture;
	
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
		Gdx.app.log("camera", ""+cam.position);
		cam.position.set(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT / 2, 0);
		Gdx.app.log("camera", ""+cam.position);
		cam.update();
		Init();
	}
	
	private void Init() {
		widgetPool.setGuiCam(cam);
		backTexture = new TextureWrapper(Assets.backgroundRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		backTexture.SetDimension(cam.viewportWidth,
				cam.viewportHeight);
		titleTexture = new TextureWrapper(Assets.titleRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2+500));
		titleTexture.SetDimension(TextureDimensions.TITLE_WIDTH, TextureDimensions.TITLE_HEIGHT);
		playTexture=new TextureWrapper(Assets.playRegion,new Vector2(GlobalSettings.VIRTUAL_WIDTH / 2, -50));
		playTexture.SetDimension(TextureDimensions.PLAY_WIDTH, TextureDimensions.PLAY_HEIGHT);
		TextureWrapper playTexture1 = new TextureWrapper(Assets.playRegion,new Vector2(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT/2));
		playTexture1.SetDimension(TextureDimensions.PLAY_WIDTH, TextureDimensions.PLAY_HEIGHT);
		playTexture1.SetColor(new Color(1,0,0,1f));
		Timeline.createSequence()
		.beginParallel()
		.push(Tween
				.to(titleTexture,
						TextureAccessor.POS_XY, 0.8f)
				.target(GlobalSettings.VIRTUAL_WIDTH / 2,
						GlobalSettings.VIRTUAL_HEIGHT / 2+100)
				.ease(Bounce.OUT))
		.push(Tween
				.to(playTexture,
						TextureAccessor.POS_XY, 1f)
				.target(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT/2)
				.ease(Quart.OUT))
		.end()
		.setCallbackTriggers(TweenCallback.COMPLETE)
		.start(Game.tweenManager);
		Play=new Button(playTexture, playTexture1, null, new Vector2(GlobalSettings.VIRTUAL_WIDTH / 2, GlobalSettings.VIRTUAL_HEIGHT/2));
		Play.setOnTapListner(new TouchListner() {
			@Override
			public void tap() {
				WorldListner.click();
				BackScreenID=Game.GAMESCREEN;
				IsDone = true;
				dispose();
			}
		});	
		/*final TextureWrapper soundTexture = new TextureWrapper(GlobalSettings.isSoundEnabled()?Assets.soundEnabled:Assets.soundDisabled,new Vector2(320, 120));
		soundTexture.SetDimension(60, 40);
		Button sound=new Button(soundTexture, soundTexture, null, new Vector2(360, 120));
		sound.setOnTapListner(new TouchListner() {
			@Override
			public void tap() {
				WorldListner.click();
				GlobalSettings.toggleSound();
				soundTexture.SetTexture(GlobalSettings.isSoundEnabled()?Assets.soundEnabled:Assets.soundDisabled,60,40);
			}
		});
		// Add widgets to pool
		widgetPool.add(sound);	*/
		widgetPool.add(Play);
		
		// Play Music
		//WorldListner.startMusic();
	}


	@Override
	public void update(float dt) {
		widgetPool.update(dt);
		super.update(dt);
	}

	@Override
	public void render() {
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.setProjectionMatrix(cam.combined);
		batch.disableBlending();
		backTexture.Draw(batch);
		batch.enableBlending();
		titleTexture.Draw(batch);
		widgetPool.draw(batch);
		batch.end();
		super.render();		
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
			//System.exit(0);
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
