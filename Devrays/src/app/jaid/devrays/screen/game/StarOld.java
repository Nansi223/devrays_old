package app.jaid.devrays.screen.game;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class StarOld {

	public static int	maxRadius;

	public Rectangle	bounds;
	public Color		color;
	float				height, radius, lifetime, distance, scale;

	public StarOld()
	{
		bounds = new Rectangle();
		lifetime = MathUtils.random(360f);
		height = MathUtils.random(Devrays.screenHeight);

		radius = MathUtils.random(50, maxRadius);
		color = new Color(1 - (radius - 100) / (maxRadius - 100), 0, 1, 1);
	}

	public void update(float delta)
	{
		float factorSin = (float) Math.sin(lifetime += delta);
		float factorCos = (float) Math.cos(lifetime);

		scale = 36 + factorSin * 28 * radius / maxRadius;

		bounds.x = maxRadius - scale / 2 + factorCos * radius;
		// bounds.y = height - scale / 2 - factorCos * 300 * Devrays.angleY;
		bounds.height = scale;
		bounds.width = scale;
	}
}
