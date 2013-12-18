package app.jaid.devrays.world;

import java.util.HashMap;

import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.screen.editor.EditorScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;

public class Tilemap {

	public static Point getTileMid(Point position)
	{
		return new Point((float) Math.floor(position.x) + 0.5f, (float) Math.floor(position.y) + 0.5f);
	}

	public Tile[][]					tiles;

	private HashMap<Point, Boolean>	checkedPoints	= new HashMap<Point, Boolean>();

	public Tilemap()
	{
		byte[][] ids = new byte[][] {// <noformat>

				{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3},
				{3,0,0,0,0,2,2,2,2,0,0,0,0,0,0,3,0,0,3,2,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,0,3,3,3,3,0,0,0,0,0,0,3,0,0,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,0,6,5,5,5,3,3,3,3,0,0,3,0,0,3,2,3,0,0,0,0,1,1,1,1,1,0,0,1,1,1,1,1,0,0,0,4,0,4,0,4,0,3},
				{3,0,0,0,0,0,6,5,5,2,0,0,0,0,0,3,2,2,3,3,3,0,0,0,0,1,0,0,0,0,0,0,2,2,2,2,1,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,0,0,0,6,5,4,4,4,4,4,4,3,0,0,2,2,2,0,0,0,0,1,0,0,0,4,4,4,4,4,4,4,1,0,0,0,4,0,4,0,4,0,3},
				{3,0,0,0,0,0,0,2,6,4,1,2,3,4,4,0,0,0,2,2,2,0,0,0,0,1,0,0,0,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,0,0,0,2,2,4,4,4,4,4,4,0,0,0,3,3,3,0,0,0,0,1,0,0,0,0,0,0,2,2,2,2,1,0,0,0,4,0,4,0,4,0,3},
				{3,0,0,0,3,0,0,2,2,4,0,0,0,0,4,0,0,0,3,2,3,0,0,0,0,1,1,1,1,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,3,3,0,2,2,4,0,0,0,0,4,4,4,4,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,3,3,3,2,2,2,0,0,0,0,0,0,4,4,3,2,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,3,0,3},
				{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,2,3,0,0,0,3,2,3,3},
				{3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,3,0,0,0,0,0,4,0,0,0,0,1,0,0,4,4,2,2,2,3,0,0,0,0,0,3,4,3},
				{3,0,0,0,4,4,0,4,4,0,4,4,0,0,0,0,0,3,3,3,0,0,0,0,0,4,0,0,0,0,1,0,0,3,4,0,0,0,3,3,3,0,0,0,0,4,3},
				{3,0,0,0,4,4,0,4,4,0,4,4,0,0,0,0,0,3,2,3,0,0,0,0,0,4,0,0,0,0,1,0,0,3,4,4,0,0,0,0,0,0,0,0,0,4,3},
				{3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,0,0,0,0,0,4,0,0,0,0,0,0,0,4,5,4,4,4,4,4,4,4,0,0,0,4,3},
				{3,0,0,0,4,4,0,4,4,0,4,4,0,0,0,0,0,3,2,3,2,2,2,2,2,4,2,2,2,1,2,4,4,4,0,0,0,0,0,0,0,4,0,0,0,4,3},
				{3,0,0,0,4,4,0,4,4,0,4,4,0,0,0,0,0,3,3,3,0,0,0,0,0,4,0,0,0,2,2,4,1,2,2,2,2,0,3,3,3,4,0,0,0,4,3},
				{3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,3,0,0,0,0,0,4,0,0,0,2,2,4,1,2,2,2,2,3,2,2,2,4,0,0,0,4,3},
				{3,0,0,0,4,4,0,4,4,0,4,4,0,0,0,0,0,3,3,3,2,2,2,2,2,4,2,2,2,1,2,4,1,3,0,0,0,3,2,4,4,4,0,0,0,4,3},
				{3,0,0,0,4,4,0,4,4,0,4,4,0,0,0,0,0,3,2,3,0,0,0,0,0,4,0,0,0,0,0,4,0,3,0,0,0,3,2,2,2,4,4,4,4,4,3},
				{3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,0,0,0,0,0,4,4,4,4,4,4,4,0,3,0,0,0,0,3,3,3,0,0,0,0,0,3},
				{3,0,0,0,0,0,0,6,5,0,0,0,0,2,2,2,2,3,2,3,0,0,0,0,4,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,0,0,6,5,5,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,2,2,2,2,2,2,2,2,3,0,0,0,0,0,0,0,0,0,0,0,0,3},
				{3,0,0,0,0,6,5,5,5,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,0,0,0,0,0,0,0,0,0,0,0,0,3},
				{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3},

		}; 

		if (EditorScreen.map != null)
			tiles = EditorScreen.map.tilemap.tiles;
		else
		{
			tiles = new Tile[ids[0].length][ids.length];

			for (int x = 0; x != tiles.length; x++)
				for (int y = 0; y != tiles[0].length; y++)
					tiles[x][y] = new Tile(ids[y][x]);
		}
	}

	public Tilemap(String name)
	{
		// FileHandle file = Gdx.files.external(name);
		// file.read();
	}

	public Tile checkCollision(Point point)
	{
		Tile tile = getTileAt(point.x, point.y);
		if (tile.isSolid())
			return tile;
		if (tile.type == 6)
			if (Intersector.isPointInTriangle(point.x, point.y, (int) point.x, (int) point.y, (int) point.x + 1, (int) point.y + 1, (int) point.x + 1, (int) point.y))
				return tile;
		return null;
	}

	public Tile[] checkCollisions(Point[] points)
	{
		Array<Tile> tiles = new Array<Tile>();

		for (Point point : points)
			if (checkCollision(point) != null)
			{
				if (!tiles.contains(checkCollision(point), false))
					tiles.add(checkCollision(point));
				if (Log.show)
					checkedPoints.put(point, true);
			}
			else if (Log.show)
				checkedPoints.put(point, false);

		return tiles.toArray(Tile.class);
	}

	public int getHeight()
	{
		return tiles[0].length;
	}

	public Pixmap getMinimap()
	{
		Pixmap minimap = new Pixmap(256, 256, Format.RGB888); // TEMP
		// Pixmap minimap = new Pixmap(MathUtils.nextPowerOfTwo(getWidth()), MathUtils.nextPowerOfTwo(getHeight()), Format.RGB888); // Power Of Two sichern, zur Kompatibilität mit OpenGL ES 1
		Tile tile;
		for (int x = 0; x != tiles.length; x++)
			for (int y = 0; y != tiles[0].length; y++)
			{
				tile = tiles[x][y];
				if (tile != null && tile.type != 0)
					minimap.drawPixel(x, 256 - y, Color.GRAY.toIntBits());
			}
		return minimap;
	}

	public Tile getTile(int x, int y)
	{
		int finalX = Math.min(x, getWidth() - 1);
		int finalY = Math.min(y, getHeight() - 1);

		if (x != finalX || y != finalY)
			Log.m("getTile(" + x + ", " + y + ") out of bounds! Changed to getTile(" + finalX + ", " + finalY + ").");

		return tiles[finalX][finalY];
	}

	public Tile getTileAt(float x, float y)
	{
		return getTile((int) x, (int) y);
	}

	public Tile getTileAt(Point position)
	{
		return getTileAt(position.x, position.y);
	}

	public int getWidth()
	{
		return tiles.length;
	}

	public void render(Camera camera)
	{
		Devrays.batch.setColor(Color.WHITE);

		Tile tile;
		int viewX = (int) Math.max(0, camera.position.x - camera.viewportWidth / 2);
		int viewWidth = (int) Math.min(getWidth(), camera.position.x + camera.viewportWidth - camera.viewportWidth / 2 + 1);
		int viewY = (int) Math.max(0, camera.position.y - camera.viewportHeight / 2);
		int viewHeight = (int) Math.min(getHeight(), camera.position.y + camera.viewportHeight - camera.viewportHeight / 2 + 1);

		for (int x = viewX; x != viewWidth; x++)
			for (int y = viewY; y != viewHeight; y++)
			{
				tile = tiles[x][y];

				if (tile.type != 0)
					Devrays.batch.draw(Atlas.get("t/" + tile.type), x, y, 1, 1);
			}

		if (Log.show)
		{
			for (Point point : checkedPoints.keySet())
			{
				Devrays.batch.setColor(checkedPoints.get(point) ? Color.RED : Color.ORANGE);
				Devrays.batch.draw(Atlas.get("star"), point.x - 0.1f, point.y - 0.1f, 0.2f, 0.2f);
			}

			Devrays.batch.setColor(Color.WHITE);
			checkedPoints.clear();
		}
	}

}
