package app.jaid.devrays.world;

import app.jaid.devrays.entity.Enemy;
import app.jaid.devrays.screen.game.GameScreen;

public class Spawner {

	public static void spawn(int type, float x, float y)
	{
		Enemy e = new Enemy(type, null);
		e.position.set(x, y);
		e.setScale(0.2f);
		GameScreen.enemies.add(e);
	}

}
