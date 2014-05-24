package com.mobezer.jet;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mobezer.jet.objects.Bob;
import com.mobezer.jet.objects.BoxObjectManager;
import com.mobezer.jet.objects.BoxRectObject;
import com.mobezer.jet.objects.BoxUserData;
import com.mobezer.jet.objects.Lane;
import com.mobezer.jet.objects.Package;

public class GameWorld {
	// World constants
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final int GAME_UNIT = 48;
	public static final int WORLD_WIDTH = GlobalSettings.VIRTUAL_WIDTH;
	public static final int WORLD_HEIGHT = GlobalSettings.VIRTUAL_HEIGHT;
	public static final Vector2 gravity = new Vector2(0, -100);
	public static OrthographicCamera camera; // camera to obtain projection
	// Physics
	public static BoxObjectManager boxManager;
	// public static BodyEditorLoader shapeLoader = new BodyEditorLoader(
	// Gdx.files.internal("shapes/shapes.json"));
									
	// Others
	public static final Random random=new Random();
	private int state;

	// Lists
	public ArrayList<Lane> lanes;
	public static ArrayList<Package> packages;
	// Game Charaters and core objects
	public Bob bob;
	
	// a list of points that define path of the heroBall
	float stateTime;
	private float interval=0;

	public GameWorld(OrthographicCamera cam) {
		GameWorld.camera = cam;
		boxManager = new BoxObjectManager();
		bob = new Bob(70, 190);
		lanes=new ArrayList<Lane>();
		packages = new ArrayList<Package>();
		lanes.add(new Lane(330));
		lanes.add(new Lane(290));
		lanes.add(new Lane(250));
		createCollisionListener();	
		createWall();
		boxManager.AddObject(bob);
		this.state = WORLD_STATE_RUNNING;
	}

	private void createWall() {
		//create ground
		BoxRectObject ground = new BoxRectObject(boxManager.GetNewObjectIndex(), 2, 640, 20, BodyType.StaticBody, 0, 0, 320, 10, 0, Assets.box);
		BoxRectObject left = new BoxRectObject(boxManager.GetNewObjectIndex(), 2, 10, 200, BodyType.StaticBody, 0, 0, 0, 100, 0, Assets.box);
		BoxRectObject right = new BoxRectObject(boxManager.GetNewObjectIndex(), 2, 10, 200, BodyType.StaticBody, 0, 0, 640, 100, 0, Assets.box);
		boxManager.AddObject(left);
		boxManager.AddObject(right);
		boxManager.AddObject(ground);
	}

	public void update(float delta) {
		if (state == WORLD_STATE_RUNNING) {
			stateTime+=delta;
			createPlanes();
			updateLanes();
			boxManager.Update(delta);
			updatePackages();
		}
	}

	private void updateLanes() {
		int laneSize= lanes.size();
		for (int j=0;j<laneSize;j++){
			lanes.get(j).update();
		}
		
	}
	private void updatePackages(){
		int size= packages.size();
		for (int j=0;j<size;j++){
			Package pack = packages.get(j);
			if(pack.isDone()){
				boxManager.removeObject(pack);
				packages.remove(j);
				size = packages.size();
			}
		}
	}

	private void createPlanes() {
		if(stateTime<interval) return;
		if(random.nextFloat()>0.95){
			int laneSize= lanes.size();
			for (int j=0;j<laneSize;j++){
				if(lanes.get(j).createPlanes()==true)return;
			}
			
		}
		interval=0.2f;
		stateTime=0;
	}
	
	public static void addPackage(Package pack) {
		packages.add(pack);
		boxManager.AddObject(pack);
		
	}

	public static OrthographicCamera getCamera() {
		return camera;
	}

	public void render(SpriteBatch batch) {
		boxManager.Draw(batch);
	}

	private void createCollisionListener() {
		BoxObjectManager.GetWorld().setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				BoxUserData itemA =  ((BoxUserData) fixtureA.getBody().getUserData());
				BoxUserData itemB = ((BoxUserData) fixtureB.getBody().getUserData());
				Gdx.app.log("Contact", "between " + itemA.name + " and " + itemB.name);
				// bob catches package
				if((itemA.name=="bob"&&itemB.name=="package")){
					((Bob) itemA.obj).catchPack(((Package)itemB.obj).PACKAGE_TYPE);
					((Package)itemB.obj).setDone(true);
				}
				if(itemA.name=="package"&&itemB.name=="bob"){	
					((Bob) itemB.obj).catchPack(((Package)itemA.obj).PACKAGE_TYPE);
					((Package)itemA.obj).setDone(true);
				}
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}

		});
	}

	public void dispose() {
		boxManager.Dispose();
	}

	public void tap() {
		//bird.fly();
	}

	public void bobRight() {
		//Bob moves right when direction is 1
		//bob.direction=1;	
		bob.movRight();
	}

	public void bobLeft() {
		bob.movLeft();
	}

	public void bobStop() {
		bob.stop();
	}


}
