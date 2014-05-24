package com.mobezer.jet.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mobezer.jet.Game;

public class WidgetPool {
	HashMap<Integer, Widget> itemPool = new HashMap<Integer, Widget>();
	private int count = 0;
	private OrthographicCamera guiCam;
	private Vector3 touchPoint=new Vector3();
	public WidgetPool() {

	}

	public void add(Widget item) {
		itemPool.put(count, item);
		count++;
	}

	public void remove(Widget item) {
		itemPool.remove(item);
	}

	public void update(float delta) {
		Iterator<Entry<Integer, Widget>> it = itemPool.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Widget> item=(Entry<Integer, Widget>) it.next();
			item.getValue().update(delta);
			//it.remove();
		}
	}
	public void draw(SpriteBatch batch) {
		Iterator<Entry<Integer, Widget>> it = itemPool.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Widget> item=(Entry<Integer, Widget>) it.next();
			item.getValue().Draw(batch);
			//it.remove();
		}	
	}
	public void listenTap(int screenX, int screenY) {
		Iterator<Entry<Integer, Widget>> it = itemPool.entrySet().iterator();
		guiCam.unproject(touchPoint.set(screenX, screenY, 0),Game.CAMSTARTX, Game.CAMSTARTY,
				Game.CAMWIDTH, Game.CAMHEIGHT);
		while (it.hasNext()) {
			//System.out.println("touch");
			Map.Entry<Integer, Widget> item=(Entry<Integer, Widget>) it.next();
			if (item.getValue().IsClicked/*IsInside(touchPoint.x, touchPoint.y)*/ ){
				System.out.println("tap");
				item.getValue().tap();
			}
			//it.remove();
		}
	}
	public void listenTouch(int screenX, int screenY) {
		Iterator<Entry<Integer, Widget>> it = itemPool.entrySet().iterator();
		guiCam.unproject(touchPoint.set(screenX, screenY, 0),Game.CAMSTARTX, Game.CAMSTARTY,
				Game.CAMWIDTH, Game.CAMHEIGHT);
		while (it.hasNext()) {
			//System.out.println("touch");
			Map.Entry<Integer, Widget> item=(Entry<Integer, Widget>) it.next();
				item.getValue().touch(touchPoint.x, touchPoint.y);
			}
			//it.remove();
		}

	public OrthographicCamera getGuiCam() {
		return guiCam;
	}

	public void setGuiCam(OrthographicCamera guiCam) {
		this.guiCam = guiCam;
	}

}
