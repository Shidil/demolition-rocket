package com.mobezer.jet;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mobezer.jet.Game;

public class Main {
	private static int hd=0;

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "JetPack";
		cfg.useGL30 = true;
		cfg.samples=4;
		cfg.audioDeviceSimultaneousSources=4;
		if (hd == 1) {
			cfg.width = 720;
			cfg.height = 1280;
		} else {
			cfg.width = 360;
			cfg.height = 640;
		}
		new LwjglApplication(new Game(), cfg);
		
	}
}
