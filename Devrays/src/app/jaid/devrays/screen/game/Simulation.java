package app.jaid.devrays.screen.game;

import app.jaid.devrays.debug.Schedule;
import app.jaid.devrays.entity.Entity;
import app.jaid.devrays.world.ProgressListener;

import com.badlogic.gdx.utils.Array;

public class Simulation {

	@SuppressWarnings("unchecked")
	public static void update()
	{
		ProgressListener.listen();

		GameScreen.joystick.update();
		GameScreen.weaponsUi.update();

		if (GameScreen.joystick.touch != -1)
			GameScreen.player.push(GameScreen.joystick.direction * 45, 3);

		if (Controller.direction() != -10)
			GameScreen.player.push(Controller.direction() * 45, 3);

		if ((GameScreen.weaponsUi.touch != -1 || Controller.shooting) && Schedule.request("shoot"))
			GameScreen.weapons.get(0).fire();

		updateEntities(GameScreen.bullets, GameScreen.enemies);
		GameScreen.player.move();
	}

	@SuppressWarnings("unchecked")
	static void updateEntities(Array<? extends Entity>... entities)
	{
		for (Array<? extends Entity> group : entities)
			for (Entity entity : group)
				if (!entity.move())
					((Array<Entity>) group).removeValue(entity, true);
	}
}
