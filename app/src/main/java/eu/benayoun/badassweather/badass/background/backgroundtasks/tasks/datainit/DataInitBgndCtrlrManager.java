package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.background.backgroundtask.tasks.BgndTaskCntrl;

public class DataInitBgndCtrlrManager
{
	DataInitBgndCtrl dataInitBgndCtrl;
	BgndTaskCntrl bgndTaskCntrl;

	public DataInitBgndCtrlrManager()
	{
		dataInitBgndCtrl = new DataInitBgndCtrl(this);
		bgndTaskCntrl = new BgndTaskCntrl(dataInitBgndCtrl);
	}

	public BgndTaskCntrl getBgndTaskCntrl()
	{
		return bgndTaskCntrl;
	}
}
