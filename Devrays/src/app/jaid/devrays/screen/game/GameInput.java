package app.jaid.devrays.screen.game;

import app.jaid.Point;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.world.effect.Explosion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class GameInput implements InputProcessor {

	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode) {
			case 51:
				Controller.movingUp = true;
			break;
			case 29:
				Controller.movingLeft = true;
			break;
			case 47:
				Controller.movingDown = true;
			break;
			case 32:
				Controller.movingRight = true;
			break;
			case 62:
				Controller.shooting = true;
			break;

			case com.badlogic.gdx.Input.Keys.F11:
				if (Gdx.graphics.isFullscreen())
					Gdx.graphics.setDisplayMode(1280, 720, false);
				else
					Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
			break;

			case com.badlogic.gdx.Input.Keys.O:
				Explosion.trigger(new Point(3, 3), Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) ? 20 : 10);
			break;

			case com.badlogic.gdx.Input.Keys.P:
				for (int i = -180; i != 180; i++)
				{
					if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
						GameScreen.weapons.get(0).color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
					GameScreen.weapons.get(0).angle = i;
					GameScreen.weapons.get(0).fire();
				}
				GameScreen.weapons.get(0).angle = -90;
			break;

			case com.badlogic.gdx.Input.Keys.ESCAPE:
				Gdx.app.exit();
			break;

			case com.badlogic.gdx.Input.Keys.B:
				GameScreen.player.blink(Color.GREEN);
			break;

			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch (keycode) {
			case 51:
				Controller.movingUp = false;
			break;
			case 29:
				Controller.movingLeft = false;
			break;
			case 47:
				Controller.movingDown = false;
			break;
			case 32:
				Controller.movingRight = false;
			break;
			case 62:
				Controller.shooting = false;
			break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if (GameScreen.joystick.bounds.contains(screenX, Devrays.screenHeight - screenY))
		{
			GameScreen.joystick.touch = pointer;
			return true;
		}

		if (GameScreen.weaponsUi.bounds.contains(screenX, Devrays.screenHeight - screenY))
		{
			GameScreen.weaponsUi.touch = pointer;
			return true;
		}

		if (new Rectangle(0, Devrays.screenHeight - 400, 400, 400).contains(screenX, Devrays.screenHeight - screenY)) // TEMP
		{
			Log.show = !Log.show;
			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (GameScreen.joystick.touch == pointer)
			GameScreen.joystick.touch = -1;
		if (GameScreen.weaponsUi.touch == pointer)
			GameScreen.weaponsUi.touch = -1;

		return false;
	}

}
