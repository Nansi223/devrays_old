package app.jaid.devrays.world.effect;

import app.jaid.Jtil;
import app.jaid.Point;
import app.jaid.devrays.entity.Entity;
import app.jaid.devrays.screen.game.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

public class Explosion {

	public static void affectEntity(Entity entity, Point position, float pressure)
	{
		if (new Circle(position.x, position.y, pressure / 3).contains(entity.position.x, entity.position.y))
		{
			float angle = entity.position.angleTo(position);

			entity.push(angle, pressure - entity.position.distanceTo(position));
			Particle.spawnEffect((int) entity.speed, 1, entity.position, angle, 300, entity.speed * 0.025f, entity.sprite);
		}
	}

	public static void trigger(Point position, float pressure) // Brauche Namen für p, der Power bedeutet aber nicht power ist
	{
		GameScreen.tilt(pressure * 5);
		Gdx.input.vibrate((int) (pressure * 20));

		for (int i = 0; i < Math.pow(pressure, 2) * Particle.quantity * 0.5f; i++)
		{
			float power = MathUtils.random();
			new Particle(pressure, position.x, position.y, Jtil.randomAngle(), power * pressure, Color.valueOf("FFE830").lerp(Color.valueOf("730000"), power));
		}

		affectEntity(GameScreen.player, position, pressure);

		for (Point ownposition : new Point[] {new Point(position.x + 1, position.y), new Point(position.x - 1, position.y), new Point(position.x, position.y + 1), new Point(position.x, position.y - 1)})
			GameScreen.map.tilemap.getTileAt(ownposition).explode(position, ownposition);
	}
}
