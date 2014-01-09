package app.jaid.devrays.screen.editor;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class EditorInput implements InputProcessor {

	int	editorPointer	= -1, editorPointerLastX, editorPointerLastY;

	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode)
		{
			case Keys.F2:
				Devrays.startGame();
				break;

			case Keys.UP:
				EditorScreen.map.setSize(EditorScreen.map.tilemap.getWidth(), EditorScreen.map.tilemap.getHeight() + 1);
				break;

			case Keys.DOWN:
				EditorScreen.map.setSize(EditorScreen.map.tilemap.getWidth(), EditorScreen.map.tilemap.getHeight() - 1);
				break;

			case Keys.RIGHT:
				EditorScreen.map.setSize(EditorScreen.map.tilemap.getWidth() + 1, EditorScreen.map.tilemap.getHeight());
				break;

			case Keys.LEFT:
				EditorScreen.map.setSize(EditorScreen.map.tilemap.getWidth() - 1, EditorScreen.map.tilemap.getHeight());
				break;

			case Keys.ESCAPE:
				if (TaskHandler.taskType != 0)
					EditorScreen.endTask(null);
				break;
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		// if (character == 'c')
		// MapCompiler.compile(EditorScreen.map.tilemap, "Editormap", EditorScreen.map.points.toArray());

		if (character == 'x')
			EditorScreen.map.setSize(3, 3);

		if (character == 'm')
			EditorScreen.map.setSize(300, 300);

		if (character == 'r')
			EditorScreen.map.setSize(EditorScreen.map.tilemap.getWidth() + MathUtils.random(-3, 3), EditorScreen.map.tilemap.getWidth() + MathUtils.random(-3, 3));

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
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		EditorScreen.setZoom(EditorScreen.cameraHeight + amount);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if (editorPointer == -1)
		{
			editorPointer = pointer;
			EditorScreen.map.touchDown(screenX, screenY);
			editorPointerLastX = screenX;
			editorPointerLastY = screenY;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if (pointer == editorPointer)
		{
			EditorScreen.map.touchDragged(editorPointerLastX, editorPointerLastY, screenX, screenY);
			editorPointerLastX = screenX;
			editorPointerLastY = screenY;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (pointer == editorPointer)
			editorPointer = -1;
		return false;
	}
}
