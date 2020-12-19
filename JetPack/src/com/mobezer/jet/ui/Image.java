package com.mobezer.jet.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.TextWrapper;
import com.mobezer.jet.TextureWrapper;

public class Image extends Widget {
	public TextureWrapper BackTexture;
	public TextureWrapper ClickedTexture;
	public TextWrapper Text;

	public Image(TextureWrapper backTex,Vector2 pos) {
		super(pos);
		isTouchable =false;
		BackTexture = backTex;
		if(BackTexture!=null) BackTexture.setPosition(pos);
	}

	@Override
	public void Draw(SpriteBatch sp) {
			if (BackTexture != null){
				//BackTexture.setPosition(position);
				BackTexture.Draw(sp);
			}
			
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

}