package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;

public class DataInitBgndCtrlr
{
	DataInitBgndTask dataInitBgndTask;
	BgndTask         bgndTask;

	public DataInitBgndCtrlr()
	{
		dataInitBgndTask = new DataInitBgndTask(this);
		bgndTask = new BgndTask(dataInitBgndTask);
	}

	public BgndTask getBgndTask()
	{
		return bgndTask;
	}
}

