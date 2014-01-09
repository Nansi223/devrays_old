package app.jaid.devrays.screen.menu;

import app.jaid.Jtil;
import app.jaid.devrays.Devrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

public class Star {

	public static final float	FADE_IN_TIME	= 5;
	public static int			maxRadius;
	public static final Pixmap	rainbow			= new Pixmap(Gdx.files.internal("atlas/rainbow.png"));
	public static float			rainbowPoint;
	public Color				color;
	float						height, radius, angle, fadeIn;

	public Star()
	{
		fadeIn = FADE_IN_TIME;
		angle = MathUtils.random(1337f);
		height = MathUtils.random(Devrays.screenHeight);
		radius = MathUtils.random(50, maxRadius);
		color = new Color();
	}

	public void render(boolean nearest)
	{
		angle += Devrays.delta;
		Color.rgba8888ToColor(color, rainbow.getPixel((int) (1 - (radius - 50) / (maxRadius - 50) * 200f + rainbowPoint) % 1024, 0));
		color.a = (FADE_IN_TIME - fadeIn) / FADE_IN_TIME * 0.2f;
		fadeIn = Jtil.normalize(fadeIn, Devrays.delta);

		float factorSin = (float) Math.sin(angle);
		float factorCos = (float) Math.cos(angle);

		float distance = radius * factorSin + maxRadius;
		// float scaleFactor = radius;
		// float scale = 64 * scaleFactor * distance;
		float scale = 36 + factorSin * 28 * radius / maxRadius;

		float x = maxRadius + distance * 0.0013f * factorCos * radius;
		float y = height;

		if (!nearest || Math.abs(factorSin) == factorSin && radius > maxRadius / 3)
			Devrays.uibatch.setColor(color);
		// Devrays.uibatch.draw(Atlas.get("pixel"), x, y, scale, scale);
	}

}
