package app.jaid;

import com.badlogic.gdx.utils.TimeUtils;

public class JTimeUtils {

	public static long[]	unitLengths;
	public static String[]	unitNames;

	static
	{
		long LENGTH_SECOND = 1000;
		long LENGTH_MINUTE = LENGTH_SECOND * 60;
		long LENGTH_HOUR = LENGTH_MINUTE * 60;
		long LENGTH_DAY = LENGTH_HOUR * 24;
		long LENGTH_MONTH = (int) (LENGTH_DAY * (365.25f / 12f));
		long LENGTH_YEAR = LENGTH_MONTH * 12;

		unitLengths = new long[] {1, LENGTH_SECOND, LENGTH_MINUTE, LENGTH_HOUR, LENGTH_DAY, LENGTH_MONTH, LENGTH_YEAR};
		unitNames = new String[] {"millisecond", "second", "minute", "hour", "day", "month", "year"};
	}

	public static String differenceToString(final long timestamp)
	{
		long difference = TimeUtils.millis() - timestamp;
		final boolean isFuture = difference < 0;
		difference = Math.abs(difference);

		for (int i = 0; i != unitLengths.length; i++)
			if (i == 6 || difference < unitLengths[i + 1])
			{
				int count = (int) (difference / unitLengths[i]);
				String diffString = count + " " + unitNames[i] + (count == 1 ? "" : "s");
				return isFuture ? "in " + diffString : diffString + " ago";
			}

		return "[JTimeUtils] ERROR";
	}

}
