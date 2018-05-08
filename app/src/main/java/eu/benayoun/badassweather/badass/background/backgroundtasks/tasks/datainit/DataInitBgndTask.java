package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.AppBgndTask;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;


/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitBgndTask implements AppBgndTask
{

	DataInitBgndCtrlr dataInitBgndCtrlr;

	public DataInitBgndTask(DataInitBgndCtrlr dataInitBgndCtrlr)
	{
		this.dataInitBgndCtrlr = dataInitBgndCtrlr;
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return BgndTask.STATUS_UPDATE_ASAP;
	}

	@Override
	public int getFirstStatusEver()
	{
		return BgndTask.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		Badass.log("## DataInitBgndTask performBgndTask");
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_loading_data));
		ThisApp.getModel().load();
		dataInitBgndCtrlr.bgndTask.sleep();
	}
}
