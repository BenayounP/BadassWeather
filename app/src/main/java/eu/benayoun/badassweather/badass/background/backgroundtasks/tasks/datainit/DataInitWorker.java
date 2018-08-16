package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassBgndWorker;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.Model;



/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitWorker extends BadassBgndWorker
{

	@Override
	public BadassBgndWorker.Status getStartingStatus()
	{
		return Status.WORK_ASAP;
	}


	@Override
	public void work()
	{
		Badass.log("## DataInitWorker performBgndTask");
		Model model = ThisApp.getModel();
		model.appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_loading_data));
		model.load();
		if (model.bareModel.locationBareCache.isValid())
		{
            ThisApp.getBgndTaskCtrl().setForecast();
        }
		sleep();
	}
}
