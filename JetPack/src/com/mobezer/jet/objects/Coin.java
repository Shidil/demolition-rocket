package com.mobezer.jet.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.Assets;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;

public class Coin extends DynamicGameObject {
	public static final int COIN_STATE_IDLE = 0;
	public static final int COIN_STATE_HIT = 1;
	public static final float COIN_WIDTH = TextureDimensions.COIN_WIDTH;
	public static final float COIN_HEIGHT = TextureDimensions.COIN_HEIGHT;	
	// Define  variables
	public static float COIN_VALUE = 2;
	// score is a static property of the character :D
	public static int SCORE = 0;
	public int state;
	public TextureWrapper texture;
	public float stateTime=0;
	public float[] vertices;
	public Polygon polyBounds;

	public Coin(float px, float py) {
		super(px,py,COIN_WIDTH,COIN_HEIGHT);
		Vector2 pos = new Vector2(px, py);
		texture = new TextureWrapper(Assets.coin, pos);
		SetTextureDimension(COIN_WIDTH, COIN_HEIGHT);
		vertices = new float[]{7,7,7,-7,-7,-7,-7,7};
		polyBounds = new Polygon();
		polyBounds.setPosition(px, py);
		polyBounds.setOrigin(0, 0);
		polyBounds.setVertices(vertices);
		state = COIN_STATE_IDLE;
	}

	public void SetTextureDimension(float width, float height) {
		texture.SetDimension(width, height);
	}

	@Override
	public void Draw(SpriteBatch sp) {
		
			//Gdx.app.log("bob", "" + texture.Position);
			texture.Draw(sp);
	}

	public void Update(float dt) {
			stateTime+=dt;
			texture.Position.set(position);
			polyBounds.setPosition(position.x, position.y);
			polyBounds.setOrigin(0, 0);
			polyBounds.dirty();
			polyBounds.setRotation(texture.getRotation());
			polyBounds.dirty();
	}
	


	public void movLeft() {

		
	}

	public void movRight() {

	}

	public void stop() {

		
	}

}
