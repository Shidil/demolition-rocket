package com.mobezer.jet.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shidil Boss Mobezer
 * 
 */
public class GameObject {
	public final Vector2 position;
	public final Vector2 origin;
	public final Rectangle bounds;

	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.origin = position;
		this.bounds = new Rectangle(x - width / 2, y - height / 2, width,
				height);
	}
}
