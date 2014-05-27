package com.mobezer.jet.objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mobezer.jet.GlobalSettings;

public class BoxObjectManager {
	static World world=new World(new Vector2(0,0), true);;
	public static final float BOX_TO_WORLD=100.0f;
	public static final float WORLD_TO_BOX=0.01f;
	
	public ArrayList<BaseBoxObject> bodies;
	

	float accumulator;
	boolean isPaused;
	
	
	public BoxObjectManager(){
		bodies=new ArrayList<BaseBoxObject>();
		isPaused=false;
	}
	
	public static World GetWorld(){
		return world;
	}

	public static float ConvertToBox(float x){
		return x*WORLD_TO_BOX;
	}
	
	public static float ConvertToWorld(float x){
		return x*BOX_TO_WORLD;
	}
	
	public void Pause(){
		accumulator=0;
		isPaused=true;
	}
	
	public int GetNewObjectIndex(){
		return bodies.size();
	}
	
	public void AddObject(BaseBoxObject obj){
		bodies.add(obj);
	}
	
	public void removeObject(BaseBoxObject obj){
		bodies.remove(obj);
		obj.DestroyBody();
	}
	
	public void Update(float dt){
		if(!isPaused){
			accumulator+=dt;
			while(accumulator>dt){
				world.step(GlobalSettings.BOX_STEP,GlobalSettings.VELOCITY_ITERATIONS, GlobalSettings.POSITION_ITERATIONS);
				accumulator-=GlobalSettings.BOX_STEP;
			}
		}
		
		for(BaseBoxObject bo:bodies){
			bo.Update(dt);
		}
	}
	
	public void Draw(SpriteBatch sp){
		for(BaseBoxObject bo:bodies){
			bo.Draw(sp);
		}
	}

	public void Resume(){
		accumulator=0;
		isPaused=false;
	}
	
	public void Dispose(){
		for(BaseBoxObject tbo:bodies){
			tbo.DestroyBody();
		}
		bodies.clear();
	}

}
