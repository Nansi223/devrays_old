package app.jaid.devrays.entity;

import app.jaid.Jtil;
import app.jaid.Point;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.Graphics;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.screen.game.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Entity {

	public static final float	MOVEMENT_STEP	= 0.2f;

	public boolean				colliding		= true, moveManually;

	public Color				color;
	public Point				position;
	public Rectangle			rect, bounds;
	public float				speed, angle;
	public TextureRegion		sprite;
	private Color				blinkColor;

	private float				blinkTime;
	protected float				scale, scaleX, scaleY;

	public Entity()
	{
		position = new Point();
		rect = new Rectangle();
		bounds = new Rectangle();
	}

	public void blink(Color color)
	{
		blinkTime = 1;
		blinkColor = color;
	}

	public boolean move()
	{
		float movement = speed * Devrays.delta;

		while (movement != 0)
		{
			if (movement > MOVEMENT_STEP)
				Log.m(movement);
			position.move(angle, movement > MOVEMENT_STEP ? MOVEMENT_STEP : movement);
			movement = movement > MOVEMENT_STEP ? movement - MOVEMENT_STEP : 0;

			// Später mit in den Constuctor und ab dann nach jedem Scale Change aufrufen
			updateBounds();

			if (colliding)
				checkCollisions();
		}

		if (!moveManually)
			speed = Jtil.normalize(speed, 10 * Devrays.delta);
		return update();

	}

	public void push(float angle, float power)
	{
		speed = Math.max(speed, power);
		this.angle = angle;
	}

	public void render()
	{
		Graphics.draw(sprite, position.x, position.y, scaleX, scaleY, blinkTime > 0 ? (color != null ? color : Color.WHITE).cpy().lerp(blinkColor, blinkTime -= Devrays.delta) : color != null ? color : Color.WHITE);
	}

	public void setScale(float scale)
	{
		this.scale = scale;
		updateScaleCache();
	}

	public boolean update()
	{
		return false;
	}

	private void checkCollisions()

	{
		Point[] points = new Point[5];
		// Collision for top side

		for (int i = 0; i != 5; i++)
			points[i] = new Point(bounds.x + bounds.width * (i / 5f) + bounds.width * 0.1f, bounds.y + bounds.height);

		if (GameScreen.map.tilemap.checkCollisions(points).length > 0)
			position.y = (float) Math.ceil(position.y) - bounds.height / 2;

		// Collision for bottom side

		for (int i = 0; i != 5; i++)
			points[i] = new Point(bounds.x + bounds.width * (i / 5f) + bounds.width * 0.1f, bounds.y);

		if (GameScreen.map.tilemap.checkCollisions(points).length > 0)
			position.y = (float) Math.floor(position.y) + bounds.height / 2;

		updateBounds(); // Damit Right und Left Collision nicht mit den alten Boundwerten arbeiten (gibt Probleme beim Fliegen in Ecken)

		// Collision for left side

		for (int i = 0; i != 5; i++)
			points[i] = new Point(bounds.x, bounds.y + bounds.height * (i / 5f) + bounds.height * 0.1f);

		if (GameScreen.map.tilemap.checkCollisions(points).length > 0)
			position.x = (float) Math.floor(position.x) + bounds.width / 2;

		// Collision for right side

		for (int i = 0; i != 5; i++)
			points[i] = new Point(bounds.x + bounds.width, bounds.y + bounds.height * (i / 5f) + bounds.height * 0.1f);

		if (GameScreen.map.tilemap.checkCollisions(points).length > 0)
			position.x = (float) Math.ceil(position.x) - bounds.width / 2;

	}

	private void updateBounds()
	{
		rect.set(position.x - scaleX / 2, position.y - scaleY / 2, scaleX, scaleY);
		bounds.set(rect.x + 0.15f, rect.y + 0.15f, rect.width - 0.3f, rect.height - 0.3f);
	}

	private void updateScaleCache()
	{
		scaleX = sprite.getRegionWidth() / 16f * scale;
		scaleY = sprite.getRegionHeight() / 16f * scale;
	}

}
