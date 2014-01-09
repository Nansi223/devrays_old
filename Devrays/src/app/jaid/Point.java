package app.jaid;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;

public class Point {

	public static final Point	neutral	= new Point(0, 0);

	public static Point fromScreenPoint(int x, int y, Camera camera)
	{
		return new Point(Math.min(Math.max(0, x), Devrays.screenWidth) / (Devrays.screenWidth / camera.viewportWidth) + camera.position.x - camera.viewportWidth / 2, Math.min(Math.max(0, y), Devrays.screenHeight) / (Devrays.screenHeight / camera.viewportHeight) + camera.position.y - camera.viewportHeight / 2);
	}

	public static Point midOf(float posX, float posY, float width, float height)
	{
		return new Point(posX + width / 2, posY + height / 2);
	}

	public static Point midOf(Rectangle rect)
	{
		return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
	}

	public float	x, y;

	public Point()
	{}

	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float angleTo(Point origin)
	{
		return (float) Math.toDegrees(Math.atan2(origin.x - x, origin.y - y));
	}

	public Point applyToGrid(float steps)
	{
		float invert = 1f / steps;
		return new Point(Math.round(x * (int) invert) / invert, Math.round(y * (int) invert) / invert);
	}

	public int directionTo(Point origin, int steps)
	{
		int direction = Math.round(angleTo(origin) / (360 / steps));
		return steps % 2 == 0 && direction == steps / 2 ? -direction : direction;
	}

	public float distanceTo(Point origin)
	{
		return (float) Math.sqrt(Math.pow(origin.y - y, 2) + Math.pow(origin.x - x, 2));
	}

	@Override
	public boolean equals(Object object) // Das Object-equals() gibt bei den zwei gleichen Floats komischerweise trotzdem false zurück, deshalb overridden
	{
		Point point = (Point) object;

		if (x == point.x && y == point.y)
			return true;
		return false;
	}

	public void move(float direction, float distance)
	{
		x -= Math.sin(Math.toRadians(direction)) * distance;
		y -= Math.cos(Math.toRadians(direction)) * distance;
	}

	public void moveTo(Point origin, float distance)
	{
		if (distance > 0 && distanceTo(origin) < distance)
			set(origin);
		else
			move(origin.angleTo(this), distance);
	}

	public void rotateAround(Point origin, float angle)
	{
		// float[] pt = {x, y};
		// AffineTransform.getRotateInstance(Math.toRadians(angle), origin.x,
		// origin.y).transform(pt, 0, pt, 0, 1);
		// set(pt[0], pt[1]);
	}

	public void set(float newX, float newY)
	{
		x = newX;
		y = newY;
	}

	public void set(Point update)
	{
		set(update.x, update.y);
	}

	public void setRotation(Point origin, float angle, float distance)
	{
		set(origin);
		move(angle, distance);
	}

	@Override
	public String toString()
	{
		return "@ <" + x + ", " + y + ">";
	}

}
