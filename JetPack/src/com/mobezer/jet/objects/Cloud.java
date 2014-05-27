package com.mobezer.jet.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mobezer.jet.Assets;
import com.mobezer.jet.GameWorld;
import com.mobezer.jet.TextureDimensions;
import com.mobezer.jet.TextureWrapper;

public class Cloud extends BaseBoxObject {
	public static final int CLOUD_STATE_IDLE = 0;
	public static final int CLOUD_STATE_MOVE = 1;
	public static final float CLOUD_WIDTH = TextureDimensions.CLOUD_WIDTH;
	public static final float CLOUD_HEIGHT = TextureDimensions.CLOUD_HEIGHT;	
	// Define movement variables
	public static float CLOUD_MOVE_VELOCITY = 0.8f;
	public int state;
	public TextureWrapper texture;
	public float stateTime=0;
	public Vector2 position;

	public Cloud(float px, float py) {
		super(GameWorld.boxManager.GetNewObjectIndex(), 2);
		position = new Vector2(px, py);
		texture = new TextureWrapper(Assets.cloud_storm, position);
		SetTextureDimension(CLOUD_WIDTH, CLOUD_HEIGHT);
		MakeBody(CLOUD_WIDTH, CLOUD_HEIGHT, 0,"cloud_storm" ,BodyType.KinematicBody, 10, 0, position, 0);
		boxUserData.setName("cloud_storm");
		boxUserData.setObj(this);
		body.setUserData(boxUserData);
		state = CLOUD_STATE_IDLE;
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
			super.Update(dt);
			stateTime+=dt;
			position.set(bodyWorldPosition);
			texture.Position.set(position);
			body.setTransform(body.getPosition(),texture.rotation*MathUtils.degreesToRadians);
	}
	
	public float GetBodyRotationInDegrees() {
		return body.getAngle() * MathUtils.radiansToDegrees;
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

	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
}
