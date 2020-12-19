package com.mobezer.jet;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Path{
	   ArrayList<Vector2>  positions;
	   ArrayList<Float> times;
	   Vector2 velocity;
	   int currentPointIndex;
	   int nextPointIndex;
	   int direction=1;
	   static final float CHECK_RADIUS=1f;
	   public Path(int count){
	      positions=new ArrayList<Vector2>();
	      times=new ArrayList<Float>();
	      velocity=new Vector2();
	   }
	   public void AddPoint(Vector2 pos,float time){
	      positions.add(pos);
	      
	      times.add(time);
	    }
	   public void Reset(){
	      currentPointIndex=0;
	      nextPointIndex=GetNextPoint();
	      SetNextPointVelocity();
	   }

	   public Vector2 GetCurrentPoint(){
		   return positions.get(currentPointIndex);
	   }
	   
	   public boolean UpdatePath(Vector2 bodyPosition){
	      return ReachedNextPoint(bodyPosition);
	   } 

	   boolean ReachedNextPoint(Vector2 bodyPosition){
	         Vector2 nextPointPosition=positions.get(nextPointIndex);
	         float d=nextPointPosition.dst2(bodyPosition);
	         if(d<CHECK_RADIUS){
	            currentPointIndex=nextPointIndex;
	            nextPointIndex=GetNextPoint();
	        	SetNextPointVelocity();
	            return true;
	         }
	         return false;
	   } 
	   int GetNextPoint(){
	         int nextPoint=currentPointIndex+direction;
	         if(nextPoint==positions.size()){
	             nextPoint=0;
	         }else if(nextPoint==-1){
	             nextPoint=positions.size()-1;
	         }
	         return nextPoint;
	   } 

	   void SetNextPointVelocity(){
	        Vector2 nextPosition=positions.get(nextPointIndex);
	        Vector2 currentPosition=positions.get(currentPointIndex);
	        float dx=nextPosition.x-currentPosition.x;
	        float dy=nextPosition.y-currentPosition.y;
	        float time=times.get(nextPointIndex);
	        velocity.set(dx/time,dy/time);
	   }
	   public Vector2 GetVelocity(){
	        return velocity;
	   }
	}