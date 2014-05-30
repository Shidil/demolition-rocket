package com.mobezer.jet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraHelper {
	
	public static int physicalHeight;
	public static float viewportWidth;
	public static float viewportHeight;
	public static int physicalWidth;
	public static float aspect;
	public static float camX,camY;

	public static OrthographicCamera GetCamera(float virtualWidth,
			float virtualHeight) {
		viewportWidth = virtualWidth;
		viewportHeight = virtualHeight;
		physicalWidth = Gdx.graphics.getWidth();
		physicalHeight = Gdx.graphics.getHeight();
		aspect = virtualWidth / virtualHeight;
		// This is to maintain the aspect ratio.
		// If the virtual aspect ration does not match with the aspect ratio
		// of the hardware screen then the viewport would scaled to
		// meet the size of one dimension and other would not cover full
		// dimension
		// If we stretch it to meet the screen aspect ratio then textures will
		// get distorted either become fatter or elongated
		if (physicalWidth / physicalHeight >= aspect) {
			// Letterbox left and right.
			viewportHeight = virtualHeight;
			viewportWidth = viewportHeight * physicalWidth / physicalHeight;
		} else {
			// Letterbox above and below.
			viewportWidth = virtualWidth;
			viewportHeight = viewportWidth * physicalHeight / physicalWidth;
		}
		OrthographicCamera camera = new OrthographicCamera(viewportWidth,
				viewportHeight);
		camX = virtualWidth / 2;
		//camY = viewportHeight/2;
		camY = virtualHeight/2;
		camera.position.set(virtualWidth / 2, virtualHeight/2, 0);
		//camera.position.set(virtualWidth / 2, viewportHeight/2, 0);
		camera.update();
		return camera;
	}
}
