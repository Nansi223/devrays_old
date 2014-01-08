package app.jaid.devrays.io;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.jaid.Point;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.Meta;
import app.jaid.devrays.debug.Log;
import app.jaid.devrays.screen.editor.data.Block;
import app.jaid.devrays.screen.editor.data.Command;
import app.jaid.devrays.screen.editor.data.Event;
import app.jaid.devrays.screen.open.MapThumbnailDrawer;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.Tile;
import app.jaid.devrays.world.logic.Timer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import static app.jaid.devrays.screen.editor.data.Function.*;

public class MapCompiler {

	public static String compile(Map map)
	{
		String validationResult = validate(map);
		if (validationResult != null)
			return validationResult;

		Array<String> warnings = new Array<String>();
		String filename = new SimpleDateFormat("yyMMdd_").format(new Date()) + map.title.toLowerCase().replace(' ', '_');

		long startTime = System.currentTimeMillis();
		FileHandle file = Devrays.file("maps/sdk/" + filename + ".devr");
		JaidWriter writer = new JaidWriter(file.write(false), 9);

		writer.writeString("DEVR.MAP");
		writer.write1Byte(map.type, map.tilemap.getWidth(), map.tilemap.getHeight());
		writer.writeString(map.title);

		// write Tiles

		for (int x = 0; x != map.tilemap.getWidth(); x++)
			for (int y = 0; y != map.tilemap.getHeight(); y++)
				writer.write1Byte(map.tilemap.tiles[x][y].type);

		// write Points

		writer.write1Byte(map.points.size);
		for (Point point : map.points)
			writer.write2Bytes((int) (point.x * 4), (int) (point.y * 4));

		// write Rects

		writer.write1Byte(map.rects.size);

		// write Polys

		writer.write1Byte(map.polygons.size);

		// write Timers

		writer.write1Byte(map.timers.size);

		for (Timer timer : map.timers)
		{
			writer.write3Bytes((int) (timer.interval * 1000f));
			writer.write1Byte(timer.steps);
		}

		// write Events / Commands

		writer.write1Byte(map.events.size);

		for (Event event : map.events)
		{
			writer.write1Byte(event.type);

			// Write Arguments of current Event
			for (int i = 0; i != event.args.length; i++)
				writeArgument(map, writer, event.args[i], Meta.sdk.events[event.type].parameters[i].type);

			writer.write1Byte(event.commands.size);

			for (Command command : event.commands)
			{
				writer.write1Byte(command.type);
				for (int i = 0; i != command.args.length; i++)
					writeArgument(map, writer, command.args[i], Meta.sdk.commands[command.type].parameters[i].type);
			}
		}

		writer.writeString(".JAID");

		writer.close();
		long compileTime = System.currentTimeMillis() - startTime;

		FileHandle htmlHandle = Devrays.file("log/compilation/" + filename + ".html");
		htmlHandle.writeString(MapCompilationReport.generate(map, warnings, file, compileTime), false);

		return warnings.size == 0 ? null : warnings.toString("\n");
	}

	public static String validate(Map map)
	{
		// Check if spawn point is set, fail if not

		boolean startEventFound = false, spawnpointFound = false;

		for (Event event : map.events)
			if (event.type == 0)
			{
				startEventFound = true;
				for (Command command : event.commands)
					if (command.type == 0)
						spawnpointFound = true;
			}

		if (!startEventFound)
			return "No Start Event found!";
		if (!spawnpointFound)
			return "No Spawn Point has been set!";

		// Check if every point has a still existing reference

		String argumentValidationResult = null;

		for (Event event : map.events)
		{

			for (int i = 0; i != event.args.length; i++)
			{
				argumentValidationResult = validateArgument(map, event.args[i]);
				if (argumentValidationResult != null)
					return "Argument #" + i + " of " + event.toString(false) + " " + argumentValidationResult;
			}

			for (Command command : event.commands)
				for (int i = 0; i != command.args.length; i++)
				{
					argumentValidationResult = validateArgument(map, command.args[i]);
					if (argumentValidationResult != null)
						return "Argument #" + i + " of " + event.toString(false) + "." + Meta.sdk.commands[command.type].name + " " + argumentValidationResult;
				}
		}

		return null;
	}

	public static void writeArgument(Map map, JaidWriter writer, Object argument, int parameterType)
	{
		if (argument == null)
			throw new IllegalArgumentException("Argument null can't be serialized.");

		// Debug all the code!
		Log.m("Argument being serialized: Type: " + parameterType + ", toString: " + String.valueOf(argument));

		switch (parameterType)
		{
			case PARAMETERTYPE_BYTE:
				writer.write1Byte((Integer) argument);
			break;
			case PARAMETERTYPE_SHORT:
				writer.write2Bytes((Integer) argument);
			break;
			case PARAMETERTYPE_SFLOAT:
				writer.write2Bytes((int) ((Float) argument * 100));
			break;
			case PARAMETERTYPE_STRING:
				writer.writeString((String) argument);
			break;
			case PARAMETERTYPE_POINT:
				writer.writeString("POINT");
				writer.write1Byte(map.points.indexOf((Point) argument, false));
			break;
			case PARAMETERTYPE_RECT:
			break;
			case PARAMETERTYPE_POLYGON:
			break;
			case PARAMETERTYPE_BLOCK:
				writer.write1Byte(((Block) argument).x, ((Block) argument).y);
			break;
			case PARAMETERTYPE_COLOR:
			break;
		}

	}

	private static String validateArgument(Map map, Object argument)
	{
		if (argument == null)
			return "is null!";

		if (argument instanceof Point && !map.points.contains((Point) argument, false))
			return "is a dead Point reference!";

		return null;
	}
}