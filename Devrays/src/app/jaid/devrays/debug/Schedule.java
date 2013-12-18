package app.jaid.devrays.debug;

import static app.jaid.devrays.debug.Log.m;

import java.util.HashMap;

import app.jaid.devrays.Devrays;

public class Schedule {

	private static HashMap<Object, Schedule>	schedules	= new HashMap<Object, Schedule>();

	public static void add(Object key, float seconds)
	{
		schedules.put(key, new Schedule(seconds));
		m("Schedule " + key.toString() + " added (every " + seconds + " seconds)");
	}

	public static boolean request(Object key)
	{
		Schedule schedule = schedules.get(key);

		if (schedule.countdown > 0) return false;

		schedule.countdown = schedule.period + Math.min(schedule.period % schedule.countdown, 0);
		return true;
	}

	public static void update()
	{
		for (Schedule schedule : schedules.values())
			schedule.countdown -= Devrays.delta;

	}

	float	period, countdown;

	Schedule(float seconds)
	{
		period = seconds;
		countdown = period;
	}
}
