package com.mobezer.jet.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Widget {
	public Vector2 Position;
	public Boolean IsClicked;
	private TouchListner onTapListner;
	private UpdateListner onUpdateListner;

	public Widget(Vector2 pos) {
		Position = pos;
		IsClicked = false;
	}

	public void Draw(SpriteBatch sp) {

	}

	public void touch(float x, float y) {
		// TODO Auto-generated method stub	
	}
	public TouchListner getOnTapListner() {
		return onTapListner;
	}
	public UpdateListner getOnUpdateListner() {
		return onUpdateListner;
	}
	public void update(float delta) {
		if (onUpdateListner != null) {
			onUpdateListner.update(delta);
		}
	}

	public void tap() {
		if (onTapListner != null) {
			onTapListner.tap();
		}	
	}
	public void setOnTapListner(TouchListner onTapListner) {
		this.onTapListner = onTapListner;
	}
	
	public void setonUpdateListner(UpdateListner onUpdateListner) {
		this.onUpdateListner = onUpdateListner;

	}

	public Boolean IsInside(float tapx, float tapy) {
		// TODO Auto-generated method stub
		return null;
	}
}
