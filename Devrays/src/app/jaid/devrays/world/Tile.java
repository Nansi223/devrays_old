package app.jaid.devrays.world;

import java.util.Arrays;

import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.world.effect.Explosion;
import app.jaid.devrays.world.effect.Particle;

public class Tile {

	public final static byte[]	explosives	= {2, 4};
	public final static byte[]	solids		= {1, 2, 3, 4, 5};
	public byte					type;

	public Tile(int type)
	{
		this.type = (byte) type;
	}

	public boolean explode(Point sourcePosition, Point ownposition) // Vielleicht eine Lösung finden, wo ein Tile seine Position selbst ermittelt
	{
		if (isExplosive())
		{
			float angle = ownposition.angleTo(sourcePosition);

			if (type == 4)
			{
				type = 0;
				Explosion.trigger(ownposition, 5);
				return false;
			}

			Particle.spawnEffect(8, 2, ownposition, angle, 2, 20, Atlas.get("t/" + type));
			type = 0;
		}

		return false; // Rückgabewert sagt aus, ob der Tile nach der Explosion weiterlebt
	}

	public boolean isExplosive()
	{
		return Arrays.binarySearch(explosives, type) > -1;
	}

	public boolean isSolid()
	{
		return Arrays.binarySearch(solids, type) > -1;
	}
}
