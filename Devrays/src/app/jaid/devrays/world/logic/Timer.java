package app.jaid.devrays.world.logic;

import app.jaid.devrays.Devrays;

public class Timer {

	public float	currentTime, interval;
	public boolean	running;
	public int		steps, currentStep;

	public boolean run()
	{
		if (running)
		{
			currentTime += Devrays.delta;
			if (currentTime > interval)
			{
				currentTime = 0;
				return true;
			}
		}
		return false;
	}

}
