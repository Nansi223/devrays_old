package app.jaid.devrays.screen.editor.tools;

import static app.jaid.devrays.screen.editor.EditorScreen.*;
import static app.jaid.devrays.screen.editor.ui.Toolbar.TOOL_ENVIRONMENT;
import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.Graphics;
import app.jaid.devrays.InputCore;
import app.jaid.devrays.screen.editor.EditorScreen;
import app.jaid.devrays.screen.editor.TaskHandler;
import app.jaid.devrays.screen.editor.ui.EnvironmentUI;
import app.jaid.devrays.ui.Tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EnvironmentHandler {

	public static float			pointHoverAnimation;
	public static Array<Point>	selectedPoints;
	public static Point[]		selectedRect;

	public static void clearSelections()
	{
		selectedPoints.clear();
	}

	public static Rectangle getRectangleBySelectedRect(Point[] points)
	{
		return new Rectangle(selectedRect[0].x, selectedRect[0].y, selectedRect[1].x - selectedRect[0].x, selectedRect[1].y - selectedRect[0].y);
	}

	public static void longtap(Point point)
	{
		Point editorPoint = point.applyToGrid(0.25f);
		{
			if (!map.points.contains(editorPoint, false))
			{
				map.points.add(editorPoint); // Create Point
				if (InputCore.ctrl)
				{// Add to selectedPoints if CTRL is pressed during longtap
					selectedPoints.add(editorPoint);
					updateSelection();
				}
				EnvironmentUI.updateEnvironmentTabNames(); // Update Overview Card
				EnvironmentUI.updateList();
			}
		}
	}

	public static void render()
	{
		float pointSize = EditorScreen.cameraHeight / Devrays.screenHeight * 15f + 0.1f; // Calculating Point size by checking current zoom factor
		for (Point point : map.points)
			// Render points Array
			Graphics.draw(Atlas.get("point"), point.x, point.y, pointSize, pointSize, selectedPoints.contains(point, false) ? Color.YELLOW : Color.WHITE);

		if (!Devrays.onSmartphone && tool == TOOL_ENVIRONMENT)
		{
			if (InputCore.mouseMoved)
				pointHoverAnimation = 1;
			else
				pointHoverAnimation += Devrays.delta * 2;

			Point hoverPoint = Point.fromScreenPoint(Gdx.input.getX(), Devrays.screenHeight - Gdx.input.getY(), camera).applyToGrid(0.25f); // Render hover Point
			if (!selectedPoints.contains(hoverPoint, false))
				Graphics.draw(Atlas.get("point"), hoverPoint.x, hoverPoint.y, pointSize, pointSize, new Color(0, 0.5f, 1, (float) Math.abs(Math.sin(pointHoverAnimation))));

			if (map.points.contains(hoverPoint, false))
				Tooltip.set("Point #" + map.points.indexOf(hoverPoint, false) + " " + hoverPoint.toString()); // Render hover Point Tooltip
		}
	}

	public static void renderPolygons()
	{
		if (selectedPoints.size > 2)
			Graphics.drawPolygon((Point[]) selectedPoints.toArray(Point.class), Color.RED);
	}

	public static void renderShapes()
	{
		if (selectedRect != null) // Render selected Points Rect
			Graphics.drawRect(getRectangleBySelectedRect(selectedRect), new Color(0.9f, 1, 0.5f, 0.5f));

		for (Point[] rect : map.rects)
			// Render rects Array
			Graphics.drawRect(getRectangleBySelectedRect(rect), Color.RED);
	}

	public static void tap(Point point)
	{
		Point editorPoint = point.applyToGrid(0.25f);

		if (map.points.contains(editorPoint, false))
		{
			if (!selectedPoints.contains(editorPoint, false))
			{
				if (selectedPoints.size > 0 && !InputCore.ctrl)
					clearSelections();
				selectedPoints.add(editorPoint); // select if not already selected
			}
			else
				selectedPoints.removeValue(editorPoint, false); // deselected if already selected
		}
		else
			selectedPoints.clear();

		updateSelection();
		EnvironmentUI.updateList();
	}

	public static void updateSelection()
	{
		if (TaskHandler.taskType != 0 && TaskHandler.taskObjectType == TaskHandler.OBJECTTYPE_POINT && selectedPoints.size == 1) // If object has been selected for a task, finish task
		{
			EditorScreen.endTask(selectedPoints.get(0));
			return;
		}

		if (selectedPoints.size == 2)
		{
			Array<Point> selectedRectPoints = new Array<Point>(selectedPoints);
			if (selectedPoints.get(0).y > selectedPoints.get(1).y)
				selectedRectPoints.reverse();
			selectedRect = selectedRectPoints.toArray(Point.class);
		}
		else
			selectedRect = null;
	}
}
