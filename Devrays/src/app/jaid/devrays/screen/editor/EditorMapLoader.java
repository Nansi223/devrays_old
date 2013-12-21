package app.jaid.devrays.screen.editor;

import app.jaid.devrays.world.Map;

public class EditorMapLoader {

	public static EditorMap load(Map map)
	{
		EditorMap editorMap = new EditorMap();

		editorMap.title = map.title;
		editorMap.type = map.type;
		editorMap.tilemap = map.tilemap;

		return editorMap;
	}

}
