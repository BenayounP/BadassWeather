package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast;

import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;

public class ForecastBgndCtrl
{
	ForecastBgndTask forecastBgndTask;
	BgndTask bgndTask;

	public ForecastBgndCtrl()
	{
		forecastBgndTask = new ForecastBgndTask(this);
		bgndTask = new BgndTask(forecastBgndTask);
	}

	public BgndTask getBgndTask()
	{
		return bgndTask;
	}
}
