package app.jaid;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

import app.jaid.devrays.entity.Enemy;
import app.jaid.devrays.world.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Jtil {

	public static float angleDifference(float a, float b)
	{
		return 180 - Math.abs(Math.abs(a + 180 - b - 180) - 180);
	}

	public static String fillString(int length, char content)
	{
		char[] array = new char[length];
		Arrays.fill(array, content);
		return new String(array);
	}

	public static int inch(int pixels)
	{
		return (int) (pixels * Gdx.graphics.getDensity());
	}

	public static String info(Object object)
	{
		if (object instanceof Rectangle)
		{
			Rectangle rect = (Rectangle) object;
			return "[x: " + rect.x + ", y: " + rect.y + ", width: " + rect.width + ", height: " + rect.height + "]";
		}

		if (object instanceof Color)
		{
			Color color = (Color) object;
			return "[R: " + color.r + ", G: " + color.g + ", B: " + color.b + ", A: " + color.a + ", Hex: " + color.toString() + "]";
		}

		if (object instanceof Enemy)
		{
			Enemy enemy = (Enemy) object;
			return "[Type: " + enemy.type + ", HP: " + enemy.hp + ", Children: " + enemy.children + "]";
		}

		if (object instanceof Point)
		{
			Point point = (Point) object;
			return "[x: " + point.x + ", y: " + point.y;
		}

		if (object instanceof Tile)
		{
			Tile tile = (Tile) object;
			return "[Type: " + tile.type + "]";
		}

		return String.valueOf(object);
	}

	public static boolean isPrintableChar(char c)
	{
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return !Character.isISOControl(c) && block != null && block != Character.UnicodeBlock.SPECIALS;
	}

	public static float moveTo(float value, float change, float origin)
	{
		return value > origin ? value - change < origin ? origin : value - change : value + change > origin ? origin : value + change;
	}

	public static float normalize(float value, float change)
	{
		return moveTo(value, change, 0);
	}

	public static float randomAngle()
	{
		return MathUtils.random(-180f, 180f);
	}

	public static float roundCommaDigits(float value, int commaDigits)
	{
		return Float.valueOf(new DecimalFormat("#." + fillString(commaDigits, '#'), new DecimalFormatSymbols(new Locale("en"))).format(value));
	}

	public static Rectangle scaledRect(Rectangle rect, float scale)
	{
		return new Rectangle(rect.x + rect.width * (scale / 2), rect.y + rect.height * (scale / 2), rect.width - rect.width * scale, rect.height - rect.height * scale);
	}

	public static String size(long size)
	{
		double re;
		String e;

		if (size < 1000)
		{
			re = size;
			e = "";
		}
		else if (size < 1000000)
		{
			re = size / 1000D;
			e = "K";
		}
		else
		{
			re = size / 1000000D;
			e = "M";
		}

		return (int) (re * 100) / 100f + " " + e + "b";
	}

	public static float spmToPeriod(int spm)
	{
		return 60f / spm;
	}

	public static int stackRandom(float... entries)
	{
		float all = 0;
		for (float entry : entries)
			all += entry;
		float random = MathUtils.random(all);
		int i = 0;
		for (float entry : entries)
			if (random <= entry)
				return i;
			else
			{
				i++;
				random -= entry;
			}
		throw new ArrayIndexOutOfBoundsException();
	}

}
