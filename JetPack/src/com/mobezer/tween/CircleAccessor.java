package com.mobezer.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Color;
import com.mobezer.jet.TextureWrapper;

/**
 * @author Shidil Boss | http://www.mobezer.com
 */
public class CircleAccessor implements TweenAccessor<TextureWrapper> {
	public static final int POS_XY = 1;
	public static final int CPOS_XY = 2;
	public static final int SCALE_XY = 3;
	public static final int ROTATION = 4;
	public static final int OPACITY = 5;
	public static final int TINT = 6;
	public static final int POS_X = 7;
	public static final int POS_Y = 8;

	@Override
	public int getValues(TextureWrapper target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POS_X:
			returnValues[0] = target.getX();
			return 1;
		case POS_Y:
			returnValues[0] = target.getY();
			return 1;
		case POS_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;

		case CPOS_XY:
			returnValues[0] = target.getX() + target.GetWidth() / 2;
			returnValues[1] = target.getY() + target.GetHeight() / 2;
			return 2;

		case SCALE_XY:
			returnValues[0] = target.getScaleX();
			returnValues[1] = target.getScaleY();
			return 2;

		case ROTATION:
			returnValues[0] = target.getRotation();
			return 1;
		case OPACITY:
			returnValues[0] = target.getColor().a;
			return 1;

		case TINT:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
			return 3;

		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(TextureWrapper target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POS_X:
			target.setX(newValues[0]);
		case POS_Y:
			target.setY(newValues[0]);
		case POS_XY:
			target.setPosition(newValues[0], newValues[1]);
			break;
		case CPOS_XY:
			target.setPosition(newValues[0] - target.GetWidth() / 2,
					newValues[1] - target.GetHeight() / 2);
			break;
		case SCALE_XY:
			target.SetScale(newValues[0], newValues[1]);
			break;
		case ROTATION:
			target.setRotation(newValues[0]);
			break;

		case OPACITY:
			Color c = target.getColor();
			c.set(c.r, c.g, c.b, newValues[0]);
			target.SetColor(c);
			break;

		case TINT:
			c = target.getColor();
			c.set(newValues[0], newValues[1], newValues[2], c.a);
			target.SetColor(c);
			break;

		default:
			assert false;
		}
	}
}
