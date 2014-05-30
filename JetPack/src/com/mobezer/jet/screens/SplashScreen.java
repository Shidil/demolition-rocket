package com.mobezer.jet.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.Assets;
import com.mobezer.jet.Game;
import com.mobezer.jet.GlobalSettings;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;
import com.mobezer.tween.TextureAccessor;

public class SplashScreen extends BaseScreen {

	TextureWrapper splashImage;
	private boolean animationFinished=false;
	private TextureAtlas atlas;

	public SplashScreen(int screenId, OrthographicCamera cam) {
		this.BackScreenID = screenId;
		batch.setProjectionMatrix(cam.combined);
		name = "SplashScreen";
		IsDone = false;
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		Assets.Load();
		GlobalSettings.loadPrefs();
		atlas = new TextureAtlas(Gdx.files.internal("splash/splash.atlas"));
		AtlasRegion splashRegion = atlas.findRegion("logo");
		splashImage = new TextureWrapper(splashRegion, new Vector2(
				GlobalSettings.VIRTUAL_WIDTH / 2,
				GlobalSettings.VIRTUAL_HEIGHT / 2));
		splashImage.SetDimension(TextureDimensions.SPLASH_WIDTH,
				TextureDimensions.SPLASH_HEIGHT);
		TweenCallback callback = new TweenCallback() {
			@SuppressWarnings("rawtypes")
			@Override public void onEvent(int type, BaseTween source) {
				switch (type) {
					case COMPLETE:animationFinished=true; break;
				}
			}
		};
		Timeline.createSequence()
		.push(Tween.set(splashImage, TextureAccessor.OPACITY).target(0))
		.push(Tween.set(splashImage, TextureAccessor.SCALE_XY).target(6f, 6f))
		//.pushPause(1f)
		.beginParallel()
			.push(Tween.to(splashImage, TextureAccessor.SCALE_XY, 0.8f).target(1,1).ease(Quart.OUT))
			.push(Tween.to(splashImage, TextureAccessor.OPACITY, 0.7f).target(1).ease(Quart.INOUT))
		.end()
		//.pushPause(1.3f)
		.beginParallel()
			.push(Tween.to(splashImage, TextureAccessor.SCALE_XY, 1.0f).target(10,10).ease(Quart.IN))
			.push(Tween.to(splashImage, TextureAccessor.OPACITY, 0.5f).target(0).ease(Quart.INOUT))
		.end()
		//.repeat(-1, 0.5f)
		.setCallback(callback)
		.setCallbackTriggers(TweenCallback.COMPLETE)
		.start(Game.tweenManager);

	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.disableBlending();
		splashImage.Draw(batch);
		batch.end();
		if (Assets.manager.update()) {
			// we are done loading, let's move to another screen!
			if(animationFinished){
				Assets.loadGame();
				Assets.loadMenu();
				Gdx.app.log(Game.LOG, "Animation Finished");
				BackScreenID=Game.MENUSCREEN;
				IsDone = true;
				dispose();
			}
		}
		// display loading information
		float progress = Assets.manager.getProgress();
		Gdx.app.log(Game.LOG, "loading : " + progress);
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return IsDone;
	}

	@Override
	public void dispose() {
		atlas.dispose();
		Game.tweenManager.killAll();
		super.dispose();
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
	public void OnKeyUp(int keyCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnKeyDown(int keyCode) {
		// TODO Auto-generated method stub

	}

}
