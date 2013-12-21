package app.jaid.devrays.screen.open;

import java.io.FileInputStream;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.io.MapLoader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class MapEntriesWidget extends ScrollPane {

	public static Table get(boolean showExisting)
	{
		Table table = new Table(Devrays.skin);
		FileHandle trash = Devrays.file("sdktrash.txt");

		Array<String> trashFiles = new Array<String>();
		if (trash.exists())
			trashFiles.addAll(trash.readString().split(" "));

		Array<FileHandle> mapfilesBuilder = new Array<FileHandle>(FileHandle.class);

		if (showExisting)
			for (FileHandle projectFile : Devrays.file("maps/sdk").list("devr"))
			{
				if (!trashFiles.contains(projectFile.nameWithoutExtension(), false))
					mapfilesBuilder.add(projectFile);
			}
		else
		{
			if (!trash.exists())
				return null;

			for (String deletedFilename : trash.readString().split(" "))
				if (deletedFilename.length() > 1)
					mapfilesBuilder.add(Devrays.file("maps/sdk/" + deletedFilename + ".devr"));
		}

		FileHandle[] mapfiles = mapfilesBuilder.toArray();

		if (mapfiles.length == 0)
			return null;

		Log.m(mapfiles.length + " map files shown in map opening screen.");
		for (FileHandle mapfile : mapfiles)
			try
		{
				table.add(new MapEntry(MapLoader.read(new FileInputStream(mapfile.file())), mapfile, showExisting, trash)).row().pad(20);
		} catch (Exception e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}

		return table;
	}

	public MapEntriesWidget(boolean showExisting)
	{
		super(get(showExisting), Devrays.skin);
	}

}
