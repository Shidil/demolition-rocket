package com.mobezer.jet.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Widget {
	public Vector2 position;
	public Boolean isTouchable, IsClicked;
	private TouchListner onTapListner;
	private UpdateListner onUpdateListner;
	private boolean enabled;

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Widget(Vector2 pos) {
		position = pos;
		IsClicked = false;
		enabled = true;
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
		if (onUpdateListner != null && isEnabled()) {
			onUpdateListner.update(delta);
		}
	}

	public boolean isEnabled() {
		return enabled;
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
