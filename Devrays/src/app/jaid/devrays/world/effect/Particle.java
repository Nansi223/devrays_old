package app.jaid.devrays.world.effect;

import static app.jaid.devrays.Devrays.batch;
import app.jaid.Jtil;
import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.NumberUtils;

public class Particle {

	private static Array<Particle>	particles	= new Array<Particle>();
	private static float			size;
	static int						quantity;

	public static void render()
	{
		int srcBlend = batch.getBlendSrcFunc();
		int dstBlend = batch.getBlendDstFunc();
		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		for (Particle particle : particles)
		{
			particle.position.move(particle.angle, particle.lifetime * particle.power * Devrays.delta);
			particle.lifetime -= Devrays.delta;
			particle.color.a = Math.min(particle.lifetime * 2, 1);
			particle.scale = size * particle.scaleFactor;

			if (particle.lifetime > 0)
				Graphics.draw(Atlas.get("pixel"), particle.position.x, particle.position.y, particle.scale, particle.scale, particle.color);
			else
				particles.removeValue(particle, true);
		}

		batch.setBlendFunction(srcBlend, dstBlend);
		batch.setColor(Color.WHITE);
	}

	public static void setQuality(int quality)
	{
		switch (quality) {
			case 0: // OFF
				quantity = 0;
			break;
			case 1: // SMARTPHONE
				quantity = 1;
				size = 0.3f;
			break;
			case 2: // NORMAL
				quantity = 2;
				size = 0.25f;
			break;
			case 3: // FANCY
				quantity = 5;
				size = 0.2f;
			break;
			case 4: // VISUAL SHAPES
				quantity = 1000;
				size = 0.15f;
			break;
		}
	}

	public static void spawnEffect(int count, float scale, Point source, float angle, float pressure, float power, Object colorSource)
	{
		int amount = MathUtils.random(count * quantity, count * quantity * 2);

		Color color = colorSource instanceof Color ? (Color) colorSource : colorSource instanceof Float ? new Color(NumberUtils.floatToIntColor((Float) colorSource)) : null;

		for (int i = 0; i != amount; i++)
		{
			float direction = Jtil.randomAngle();
			float weite = power * (float) Math.pow(180 - Jtil.angleDifference(angle, direction), 1.5) * pressure / 10000;

			if (colorSource instanceof TextureRegion)
				color = PixmapColor.retrieve((TextureRegion) colorSource); // Sind Casts in Iterationen groﬂe Performancekiller?

			new Particle(MathUtils.random(scale * 0.7f, scale * 1.3f), source.x, source.y, direction, MathUtils.random(1, weite), color);
		}
	}

	private Color	color;
	private Point	position;

	private float	scale, power, scaleFactor, angle, lifetime = 1;

	Particle(float scale, float x, float y, float angle, float power, Color color)
	{
		position = new Point(x, y);
		scaleFactor = scale;
		this.color = color;
		this.angle = angle;
		this.power = power;

		particles.add(this);
	}

}
