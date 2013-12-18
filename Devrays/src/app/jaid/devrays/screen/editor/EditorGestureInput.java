package app.jaid.devrays.screen.editor;

import static app.jaid.devrays.screen.editor.EditorScreen.tool;
import static app.jaid.devrays.screen.editor.ui.Toolbar.TOOL_OVERVIEW;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class EditorGestureInput implements GestureListener {

	@Override
	public boolean fling(float velocityX, float velocityY, int button)
	{
		Log.m("fling: x: " + velocityX + ", y: " + velocityY);
		return false;
	}

	@Override
	public boolean longPress(float x, float y)
	{
		EditorScreen.map.longtap((int) x, (int) y);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		if (tool == TOOL_OVERVIEW)
			EditorScreen.setCamera(EditorScreen.camera.position.x - deltaX * (EditorScreen.cameraWidth / Devrays.screenWidth), EditorScreen.camera.position.y + deltaY * (EditorScreen.cameraHeight / Devrays.screenHeight));
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		if (Devrays.onSmartphone || button == Input.Buttons.LEFT)
			EditorScreen.map.tap((int) x, (int) y);
		else
			EditorScreen.map.longtap((int) x, (int) y);
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance)
	{
		return false;
	}

}
