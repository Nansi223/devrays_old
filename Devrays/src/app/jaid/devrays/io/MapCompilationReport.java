package app.jaid.devrays.io;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import app.jaid.devrays.screen.editor.ui.LogicUi;
import app.jaid.devrays.screen.open.MapThumbnailDrawer;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.Tile;

public class MapCompilationReport {

	public static String generate(Map map, Array<String> warnings, FileHandle file, long compileTime)
	{
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

		// Wieso sind die PNGs kaputt?
		MapThumbnailDrawer.base64(map, 512, 256);
		// html.append("<img src='data:image/png;base64," + + ">");

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
			html.append("<tr><td class='p'>" + properties[i] + "</td><td class='p'>" + properties[i + 1] + "</td></tr>");
		// report[0] += properties[i] + "<td>" + (properties[i + 1].contains("<br>") ? properties[i + 1].substring(properties[i + 1].indexOf("<br>") + 4) : properties[i + 1]) + "<tr>";

		html.append("</table>");
		html.append("<table class='t' border='1px solid'><tr><td class='h' colspan='4'>Points</td></tr><tr><td></td><td class='p'>x</td><td class='p'>y</td><td class='p'>Refs</td>");

		for (int i = 0; i != map.points.size; i++)
			html.append("<tr><td class='p'>Point #" + i + "</td><td class='p'>" + map.points.get(i).x + "</td><td class='p'>" + map.points.get(i).y + "</td><td class='p'>" + "" /* points[i].refs */+ "?</td>");
		// if (points[i].refs == 0)
		// warnings.add("Point #" + i + " is unused.");

		html.append("</table></td><td style='vertical-align:top;padding-left:10px'><!--w-->");

		// Rechte Seite im Report:

		// Pseudo Code Table
		html.append("<table class='t' border='1px solid'><tr><td class='h'>Pseudo Code</td></tr><tr><td class='p' style='white-space:pre;text-align:left'>" + LogicUi.getPseudoCode().replace("\n", "<br>") + "</td></tr></table>");

		html.append("</td><table border='1x solid'>");

		for (int y = 0; y != map.tilemap.getHeight(); y++)
		{
			html.append("<tr><td class='s'>" + (map.tilemap.getHeight() - 1 - y) + "</td>");
			for (int x = 0; x != map.tilemap.tiles.length; x++)
			{
				Tile tile = map.tilemap.tiles[x][map.tilemap.getHeight() - 1 - y];
				html.append("<td class='" + (tile.type == 0 ? "e" : "b") + "'>" + tile.type + "</td>");

				// Prüfen ob man aus der Map fliegen könnte
				if (!tile.isSolid() && (x == 0 || x == map.tilemap.getWidth() - 1 || y == 0 || y == map.tilemap.getHeight() - 1))
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
		return html.toString();
	}

}
