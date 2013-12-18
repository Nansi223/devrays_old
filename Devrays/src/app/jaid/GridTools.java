package app.jaid;

import java.util.ArrayList;
import java.util.List;

import app.jaid.devrays.debug.Log;

public class GridTools {

	public static List<int[]> line(int x1, int y1, int x2, int y2)
	{
		int bx = x1; // TEMP Können irgendwann wieder weg, werden nur für die Logline unten gebraucht
		int by = y1;

		List<int[]> points = new ArrayList<int[]>();

		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;

		int err = dx - dy;

		while (true)
		{
			points.add(new int[] {x1, y1});

			if (x1 == x2 && y1 == y2)
				break;

			int e2 = 2 * err;

			if (e2 > -dy)
			{
				err = err - dy;
				x1 = x1 + sx;
			}

			if (e2 < dx)
			{
				err = err + dx;
				y1 = y1 + sy;
			}
		}

		Log.m("Bresenham computed! {" + bx + ", " + by + "} to {" + x2 + ", " + y2 + "}, " + points.size() + " points.");
		return points;
	}

}
