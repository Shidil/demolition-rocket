package com.mobezer.jet.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.Assets;
import com.mobezer.jet.GameWorld;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;

public class Bob extends DynamicGameObject {
	public static final int BOB_STATE_IDLE = -1;
	public static final int BOB_STATE_FLY = 0;
	public static final int BOB_STATE_FALL = 1;
	public static final int BOB_STATE_HIT = 2;
	public static final int BOB_STATE_SHEILD = 3;
	public static final int BOB_STATE_MAGNET = 4;
	public static final float BOB_WIDTH = TextureDimensions.BOB_WIDTH;
	public static final float BOB_HEIGHT = TextureDimensions.BOB_HEIGHT;	
	// Define movement variables
	public static float BOB_FLY_VELOCITY = 120;
	public static float BOB_MOVE_VELOCITY = 420;
	public static float BOB_ACCELERATION = 0.5f; // pixels/second/second
	// score is a static property of the character :D
	public static int SCORE = 0;
	public int state;
	public int bonusState;
	public TextureWrapper texture;
	public float stateTime=0,runTime=0, sheildTime = 0, jumpPictureTime = 0;

	public Bob(float px, float py) {
		super(px,py,BOB_WIDTH,BOB_HEIGHT);
		Vector2 pos = new Vector2(px, py);
		texture = new TextureWrapper(Assets.jet, pos);
		SetTextureDimension(BOB_WIDTH, BOB_HEIGHT);
		SCORE = 0;
		state = BOB_STATE_IDLE;
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
			if(runTime>40.0){
				BOB_FLY_VELOCITY+=BOB_ACCELERATION;
				runTime+=0;
			}
			velocity.y = BOB_FLY_VELOCITY;
			velocity.add(GameWorld.gravity.x * dt, GameWorld.gravity.y * dt);
			position.add(velocity.x * dt, velocity.y * dt);
			bounds.x = position.x - bounds.width / 2;
			bounds.y = position.y - bounds.height / 2;

			if (velocity.y > 0 && state != BOB_STATE_HIT
					&& state != BOB_STATE_MAGNET) {
				if (state != BOB_STATE_FLY) {
					state = BOB_STATE_FLY;
					stateTime = 0;
				}
			}
			if (velocity.y < 0 && state != BOB_STATE_HIT) {
				if (state != BOB_STATE_FALL) {
					state = BOB_STATE_FALL;
					stateTime = 0;
				}
			}

			if (position.x < 0)
				position.x = GameWorld.WORLD_WIDTH;
			if (position.x > GameWorld.WORLD_WIDTH)
				position.x = 0;
			sheildTime += dt;
			stateTime += dt;
			jumpPictureTime+=dt;
			if(sheildTime>15&&bonusState==BOB_STATE_SHEILD)
			{
				stateTime=0;
				sheildTime=0;
				bonusState=0;
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
