package eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.backgroundworker.backgroundtask.BadassBgndTaskCntrl;
import eu.benayoun.badass.backgroundworker.backgroundtask.BgndTaskCntrlManager;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitBgndCtrl extends BgndTaskCntrlManager
{
	@Override
	public int getOnAppInitialisationStatus()
	{
		return BadassBgndTaskCntrl.STATUS_UPDATE_ASAP;
	}

	@Override
	public int getFirstStatusEver()
	{
		return BadassBgndTaskCntrl.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		AppBadass.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_loading_data));
		AppBadass.getDataContainer().load();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_UIDATA_AVAILABLE);
		badassBgndTaskCntrl.sleep();
	}
}
