package com.mobezer.jet;

public class WorldListner {

	public static void start(){
		//Assets.playSound(Assets.start);
	}
	public static void restart(){
		Assets.playSound(Assets.restart);
	}
	public static void bonus(){
		Assets.playSound(Assets.bonus);
	}
	public static void victory(){
		Assets.playSound(Assets.victory);
	}
	public static void click(){
		Assets.playSound(Assets.clickSound);
	}
	public static void startMusic(){
		if(GlobalSettings.isSoundEnabled())
			{
				if (!Assets.gameMusic.isPlaying())
					Assets.gameMusic.play();
			}
		else
			if (Assets.gameMusic.isPlaying())
				Assets.gameMusic.pause();
	}
	public static void bounce() {
		Assets.playSound(Assets.bounce1);
	}
}
