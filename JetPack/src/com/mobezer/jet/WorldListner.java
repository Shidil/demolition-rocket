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
	public static void hit(){
		Assets.playSound(Assets.hit);
	}
	public static void click(){
		Assets.playSound(Assets.clickSound);
	}
	public static void startMusic(){
		/*if(GlobalSettings.isSoundEnabled())
			{
				if (!Assets.gameMusic.isPlaying())
					Assets.gameMusic.play();
			}
		else
			if (Assets.gameMusic.isPlaying())
				Assets.gameMusic.pause();*/
	}
	public static void explode() {
		Assets.playSound(Assets.explode);
	}
	public static void coin() {
		Assets.playSound(Assets.money);
	}
}
