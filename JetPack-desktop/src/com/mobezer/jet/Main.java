package com.mobezer.jet;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mobezer.jet.Game;

public class Main {
	private static int hd=0;
	private static int size = 4; // 0-320x480  1-360x640 2-720x1280 3-480x800 4-640x960
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "JetPack";
		cfg.useGL30 = true;
		cfg.samples=4;
		cfg.audioDeviceSimultaneousSources=4;
		switch (size) {
		case 0:
			cfg.width = 320;
			cfg.height = 480;
			break;
		case 1:
			cfg.width = 360;
			cfg.height = 640;
			break;
		case 2:
			cfg.width = 720;
			cfg.height = 1280;
			break;
		case 3:
			cfg.width = 480;
			cfg.height = 800;
		case 4:
			cfg.width = 640;
			cfg.height = 960;
			break;
		default:
			cfg.width = 200;
			cfg.height = 600;
			break;
		}
		/*if (hd == 1) {
			cfg.width = 640;
			cfg.height = 960;
		} else {
			cfg.width = 320;
			cfg.height = 480;
		}*/
		new LwjglApplication(new Game(), cfg);
		
	}
	public static int getHd() {
		return hd;
	}
	public static void setHd(int hd) {
		Main.hd = hd;
	}
}
