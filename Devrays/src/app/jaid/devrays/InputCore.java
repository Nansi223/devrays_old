package app.jaid.devrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class InputCore {

	public static float	angleX, angleY;
	public static boolean	ctrl, mouseMoved;

	static void update()
	{
		angleY = Gdx.input.getRotation() == 180 ? Math.max(Math.min(Gdx.input.getRoll(), 0), -90) / 45 + 1 : Math.max(Math.min(Gdx.input.getRoll(), 0), -90) / 45 + 1;
		ctrl = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT);
	}

}
