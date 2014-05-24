package com.mobezer.jet;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Rotation{
	   ArrayList<Float>  angles;
	   ArrayList<Float> times;
	   Vector2 velocity;
	   int currentPointIndex;
	   int nextPointIndex;
	   int direction=1;
	   static final float CHECK_RADIUS=1f;
	   public Rotation(int count){
	      angles=new ArrayList<Float>();
	      times=new ArrayList<Float>();
	      velocity=new Vector2();
	   }
	   public void AddAngle(Float pos,float time){
	      angles.add(pos);      
	      times.add(time);
	    }
	   public void Reset(){
	      currentPointIndex=0;
	      nextPointIndex=GetNextPoint();
	      SetNextPointVelocity();
	   }

	   public Float GetCurrentPoint(){
		   
		   return angles.get(currentPointIndex);
	   }
	   
	   public boolean UpdatePath(float bodyPosition){
	      return ReachedNextPoint(bodyPosition);
	   } 

	   @SuppressWarnings("unused")
	boolean ReachedNextPoint(float bodyPosition){
	         Float nextAngle=angles.get(nextPointIndex);
	         /*if(d<CHECK_RADIUS){
	            currentPointIndex=nextPointIndex;
	            nextPointIndex=GetNextPoint();
	        	SetNextPointVelocity();
	            return true;
	         }*/
	         return false;
	   } 
	   int GetNextPoint(){
	         int nextPoint=currentPointIndex+direction;
	         if(nextPoint==angles.size()){
	             nextPoint=0;
	         }else if(nextPoint==-1){
	             nextPoint=angles.size()-1;
	         }
	         return nextPoint;
	   } 

	   void SetNextPointVelocity(){
	        Float nextPosition=angles.get(nextPointIndex);
	        Float currentPosition=angles.get(currentPointIndex);
	        float dx=nextPosition-currentPosition;
	        float time=times.get(nextPointIndex);
	        velocity.set(dx/time,0);
	   }
	   public Vector2 GetVelocity(){
	        return velocity;
	   }
	}