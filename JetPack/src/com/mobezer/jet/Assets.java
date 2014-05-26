package com.mobezer.jet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Assets {
	public static AssetManager manager = new AssetManager();
	public static Music menuMusic;
	public static Sound clickSound,restart,bonus,explode,hit;
	public static BitmapFont Shemlock;
	public static TextButtonStyle buttonShemlock = new TextButtonStyle();
	public static TextureRegion playRegion, backgroundRegion,titleRegion,
					soundEnabled,soundDisabled;
	public static TextureRegion test;
	@SuppressWarnings("unused")
	private static ShaderProgram fontShader;
	public static TextureRegion jet,cloud,cloud_storm,enemey;

	public static void Load() {
	
		/*fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"),
				Gdx.files.internal("shaders/font.frag"));
		if (!fontShader.isCompiled()) {
			Gdx.app.error("fontShader",
					"compilation failed:\n" + fontShader.getLog());
		}*/
		if (Gdx.graphics.getWidth()>=1000){
			manager.load("mainmenu/mainmenu_hd.atlas", TextureAtlas.class);
			manager.load("game/game_hd.atlas", TextureAtlas.class);
			
		}
		else{
			//manager.load("mainmenu/mainmenu.atlas", TextureAtlas.class);
			manager.load("game/game.atlas", TextureAtlas.class);
		}
		
		//manager.load("sounds/crystal_palace.ogg", Music.class);
		manager.load("sounds/click.wav", Sound.class);
		manager.load("sounds/ui_click.ogg", Sound.class);
		manager.load("sounds/restart.wav", Sound.class);
		manager.load("sounds/bonus.ogg", Sound.class);
		manager.load("sounds/explode.mp3", Sound.class);
		manager.load("sounds/hit.wav", Sound.class);
		LoadFont();

	}

	private static void LoadFont() {
		// Load Fonts
		Texture fontTex = new Texture(
				Gdx.files.internal("fonts/Shemlock48Outline.png"), true);
		// Texture fontTex=new
		// Texture(Gdx.files.internal("fonts/fff.png"),true);
		fontTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		fontTex.bind();
		TextureRegion fontTexRegion = new TextureRegion(fontTex);
		Shemlock = new BitmapFont(
				// Gdx.files.internal("fonts/fff.fnt"),fontTexRegion,false);
				Gdx.files.internal("fonts/Shemlock48Outline.fnt"),
				fontTexRegion, false);
		//Shemlock.setScale(0.5f);
		//Shemlock.setColor(Color.RED);
		buttonShemlock.font = Shemlock;// TODO Auto-generated method stub

	}


	public static void loadGame() {
		/*ballRegion = Assets.getAtlas("game").findRegion("background");*/

	}

	public static void loadMenu() {
		playRegion = Assets.getAtlas("game").findRegion("play");
		backgroundRegion = Assets.getAtlas("game").findRegion("sky");
		titleRegion = Assets.getAtlas("game").findRegion("logo");
		/*soundDisabled = Assets.getAtlas("mainmenu").findRegion("sound_disabled");
		soundEnabled = Assets.getAtlas("mainmenu").findRegion("sound_enabled");*/
		test = Assets.getAtlas("game").findRegion("white");
		jet = Assets.getAtlas("game").findRegion("jet");
		cloud = Assets.getAtlas("game").findRegion("cloud");
		cloud_storm = Assets.getAtlas("game").findRegion("cloud_storm");
		enemey = Assets.getAtlas("game").findRegion("enemey");
		// Load Music
		//gameMusic = manager.get("sounds/crystal_palace.ogg");
		//gameMusic.setLooping(true);
		//gameMusic.setVolume(0.9f);
		// Load Sounds
		clickSound = manager.get("sounds/ui_click.ogg");
		explode = manager.get("sounds/explode.mp3");
		restart = manager.get("sounds/restart.wav");
		bonus = manager.get("sounds/bonus.ogg");
		hit = manager.get("sounds/hit.wav");
	}

	public static void playSound(Sound sound) {
		if (GlobalSettings.isSoundEnabled())
			sound.play(1);
	}

	public static TextureAtlas getAtlas(String name) {
		if (Gdx.graphics.getWidth()>=1000){
			if (name.equals("game"))
				return manager.get("game/game_hd.atlas", TextureAtlas.class);
			if (name.equals("common"))
				return manager.get("game/common/common.atlas", TextureAtlas.class);
			if (name.equals("mainmenu"))
				return manager.get("mainmenu/mainmenu_hd.atlas", TextureAtlas.class);
		}
		else{
			if (name.equals("game"))
				return manager.get("game/game.atlas", TextureAtlas.class);
			if (name.equals("common"))
				return manager.get("game/common/common.atlas", TextureAtlas.class);
			if (name.equals("mainmenu"))
				return manager.get("mainmenu/mainmenu.atlas", TextureAtlas.class);
		}

		return null;
	}

	public static void Dispose() {
		manager.dispose();
	}
}
