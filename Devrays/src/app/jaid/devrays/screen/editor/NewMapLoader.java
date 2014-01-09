package app.jaid.devrays.screen.editor;

import app.jaid.Point;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.world.Tile;
import app.jaid.devrays.world.Tilemap;

public class NewMapLoader {

	public static EditorMap get()
	{
		EditorMap map = new EditorMap();

		map.title = "Enter Title";
		map.type = 0;

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

		}; // </noformat>

		map.tilemap.tiles = new Tile[ids[0].length][ids.length];

		for (int x = 0; x != map.tilemap.tiles.length; x++)
			for (int y = 0; y != map.tilemap.tiles[0].length; y++)
				map.tilemap.tiles[x][y] = new Tile(ids[y][x]);

		map.points.add(new Point(3, 3));

		return map;
	}

}
