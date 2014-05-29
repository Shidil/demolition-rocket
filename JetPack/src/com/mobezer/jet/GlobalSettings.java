package com.mobezer.jet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GlobalSettings {
	
	public static final int VIRTUAL_WIDTH=320;
	public static final int VIRTUAL_HEIGHT=480;
	
	public static final float BOX_STEP=1/60f;
	public static final int VELOCITY_ITERATIONS=6;
	public static final int POSITION_ITERATIONS=2;
	protected static final String PREF_SOUND = "soundEnabled";
	protected static final String PREF_TILT = "tiltSensitivity";
	private static Preferences prefs;
	public static void loadPrefs(){
		prefs = Gdx.app.getPreferences("com.mobezer.jet.pref1");
	}
	public static Preferences getPrefs() {
		return prefs;
	}
	public static void setPrefs(Preferences prefs) {
		GlobalSettings.prefs = prefs;
	}
	public static boolean isSoundEnabled() {
		return prefs.getBoolean(PREF_SOUND, true);	
		
	}
	public static float getTiltSensitivity(){
		return prefs.getFloat(PREF_TILT, 1);
	}
	public static void setTiltSensitivity(float val){
		prefs.putFloat(PREF_TILT, val);
		prefs.flush();
	}
	public static void toggleSound() {
		prefs.putBoolean(PREF_SOUND, !isSoundEnabled());
		prefs.flush();
		WorldListner.startMusic();
		
	}
	public static boolean isUnlocked(int levelId){
		return false;
	}
	public static void store(){
		prefs.flush();
	}
}
