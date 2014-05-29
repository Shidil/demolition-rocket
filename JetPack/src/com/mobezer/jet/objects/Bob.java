package com.mobezer.jet.objects;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mobezer.jet.Assets;
import com.mobezer.jet.Game;
import com.mobezer.jet.GameWorld;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;
import com.mobezer.tween.TextureAccessor;

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
	public static float BOB_FLY_VELOCITY = 190;
	public static float BOB_MAX_VELOCITY = 300;
	public static float BOB_MOVE_VELOCITY = 550;
	public static float BOB_ACCELERATION = 0.2f; // pixels/second/second
	// score is a static property of the character :D
	public static int SCORE = 0;
	public int state;
	public int bonusState,level=1;
	public TextureWrapper texture;
	public float stateTime=0,runTime=0, sheildTime = 0, jumpPictureTime = 0;
	public Polygon polyBounds;
	public float []vertices;

	public Bob(float px, float py) {
		super(px,py,BOB_WIDTH,BOB_HEIGHT);
		Vector2 pos = new Vector2(px, py);
		texture = new TextureWrapper(Assets.jet, pos);
		SetTextureDimension(BOB_WIDTH, BOB_HEIGHT);
		vertices = new float[]{0,41,11,26,20,-10,16,-22,0,-28,-15,-25,-18,-10,-11,23};
		polyBounds = new Polygon();
		polyBounds.setOrigin(0, 0);
		polyBounds.setOrigin(px, py);
		polyBounds.setPosition(px, py);
		polyBounds.setVertices(vertices);
		SCORE = 0;
		state = BOB_STATE_FLY;
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
			if(state == BOB_STATE_FLY){
				if(runTime>7){
					BOB_FLY_VELOCITY+=BOB_ACCELERATION;
					if(BOB_FLY_VELOCITY>BOB_MAX_VELOCITY)
						BOB_FLY_VELOCITY=BOB_MAX_VELOCITY;
					
					level++;
				}
				if(runTime>9){
					runTime=0;
				}
				velocity.y = BOB_FLY_VELOCITY;
				double angle=MathUtils.radiansToDegrees*(Math.atan2(velocity.y, velocity.x));
				angle = angle-90;
				Timeline.createSequence()
				.push(Tween.to(texture, TextureAccessor.ROTATION, 0.22f)
						.target((int) angle).ease(Linear.INOUT))
				.start(Game.tweenManager);
				//texture.SetRotation((int) angle);
				if(velocity.x>0){
					velocity.y = (float) (velocity.y+((int) angle+30));
					if(velocity.y<30)
						velocity.y = 30;
				}
				if(velocity.x<0){
					velocity.y = (float) (velocity.y-((int) angle-30));
					if(velocity.y<50)
						velocity.y = 50;
				}
				//Gdx.app.log("velocity", ""+(velocity));
			}

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

			if (position.x-(BOB_WIDTH/2-10) < 0)
				position.x = BOB_WIDTH/2-10;
			if (position.x+(BOB_WIDTH/2-10) > GameWorld.WORLD_WIDTH)
				position.x = GameWorld.WORLD_WIDTH-(BOB_WIDTH/2-10);
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
			polyBounds.setPosition(position.x, position.y);
			polyBounds.setOrigin(0, 0);
			polyBounds.dirty();
			polyBounds.setRotation(texture.getRotation());
			polyBounds.dirty();
			//bounds.
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

	public void hitStorm() {
		velocity.set(0,10);
		state = BOB_STATE_HIT;
		stateTime = 0;
	}
}
