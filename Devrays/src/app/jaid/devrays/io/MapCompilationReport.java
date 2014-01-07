package app.jaid.devrays.io;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import app.jaid.Jtil;
import app.jaid.devrays.debug.Log;
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
		// f1 / f2 (File Content Table TR)
		// htr (Hexadecimal table row)
		// atr (ASCII table row)
		// n (invisble character in ASCII table cell)
		// i (File content Byte Index Cell)
		// eT / eB / eR / eL (Tile Edge Border Classes, Top / Bottom / Right / Left)

		StringBuilder html = new StringBuilder();
		html.append("<html><head><style type='text/css'>body{font-family:\"Lucida Console\",\"Lucida Sans Typewriter\"}");
		html.append("table{margin-top:10px;border-color:#555;border-collapse:collapse}");
		html.append("td{min-width:20px;height:20px;text-align:center;vertical-align:middle}.b{min-width:20px;background-color:#666;color:#FFF;font-size:10px}");
		html.append(".e{min-width:20px;background-color:#000;color:#FFF;font-size:10px}.p{color:#666;padding:5px}");
		html.append(".s{color:#666;font-size:10px}.h{background-color:#999;color:#FFF;padding:5px}.t{width:500px}");
		html.append(".f1{background-color:#DDD;color:#000;font-size:10px}");
		html.append(".f2{background-color:#FFF;color:#000;font-size:10px}");
		html.append("#htr td {min-width:17px}");
		html.append("#atr td {min-width:11px}");
		html.append(".n{background: linear-gradient(to bottom, #999 0%,#777 100%)}");
		html.append(".i{color:#666}");
		html.append(".eT{border-top:1px solid white}");
		html.append(".eB{border-bottom:1px solid white}");
		html.append(".eL{border-left:1px solid white}");
		html.append(".eR{border-right:1px solid white}");
		html.append("</style></head>");

		html.append("<body style='background-color:gray'><div align='center'><div style='box-shadow:0 0 10px #000;display:inline-block;padding:20px;background-color:white'><table style='margin:0'><tr><td style='vertical-align:top'>");

		// Wieso sind die PNGs kaputt?
		MapThumbnailDrawer.base64(map, 512, 256);
		// html.append("<img src='data:image/png;base64," + + ">");

		html.append("<table border='1px solid' class='t'><tr><td class='h' colspan='2'>Map Compilation Report</td></tr>");

		// noformat
		String[] properties = new String[] {
				"Title", 			map.title,
				"Filename", 		new StringBuilder(file.path()).insert(file.path().lastIndexOf("/") + 1, "<br>").toString(),
				"Date",				new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()),
				"Size", 			String.valueOf(map.tilemap.getWidth()) + " x " + String.valueOf(map.tilemap.getHeight()),
				"Block Amount", 			String.valueOf(map.tilemap.getWidth() * map.tilemap.getHeight()),
				"Points", 			String.valueOf(map.points.size),
				"Inflated Size",	"<!--u--> Bytes",	
				"Deflated Size",	new DecimalFormat("#,###,###").format(file.length()) + " Bytes <!--p-->",
				"Needed Time",		compileTime + " ms"
		};
		// /noformat

		for (int i = 0; i != properties.length; i += 2)
			html.append("<tr><td class='p'>" + properties[i] + "</td><td class='p'>" + properties[i + 1] + "</td></tr>");

		html.append("</table>");
		html.append("<table class='t' border='1px solid'><tr><td class='h' colspan='4'>Points</td></tr><tr><td></td><td class='p'>x</td><td class='p'>y</td><td class='p'>Refs</td>");

		for (int i = 0; i != map.points.size; i++)
			html.append("<tr><td class='p'>Point #" + i + "</td><td class='p'>" + map.points.get(i).x + "</td><td class='p'>" + map.points.get(i).y + "</td><td class='p'>" + "" /* points[i].refs */+ "?</td>");
		// if (points[i].refs == 0)
		// warnings.add("Point #" + i + " is unused.");

		html.append("</table></td><td style='vertical-align:top;padding-left:10px'><!--w-->");

		// Rechte Seite im Report:

		// Pseudo Code Table
		html.append("<table class='t' border='1px solid'><tr><td class='h'>Pseudo Code</td></tr><tr><td class='p' style='max-width:500px;overflow:overlay;white-space:pre;text-align:left'>" + LogicUi.getPseudoCode().replace("\n", "<br>") + "</td></tr></table>");

		// Map View

		html.append("</td></tr><tr><td colspan='2'><div style='max-height:640px;max-width:1013px;overflow:auto'><table border='1x solid'>");

		for (int y = 0; y != map.tilemap.getHeight(); y++)
		{
			html.append("<tr><td class='s'>" + (map.tilemap.getHeight() - 1 - y) + "</td>");
			for (int x = 0; x != map.tilemap.tiles.length; x++)
			{
				Tile tile = map.tilemap.tiles[x][map.tilemap.getHeight() - 1 - y];
				html.append("<td class='" + getTileCellClass(map, x, map.tilemap.getHeight() - 1 - y) + "'>" + tile.type + "</td>");

				// Prüfen ob man aus der Map fliegen könnte
				// if (!tile.isSolid() && (x == 0 || x == map.tilemap.getWidth() - 1 || y == 0 || y == map.tilemap.getHeight() - 1))
				// warnings.add("Block at <" + x + ", " + (map.tilemap.getHeight() - 1 - y) + "> is on edge but not solid.");
			}
			html.append("</tr>");
		}

		html.append("<tr><td></td>");
		for (int x = 0; x != map.tilemap.getWidth(); x++)
			html.append("<td class='s'>" + x + "</td>");
		html.append("</tr></table></div>");

		// Build Decimal, Hexadecimal and ASCII Representation Table of File Contents

		StringBuilder indexTable = new StringBuilder("<table border='1px solid' class='f'><tr><td class='h'>Byte</td></tr>");
		StringBuilder decView = new StringBuilder("<table border='1px solid' class='f'><tr><td colspan='16' class='h'>Decimal View</td></tr>");
		StringBuilder hexView = new StringBuilder("<table border='1px solid' class='f'><tr><td colspan='16' class='h'>Hexadecimal View</td></tr>");
		StringBuilder asciiView = new StringBuilder("<table  border='1px solid' class='f'><tr><td colspan='16' class='h'>ASCII View</td></tr>");
		InflaterInputStream stream = new InflaterInputStream(file.read());

		int currentColumn = 15, columnCount = 0;
		boolean evenRow = false;

		try
		{
			while (stream.available() != 0)
			{
				int b = stream.read();
				if (b > -1 && b < 256)
				{

					if (currentColumn++ == 15)
					{
						currentColumn = 0;
						indexTable.append("</tr><tr><td class='i'>" + columnCount++ * 16 + "</td>");
						decView.append("</tr><tr class='" + (evenRow ? "f1" : "f2") + "'>");
						hexView.append("</tr><tr id='htr' class='" + (evenRow ? "f1" : "f2") + "'>");
						asciiView.append("</tr><tr id='atr' class='" + (evenRow ? "f1" : "f2") + "'>");
						evenRow = !evenRow;
					}

					decView.append("<td>" + b + "</td>");
					hexView.append("<td>" + String.format("%02X", b) + "</td>");
					asciiView.append(Jtil.isPrintableChar((char) (byte) b) ? "<td>" + String.valueOf((char) (byte) b) + "</td>" : "<td class='n'></td>");
				}
			}

			// We can obtain the inflated file size now, so extend the property field <!--u-->
			int inflatedSize = columnCount * 16 + currentColumn + 1; // +1 because we skipped the last byte (value -1) above
			html.insert(html.indexOf("<!--u-->") + 8, new DecimalFormat("#,###,###").format(inflatedSize));
			html.insert(html.indexOf("<!--p-->") + 8, "(" + (int) ((float) file.length() / inflatedSize * 100) + "%)");

		} catch (IOException e)
		{
			app.jaid.devrays.debug.Log.e(e);
		}

		// Insert to html

		indexTable.append("</table>");
		decView.append("</table>");
		hexView.append("</table>");
		asciiView.append("</table>");

		html.append("<div style='max-height:640px;overflow:auto;margin-top:10px'><table style='margin:0'><tr><td>");
		html.append(indexTable);
		html.append("</td><td>");
		html.append(decView);
		html.append("</td><td>");
		html.append(hexView);
		html.append("</td><td>");
		html.append(asciiView);
		html.append("</td></tr></table></div>");

		// End html

		html.append("</div></div></body></html>");

		// Build warnings list based on warnings Array and insert after <!--w--> comment

		StringBuilder warningsTable = new StringBuilder("<table class='t' border='1px solid'><tr><td class='h'>Warnings</td></tr>");

		if (warnings.size == 0)
			warningsTable.append("<tr><td class='p'>Everything's fine!</td></tr>");
		else
			for (String warning : warnings)
				warningsTable.append("<tr><td class='p'>" + warning + "</td></tr>");
		warningsTable.append("</table>");
		html.insert(html.indexOf("<!--w-->") + 8, warningsTable.toString());

		return html.toString();
	}

	private static String getTileCellClass(Map map, int x, int y)
	{
		Tile tile = map.tilemap.getTile(x, y);
		String classField = tile.type == 0 ? "e" : "b";

		if (x != 0 && tile.type != map.tilemap.getTile(x - 1, y).type)
			classField += " eL";

		if (x != map.tilemap.getWidth() - 1 && tile.type != map.tilemap.getTile(x + 1, y).type)
			classField += " eR";

		if (y != 0 && tile.type != map.tilemap.getTile(x, y - 1).type)
			classField += " eB";

		if (y != map.tilemap.getHeight() - 1 && tile.type != map.tilemap.getTile(x, y + 1).type)
			classField += " eT";

		return classField;
	}
}
