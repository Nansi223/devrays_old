package app.jaid.devrays.screen.editor.tools;

import static app.jaid.devrays.screen.editor.EditorScreen.*;
import static app.jaid.devrays.screen.editor.ui.Toolbar.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

import app.jaid.GridTools;
import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.InputCore;
import app.jaid.devrays.screen.editor.EditorScreen;
import app.jaid.devrays.ui.Tooltip;

public class DrawHandler {

	public static byte		drawTile;
	private static float	blockHoverAnimation;

	public static void longtap(Point point)
	{

	}

	public static void render()
	{
		if (!Devrays.onSmartphone)
		{
			if (InputCore.mouseMoved)
				blockHoverAnimation = 1;
			else
				blockHoverAnimation += Devrays.delta * 2;
			Point point = Point.fromScreenPoint(Gdx.input.getX(), Devrays.screenHeight - Gdx.input.getY(), EditorScreen.camera);

			if (subtool == TOOL_DRAW_PENCIL)
			{
				Devrays.batch.setColor(1, 1, 1, (float) Math.abs(Math.sin(blockHoverAnimation)));
				Devrays.batch.draw(Atlas.get("t/" + drawTile), (int) point.x, (int) point.y, 1, 1);
			}

			Devrays.batch.setColor(Color.WHITE);
			if (subtool == TOOL_DRAW_PENCIL && map.tilemap.getTileAt(point).type != drawTile || subtool == TOOL_DRAW_RUBBER && map.tilemap.getTileAt(point).type != 0)
				Devrays.batch.draw(Atlas.get("t/b"), (int) point.x, (int) point.y, 1, 1);
		}
	}

	public static void tap(Point point)
	{
		if (subtool == TOOL_DRAW_FILL)
			map.fill((int) point.x, (int) point.y, drawTile);
	}

	public static void touchDown(Point point)
	{
		if (subtool != TOOL_DRAW_FILL)
			map.tilemap.getTileAt(point).type = subtool == TOOL_DRAW_PENCIL ? drawTile : (byte) 0;
	}

	public static void touchDragged(Point oldPoint, Point newPoint)
	{
		for (int[] block : GridTools.line((int) oldPoint.x, (int) oldPoint.y, (int) newPoint.x, (int) newPoint.y))
			map.tilemap.getTile(block[0], block[1]).type = subtool == TOOL_DRAW_RUBBER ? (byte) 0 : drawTile;
	}

}
