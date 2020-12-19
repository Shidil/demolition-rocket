package com.mobezer.jet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TextureWrapper {
	public Texture texture;
	public Vector2 Position;
	public Vector2 Velocity;
	public TextureRegion region;

	int srcX;
	int srcY;
	int srcWidth;
	int srcHeight;
	int destWidth;
	int destHeight;

	int colPadding;

	public float rotation;
	float rotationVelocity;

	float scaleX;
	float scaleY;
	float originX;
	float originY;
	private Color color;
	public boolean flip = false;
	public TextureWrapper(TextureRegion tex, Vector2 pos) {
		SetTexture(tex);
		colPadding = 0;
		Position = pos;
		scaleX = 1;
		scaleY = 1;
		color = new Color(1, 1, 1, 1);
		SetFilter(TextureFilter.MipMap, TextureFilter.Linear);
		Velocity = new Vector2();
	}

	public void SetFilter(TextureFilter min, TextureFilter max) {
		texture.setFilter(min, max);
	}

	public void SetTexture(TextureRegion region, int width, int height) {
		this.region = region;
		texture = region.getTexture();
		srcX = region.getRegionX();
		srcY = region.getRegionY();
		srcWidth = region.getRegionWidth();
		srcHeight = region.getRegionHeight();
		destWidth = width;
		destHeight = height;
	}

	public void SetTexture(TextureRegion region) {


		SetTexture(region, region.getRegionWidth(), region.getRegionHeight());

	}

	public int GetWidth() {
		return destWidth;
	}

	public int GetHeight() {
		return destHeight;
	}

	public void SetOrigin(int originx, int originy) {
		originX = originx;
		originY = originy;
	}

	public void SetDimension(int width, int height) {
		destWidth = width;
		destHeight = height;
		originX = width / 2;
		originY = height / 2;
	}

	public void SetColor(Color c) {
		this.color = c;
	}

	public void SetScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}

	public Vector2 getPosition() {
		return Position;
	}

	public void setPosition(Vector2 position) {
		Position = position;
	}
	public void setPosition(float x,float y) {
		Position = new Vector2(x, y);
	}
	public void SetRotation(int r) {
		rotation = r;
	}

	public void SetVelocity(float rv) {
		SetVelocity(Velocity, rv);
	}

	public void SetVelocity(float x, float y) {
		SetVelocity(x, y, rotationVelocity);
	}

	public void SetVelocity(Vector2 v, float rot) {
		SetVelocity(v.x, v.y, rot);
	}

	public void SetVelocity(float vx, float vy, float rot) {
		rotationVelocity = rot;
		Velocity.set(vx, vy);
	}

	public void Draw(SpriteBatch sp) {
		if(flip) region.flip(true, false);
		else region.flip(false, false);
		sp.setColor(color);
		sp.draw(region, Position.x - destWidth / 2,
				Position.y - destHeight / 2, originX, originY, destWidth,
				destHeight, scaleX, scaleY, rotation);
		sp.setColor(1,1,1,1);

	}
	
	public void Update(float dt) {
		Position.x += Velocity.x * dt;
		Position.y += Velocity.y * dt;
		rotation += rotationVelocity * dt;
	}

	public Boolean IsClicked(float x, float y) {
		if (x > (Position.x - destWidth / 2 - colPadding)
				&& x < (Position.x + destWidth / 2 + colPadding)
				&& y > (Position.y - destHeight / 2 - colPadding)
				&& y < (Position.y + destHeight / 2 + colPadding))
			return true;
		return false;
	}

	public float getX() {
		return Position.x;
	}
	public float getY() {
		return Position.y;
	}
	public void setX(float x) {
		Position.x=x;
	}
	public void setY(float y) {
		Position.x=y;
	}

	public float getScaleX() {
		return scaleX;
	}
	public float getScaleY() {
		return scaleY;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	public float getRotation() {
		return rotation;
	}
	public Color getColor() {
		return color;
	}

	public void SetDimension(float width, float height) {
		destWidth = (int) width;
		destHeight = (int) height;
		originX = width / 2;
		originY = height / 2;
	}
}
