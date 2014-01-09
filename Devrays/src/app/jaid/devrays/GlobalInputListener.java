package app.jaid.devrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class GlobalInputListener implements InputProcessor {

	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode)
		{
			case Keys.F12:
				if (Gdx.graphics.isFullscreen())
					Gdx.graphics.setDisplayMode(1280, 720, false);
				else
					Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
				return true;

			case Keys.F10:
				Devrays.debug = !Devrays.debug;
				return true;
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		InputCore.mouseMoved = true;
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
		return false;
	}

}
