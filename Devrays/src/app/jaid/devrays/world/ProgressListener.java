package app.jaid.devrays.world;

import app.jaid.devrays.debug.Log;
import app.jaid.devrays.screen.game.GameScreen;

public class ProgressListener {

	static int	progress;

	public static void listen()
	{
		Log.p("Progress", progress);

		if (GameScreen.enemies.size == 1000)
		{
			switch (++progress) {
				case 1:
					Spawner.spawn(0, 460, 500);
					Spawner.spawn(2, 915, 363);
					Spawner.spawn(2, 735, 63);
					Spawner.spawn(0, 990, 90);
					break;
				case 2:
					Spawner.spawn(1, 1663, 326);
					Spawner.spawn(1, 2034, 342);
					Spawner.spawn(2, 1830, 63);
					Spawner.spawn(2, 1830, 635);
					break;
				case 3:
					Spawner.spawn(1, 2465, 604);
					Spawner.spawn(3, 2465, 350);
					break;
				case 4:
					Spawner.spawn(1, 2200, 1000);
					Spawner.spawn(1, 2200, 1300);
					Spawner.spawn(1, 2358, 1151);
					Spawner.spawn(3, 2142, 889);
					Spawner.spawn(3, 2610, 1151);
					break;
				case 5:
					Spawner.spawn(3, 1678, 810);
					Spawner.spawn(2, 1400, 1333);
					Spawner.spawn(2, 1720, 1333);
					Spawner.spawn(1, 1853, 1060);
					Spawner.spawn(1, 1653, 1060);
					Spawner.spawn(1, 1453, 1060);
					break;
				case 6:
					Spawner.spawn(3, 1080, 1413);
					break;
				case 7:
					Spawner.spawn(4, 166, 820);
					break;
			}

			if (progress < 8) GameScreen.player.hp += 25;
		}

	}
}
