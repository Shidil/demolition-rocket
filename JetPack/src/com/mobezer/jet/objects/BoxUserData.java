package com.mobezer.jet.objects;

public class BoxUserData {
	public int BodyIndex;
	public int BodyCollisionGroup;
	public String name="default";
	public BaseBoxObject obj;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BaseBoxObject getObj() {
		return obj;
	}

	public void setObj(BaseBoxObject obj) {
		this.obj = obj;
	}

	public BoxUserData(int index,int group)
	{
		SetIndex(index);
		SetCollisionGroup(group);
	}
	
	public void SetIndex(int index){
		BodyIndex=index;
	}
	
	public void SetCollisionGroup(int group){
		BodyCollisionGroup=group;
	}

}
