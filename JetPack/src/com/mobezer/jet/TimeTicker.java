package com.mobezer.jet;

public class TimeTicker{
	   float ticker;
	   public float TimeLimit;
	   boolean hasCrossedTimeLimit;
	   public boolean IsActive;
	   public TimeTicker(float timeLimit){
	        TimeLimit=timeLimit;
	        Reset();
	   }
	   public void Reset(){
	        hasCrossedTimeLimit=false;
	   }
	   public void ResetTicker(){
	     ticker=0;
	   } 
	   public void Update(float dt){
	        if(IsActive && !hasCrossedTimeLimit){
	           ticker+=dt;
	           if(ticker>TimeLimit){
	               hasCrossedTimeLimit=true;
	               ticker-=TimeLimit;
	           }
	        }
	   } 
	   public Boolean HasCrossedTimeLimit(){
	       return hasCrossedTimeLimit;
	   }
	}