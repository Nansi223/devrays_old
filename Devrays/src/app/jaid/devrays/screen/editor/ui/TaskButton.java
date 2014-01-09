package app.jaid.devrays.screen.editor.ui;

import static app.jaid.devrays.screen.editor.TaskHandler.*;
import app.jaid.Point;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.screen.editor.EditorScreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class TaskButton extends TextButton {

	private final int	tool, subtool;

	public TaskButton(final int type, Object object, final int... arguments)
	{
		super("", Devrays.skin);
		Log.m("New TaskButton added!");
		tool = EditorScreen.tool;
		subtool = EditorScreen.subtool;
		updateText(object);
		addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				startTask(TaskButton.this, type, tool, subtool, arguments);
			}
		});
	}

	public void updateText(Object object)
	{
		switch (taskObjectType)
		{
			case OBJECTTYPE_POINT:
				if (object == null)
					setText("(Set Point)");
				else
					setText("Point #" + EditorScreen.map.points.indexOf((Point) object, false) + " " + ((Point) object).toString());
				break;
			case OBJECTTYPE_RECT:
				if (object == null)
					setText("(Set Rect)");
				else
					;
				break;
			case OBJECTTYPE_POLYGON:
				if (object == null)
					setText("(Set Polygon)");
				else
					;
				break;
		}
	}

}
