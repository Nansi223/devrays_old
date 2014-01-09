package app.jaid.devrays.entity;

import app.jaid.devrays.screen.game.GameScreen;
import app.jaid.devrays.ui.Toast;
import app.jaid.devrays.world.Tile;
import app.jaid.devrays.world.Tilemap;
import app.jaid.devrays.world.effect.Explosion;
import app.jaid.devrays.world.effect.Particle;

import com.badlogic.gdx.graphics.Color;

public class Bullet extends Entity {

	public boolean	harmless;
	public float	lifetime;
	public int		type;

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public boolean update()
	{
		Tile intersect = GameScreen.map.tilemap.checkCollision(position);

		if (intersect != null)
		{
			new Toast(String.valueOf((int) speed), GameScreen.screenPointOf(position), angle, Color.WHITE.cpy());
			Particle.spawnEffect(2, 1, position, angle, 15, speed / 2, color);
			intersect.explode(position, Tilemap.getTileMid(position));

			if (type == 4)
				Explosion.trigger(position, 3);
			return false;
		}

		if (!harmless && GameScreen.player.bounds.contains(position.x, position.y)) // TEMP
		{
			Particle.spawnEffect(40, 1, position, angle - 100, angle + 100, 20, color);
			GameScreen.player.push(angle, 300);
			GameScreen.player.hp -= 10;
			return false;
		}

		if (harmless)
			for (Enemy enemy : GameScreen.enemies)
				if (enemy.bounds.contains(position.x, position.y))
				{
					enemy.damage(10);
					Particle.spawnEffect(5, 1, position, angle - 75, angle + 75, 10, color);
					return false;
				}

		return true;
	}
}
