package com.mobezer.jet.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.Assets;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;

public class Enemey extends DynamicGameObject {
	public static final int ENEMEY_STATE_IDLE = -1;
	public static final int ENEMEY_STATE_FLY = 0;
	public static final int ENEMEY_STATE_FALL = 1;
	public static final int ENEMEY_STATE_HIT = 2;
	public static final int ENEMEY_STATE_SHEILD = 3;
	public static final int ENEMEY_STATE_MAGNET = 4;
	public static final float ENEMEY_WIDTH = TextureDimensions.ENEMEY_WIDTH;
	public static final float ENEMEY_HEIGHT = TextureDimensions.ENEMEY_HEIGHT;	
	// Define movement variables
	public static float ENEMEY_FLY_VELOCITY = 30;
	public static float ENEMEY_MOVE_VELOCITY = 20;
	public static float ENEMEY_ACCELERATION = 0.4f; // pixels/second/second
	// score is a static property of the character :D
	public static int SCORE = 0;
	public int state;
	public int bonusState;
	public TextureWrapper texture;
	public float stateTime=0,runTime=0, sheildTime = 0, jumpPictureTime = 0;

	public Enemey(float px, float py) {
		super(px,py,ENEMEY_WIDTH,ENEMEY_HEIGHT);
		Vector2 pos = new Vector2(px, py);
		texture = new TextureWrapper(Assets.enemey, pos);
		SetTextureDimension(ENEMEY_WIDTH, ENEMEY_HEIGHT);
		SCORE = 0;
		state = ENEMEY_STATE_IDLE;
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
			runTime+=dt;
			// give acceleration
			if(runTime>1.0){
				ENEMEY_FLY_VELOCITY+=ENEMEY_ACCELERATION;
				runTime+=0;
			}
			texture.Position.set(position);
			// texture.rotation=GetBodyRotationInDegrees();
	}
	


	public void movLeft() {

		
	}

	public void movRight() {

	}

	public void stop() {

		
	}

	public void catchPack(int package_TYPE) {
		// TODO Auto-generated method stub
		
	}
}
