package app.jaid.devrays.io;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.jaid.Point;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.Meta;
import app.jaid.devrays.screen.editor.data.Command;
import app.jaid.devrays.screen.editor.data.Event;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.Tile;
import app.jaid.devrays.world.logic.Timer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class MapCompiler {

	public static Object compile(Map map, String code)
	{
		String validationResult = validate(map);
		if (validationResult != null)
			return validationResult;

		String[] report = new String[2];
		Array<String> warnings = new Array<String>();
		String filename = new SimpleDateFormat("yyMMdd_").format(new Date()) + map.title.toLowerCase().replace(' ', '_');

		long startTime = System.currentTimeMillis();
		FileHandle file = Devrays.file("maps/sdk/" + filename + ".devr");
		JaidWriter writer = new JaidWriter(file.write(false), 9);

		writer.writeString("DEVR.MAP");
		writer.write1Byte(map.type); // MAP TYPE
		writer.write1Byte(map.tilemap.getWidth(), map.tilemap.getHeight());
		writer.writeString(map.title);

		for (int x = 0; x != map.tilemap.getWidth(); x++)
			for (int y = 0; y != map.tilemap.getHeight(); y++)
				writer.write1Byte(map.tilemap.tiles[x][y].type);

		writer.write1Byte(map.points.size);
		for (Point point : map.points)
			writer.write2Bytes((int) (point.x * 4), (int) (point.y * 4));

		writer.write1Byte(map.rects.size);

		// write Rects

		writer.write1Byte(map.polygons.size);

		// write Polys

		writer.write1Byte(map.timers.size);

		for (Timer timer : map.timers)
		{
			writer.write3Bytes((int) (timer.interval * 1000f));
			writer.write1Byte(timer.steps);
		}

		writer.close();
		long compileTime = System.currentTimeMillis() - startTime;

		// HTML Report

		// Klassen:
		// t (Stat Table) - Alle Tabellen außer Maptabelle
		// h (Head Td) - Weiß auf grau Schrift, Tabellenüberschrift
		// p (Property Cell) - Allgemeine Zellenklasse für Stat Tables
		// s (Scala Cell) - Zelle für die Skalabeschriftung in der Maptabelle
		// e (Empty Block) - Block Type 0
		// m (Mutable Block) - Block, der während des Spielens zu Block Type 0 werden könnte
		// b (Static Block) - Block, der niemals durchgangen werden kann

		StringBuilder html = new StringBuilder();
		html.append("<html><head><style type='text/css'>body{font-family:\"Lucida Console\",\"Lucida Sans Typewriter\"}table{margin-bottom:10px;border-color:#555;border-collapse:collapse}");
		html.append("td{color:white;width:20px;height:20px;text-align:center;vertical-align:middle}.b{min-width:20px;background-color:#666;color:#FFF;font-size:10px}");
		html.append(".e{min-width:20px;background-color:#000;color:#FFF;font-size:10px}.p{color:#666;padding:5px}");
		html.append(".s{color:#666;font-size:10px}.h{background-color:#999;color:#FFF;padding:5px}.t{width:500px}");
		html.append("</style></head><body><table style='margin:0'><tr><td style='vertical-align:top'>");

		// html.append("<img src='data:image/png;base64," + MapThumbnailDrawer.base64(map, 512, 256) + ">");

		html.append("<table border='1px solid' class='t'><tr><td class='h' colspan='2'>Map Compilation Report</td></tr>");

		// noformat
		String[] properties = new String[] {
				"Title", 		map.title,
				"Filename", 	new StringBuilder(file.path()).insert(file.path().lastIndexOf("/") + 1, "<br>").toString(),
				"Date",			new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()),
				"Width", 		String.valueOf(map.tilemap.getWidth()),
				"Height", 		String.valueOf(map.tilemap.getHeight()),
				"Blocks", 		String.valueOf(map.tilemap.getWidth() * map.tilemap.getHeight()),
				"Points", 		String.valueOf(map.points.size),
				"Filesize", 	new DecimalFormat("#,###,###").format(file.file().length()) + " Byte",
				"Needed Time",	compileTime + " ms"
		};
		// /noformat

		for (int i = 0; i != properties.length; i += 2)
		{
			html.append("<tr><td class='p'>" + properties[i] + "</td><td class='p'>" + properties[i + 1] + "</td></tr>");
			report[0] += properties[i] + "<td>" + (properties[i + 1].contains("<br>") ? properties[i + 1].substring(properties[i + 1].indexOf("<br>") + 4) : properties[i + 1]) + "<tr>";
		}

		html.append("</table>");
		html.append("<table class='t' border='1px solid'><tr><td class='h' colspan='4'>Points</td></tr><tr><td></td><td class='p'>x</td><td class='p'>y</td><td class='p'>Refs</td>");

		for (int i = 0; i != map.points.size; i++)
			html.append("<tr><td class='p'>Point #" + i + "</td><td class='p'>" + map.points.get(i).x + "</td><td class='p'>" + map.points.get(i).y + "</td><td class='p'>" + "" /* points[i].refs */+ "?</td>");
		// if (points[i].refs == 0)
		// warnings.add("Point #" + i + " is unused.");

		html.append("</table></td><td style='vertical-align:top;padding-left:10px'><!--w-->");

		// Rechte Seite im Report:

		// Pseudo Code Table
		html.append("<table class='t' border='1px solid'><tr><td class='h'>Pseudo Code</td></tr><tr><td class='p' style='white-space:pre;text-align:left'>" + code.replace("\n", "<br>") + "</td></tr></table>");

		html.append("</td><table border='1x solid'>");

		for (int y = 0; y != map.tilemap.getHeight(); y++)
		{
			html.append("<tr><td class='s'>" + (map.tilemap.getHeight() - 1 - y) + "</td>");
			for (int x = 0; x != map.tilemap.tiles.length; x++)
			{
				Tile tile = map.tilemap.tiles[x][map.tilemap.getHeight() - 1 - y];
				html.append("<td class='" + (tile.type == 0 ? "e" : "b") + "'>" + tile.type + "</td>");
				if (!tile.isSolid() && (x == 0 || x == map.tilemap.getWidth() - 1 || y == 0 || y == map.tilemap.getHeight() - 1)) // Prüfen ob man aus der Map fliegen könnte
					warnings.add("Block at <" + x + ", " + (map.tilemap.getHeight() - 1 - y) + "> is on edge but not solid.");
			}
			html.append("</tr>");
		}

		html.append("<tr><td></td>");
		for (int x = 0; x != map.tilemap.getWidth(); x++)
			html.append("<td class='s'>" + x + "</td>");
		html.append("</tr></table>");

		html.append("</body></html>");

		StringBuilder warningsTable = new StringBuilder("<table class='t' border='1px solid'><tr><td class='h'>Warnings</td></tr>");

		for (String warning : warnings)
			warningsTable.append("<tr><td class='p'>" + warning + "</td></tr>");

		warningsTable.append("</table>");

		html.insert(html.indexOf("<!--w-->") + 8, warningsTable.toString());

		FileHandle htmlHandle = Devrays.file("log/compilation/" + filename + ".html");
		htmlHandle.writeString(html.toString(), false);

		report[1] = warnings.toString("\n");
		return report;
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

	private static String validateArgument(Map map, Object argument)
	{
		if (argument == null)
			return "is null!";

		if (argument instanceof Point && !map.points.contains((Point) argument, false))
			return "is a dead Point reference!";

		return null;
	}
}