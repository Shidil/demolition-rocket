package com.mobezer.jet.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mobezer.jet.GameWorld;

public abstract class BaseBoxObject implements ObjectModifier{

	protected Body body;

	protected BoxUserData boxUserData;
	public Boolean IsAlive=true;
	public Vector2 bodyWorldPosition;
	
	public BaseBoxObject(int bodyIndex,int collisionGroup){
		IsAlive=true;
		bodyWorldPosition=new Vector2();
		boxUserData=new BoxUserData(bodyIndex, collisionGroup);
		
	}
	
	public void MakeBody(float width,float  height,float radius,String shape, BodyDef.BodyType bodyType,
			float density,float restitution, Vector2 pos,float angle)
	{
		World world=BoxObjectManager.GetWorld();
		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = bodyType;
		
		jumperBodyDef.position.set(BoxObjectManager.ConvertToBox(pos.x),BoxObjectManager.ConvertToBox(pos.y));
		
		jumperBodyDef.angle=angle;
		
		body = world.createBody(jumperBodyDef);
		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		if(shape==null){
			if(radius==0)
			{
				MakeRectBody(width,height,bodyType,density,restitution,pos,angle);
		 		
		 	}else{
		 		MakeCircleBody(radius,bodyType,density,restitution,pos,angle);
		 	}
		}
		else{
			float w=BoxObjectManager.ConvertToBox(width/1f);
			//float h=BoxObjectManager.ConvertToBox(height/1f);
			//PolygonShape bodyShape = new PolygonShape();
			//bodyShape.setAsBox(w,h);
			
			FixtureDef fixtureDef=new FixtureDef();
	 		fixtureDef.density=density;
	 		fixtureDef.friction=0.07f;
	 		fixtureDef.restitution=restitution;
	 		//fixtureDef.shape=bodyShape;
	 		GameWorld.shapeLoader.attachFixture(body, shape, fixtureDef, w);
	 		body.createFixture(fixtureDef);
			//bodyShape.dispose();
		}
 
		/**
		 * The character should not ever spin around on impact.
		 */
		
		bodyWorldPosition.set(BoxObjectManager.ConvertToWorld(body.getPosition().x),
				BoxObjectManager.ConvertToWorld(body.getPosition().y));
	}
	
	public Vector2 GetPosition(){
		return bodyWorldPosition;
	}
	
	void MakeRectBody(float width,float height,BodyDef.BodyType bodyType,
			float density,float restitution, Vector2 pos,float angle){
		
		float w=BoxObjectManager.ConvertToBox(width/2f);
		float h=BoxObjectManager.ConvertToBox(height/2f);
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(w,h);
		
		FixtureDef fixtureDef=new FixtureDef();
 		fixtureDef.density=density;
 		fixtureDef.friction=0.07f;
 		fixtureDef.restitution=restitution;
 		fixtureDef.shape=bodyShape;
 		
 		body.createFixture(fixtureDef);
		bodyShape.dispose();
	}
	
	void MakeCircleBody(float radius,BodyDef.BodyType bodyType,
			float density,float restitution, Vector2 pos,float angle){
		
		FixtureDef fixtureDef=new FixtureDef();
 		fixtureDef.density=density;
 		fixtureDef.friction=0.01f;
 		fixtureDef.restitution=restitution;
 		fixtureDef.shape=new CircleShape();
 		fixtureDef.shape.setRadius(BoxObjectManager.ConvertToBox(radius));
 		
 		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
	}
	
	public void DestroyBody()
	{
		if(body!=null)
		{
			IsAlive=false;
			BoxObjectManager.GetWorld().destroyBody(body);
			body=null;
		}
	}
	
	public boolean IsClicked(float wx,float wy){
		Array<Fixture> flist=body.getFixtureList();
		Boolean present=false;
		wx=BoxObjectManager.ConvertToBox(wx);
		wy=BoxObjectManager.ConvertToBox(wy);
		for(Fixture f:flist){
			present=f.testPoint(wx, wy);
			if(present)
				return present;
		}
		return present;
	}
	
	public void UpdateWorldPosition()
	{
		bodyWorldPosition.set(BoxObjectManager.ConvertToWorld(body.getPosition().x),
				BoxObjectManager.ConvertToWorld(body.getPosition().y));
		
	}
	
	public void SetWorldVelocity(Vector2 v){
		SetWorldVelocity(v.x,v.y);
	}
	
	public void SetWorldVelocity(float vx,float vy){
		vx=BoxObjectManager.ConvertToBox(vx);
		vy=BoxObjectManager.ConvertToBox(vy);
		body.setLinearVelocity(vx,vy);
	}
	
	public void SetPosition(float wx,float wy){
		wx=BoxObjectManager.ConvertToBox(wx);
		wy=BoxObjectManager.ConvertToBox(wy);
		float angle=body.getAngle();
		body.setTransform(wx, wy, angle);
		UpdateWorldPosition();
	}
	
	public void SetTransform(BaseBoxObject bbo){
		body.setTransform(bbo.GetBoxPosition(),bbo.GetAngle());
		UpdateWorldPosition();
	}
	
	public Vector2 GetBoxPosition(){
		return body.getPosition();
	}
	
	public float GetAngle(){
		return body.getAngle();
	}
	
	public int GetBoxIndex(){
		return boxUserData.BodyIndex;
	}
	
	public int GetBoxCollisionGroup(){
		return boxUserData.BodyCollisionGroup;
	}
	
	public void SetPosition(Vector2 v){
		SetPosition(v.x, v.y);
	}
	
	public void Update(float dt){ 
		UpdateWorldPosition();
		rotate();
	}
	
	public void SetUserData(int index){
		boxUserData.SetIndex(index);
	}
	
	public void SetBodyCollisionType(int group){
		boxUserData.SetCollisionGroup(group);
		
	}
	
	public abstract void Draw(SpriteBatch sp);
	public float getX() {
		return GetPosition().x;

	}
	public float getY() {
		return GetPosition().y;

	}
	public void setRotation(float angle){
		angle*=MathUtils.degreesToRadians;
		body.setTransform(GetBoxPosition(), GetAngle()+angle);
	}
}
