package app.jaid.devrays.io;

import java.io.InputStream;

import app.jaid.devrays.debug.Log;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.Tile;
import app.jaid.devrays.world.Tilemap;

public class MapLoader {

	public static Map read(InputStream stream)
	{
		Map map = new Map();

		JaidReader reader = new JaidReader(stream);

		// Read meta information (type, title, size)

		String devrType = reader.readString();
		map.type = reader.read1Byte();
		int width = reader.read1Byte();
		int height = reader.read1Byte();
		map.title = reader.readString();

		if (!devrType.equals("DEVR.MAP"))
			Log.m("BROKEN HEADER: \"" + devrType + "\"");

		Log.m("Loading " + devrType + " \"" + map.title + "\" (Type: " + map.type + ", Dimensions: " + width + "x" + height + ").");

		map.tilemap = new Tilemap();
		map.tilemap.tiles = new Tile[width][height];

		// Read tiles

		for (int x = 0; x != width; x++)
			for (int y = 0; y != height; y++)
				map.tilemap.tiles[x][y] = new Tile(reader.read1Byte());

		// Read Event Data

		return map;
	}
}
