package app.jaid.devrays.screen.game;

import app.jaid.Jtil;
import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Joystick {

	public Rectangle	bounds;
	public int			touch	= -1, direction;
	private Point		ball;
	private float		idle	= 10, alpha, animation;

	public Joystick()
	{
		ball = new Point();
		resize();
	}

	public void render()
	{
		float ballSize = bounds.width / 2 + bounds.width / 10 * animation + (1 - alpha) * bounds.width / 2;

		if (animation != 0)
			animation = Jtil.normalize(animation, 50 * Devrays.delta);

		Devrays.uibatch.setColor(1, 1, 1, alpha / 2); // TEMP
		Devrays.uibatch.draw(Atlas.get("joystick_area_small"), bounds.x, bounds.y, bounds.width, bounds.height);
		Devrays.uibatch.setColor(1, 1, 1, 0.5f + alpha / 2); // TEMP
		Devrays.uibatch.draw(Atlas.get("joystick_ball"), bounds.x + ball.x - ballSize / 2, bounds.y + ball.y - ballSize / 2, ballSize, ballSize);
		Devrays.uibatch.setColor(1, 1, 1, 1); // TEMP
	}

	public void resize()
	{
		bounds = new Rectangle(0, 0, Gdx.graphics.getPpcX() * 2, Gdx.graphics.getPpcY() * 2);
	}

	public void update()
	{
		if (touch != -1)
		{
			idle = 0;
			alpha = 1;
			float lastDirection = direction;

			direction = new Point(Gdx.input.getX(touch), Devrays.screenHeight - Gdx.input.getY(touch)).directionTo(Point.midOf(bounds), 8);

			if (lastDirection != direction)
				animation = 10;

			ball.setRotation(Point.midOf(bounds), direction * 45, bounds.width / 3 + (float) Math.pow(animation, 1.5));

			Log.p("Joystick Angle", new Point(Gdx.input.getX(touch), Devrays.screenHeight - Gdx.input.getY(touch)).angleTo(Point.midOf(bounds)));
			Log.p("Joystick Direction", direction);
		}
		else
		{
			direction = -100;
			ball.set(Point.midOf(bounds));

			if ((idle += Devrays.delta) > 2)
				alpha = 1 - Math.min(1, idle - 2);
		}

	}
}