package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.AppBgndTaskCntrl;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTaskCntrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitBgndCtrl implements AppBgndTaskCntrl
{

	DataInitBgndCtrlrManager dataInitBgndCtrlrManager;

	public DataInitBgndCtrl(DataInitBgndCtrlrManager dataInitBgndCtrlrManager)
	{
		this.dataInitBgndCtrlrManager = dataInitBgndCtrlrManager;
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return BgndTaskCntrl.STATUS_UPDATE_ASAP;
	}

	@Override
	public int getFirstStatusEver()
	{
		return BgndTaskCntrl.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		Badass.log("## DataInitBgndCtrl performBgndTask");
		ThisApp.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_loading_data));
		ThisApp.getDataContainer().load();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_UIDATA_AVAILABLE);
		dataInitBgndCtrlrManager.bgndTaskCntrl.sleep();
	}
}
