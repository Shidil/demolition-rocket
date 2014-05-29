package com.mobezer.jet.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.TextWrapper;

public class Label extends Widget {
	public TextWrapper Text;

	public Label(String text,BitmapFont font,Vector2 pos) {
		super(pos);
		isTouchable = false;
		if(text!=null)
			Text = new TextWrapper(text, font, pos);
	}
	public void setText(String text){
		Text.Text = text;
	}
	@Override
	public void Draw(SpriteBatch sp) {
		if (Text != null){
			//Text.Position.set(position);
			Text.Draw(sp);
		}
			
	}
	@Override
	public void touch(float tapx, float tapy) {
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	@Override
	public Boolean IsInside(float tapx, float tapy) {
		return false;
	}
	@Override
	public void tap() {
		super.tap();
	}
}