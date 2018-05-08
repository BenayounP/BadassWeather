package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.uiupdate;

import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;

public class UiUpdateBgndCtrl
{
	UiUpdateBgndTask uiUpdateBgndTask;
	BgndTask         bgndTask;

	public UiUpdateBgndCtrl()
	{
		uiUpdateBgndTask = new UiUpdateBgndTask(this);
		bgndTask = new BgndTask(uiUpdateBgndTask);
	}

	public BgndTask getBgndTask()
	{
		return bgndTask;
	}
}
