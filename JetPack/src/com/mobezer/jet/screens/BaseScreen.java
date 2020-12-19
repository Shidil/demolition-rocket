

package com.mobezer.jet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mobezer.jet.Game;



public  class BaseScreen {

	protected int BackScreenID;
	String name;

	public boolean IsDone;
	
	public Vector3 TouchPoint;
	public com.mobezer.jet.TouchHandler TouchHandler;
	protected final SpriteBatch batch;
	public BaseScreen(String string) {
		this.batch = new SpriteBatch();
		this.name=string;
		Gdx.app.log(Game.LOG, "Creating "+name);
	}
	public BaseScreen() {
		this.batch = new SpriteBatch();
	}
	public  void update (float delta) {
	}

	public  void render () {
	}

	public  boolean isDone () {
		return false;
	}

	public  void dispose () {
		//batch.dispose();
		Gdx.app.log(Game.LOG, "Disposing "+name);
	}
	
	public  void OnPause() {
	}
	
	public  void OnResume() {
	}
	
	public void UnProjectCamera(int x,int y,OrthographicCamera cam,float startX,float startY,float width,float height)
	{
		TouchPoint.set(x,y,0);
		cam.unproject(TouchPoint/*,startX,startY,width,height*/);
	}
	
	public  void OnKeyUp(int keyCode) {
	}
	
	public  void OnKeyDown(int keyCode) {
	}
	
	public int GetScreenID()
	{
		return BackScreenID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
