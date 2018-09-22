package eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.Model;



/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitWorker extends BadassJob
{

	@Override
	public BadassJob.Status getStartingStatus()
	{
		return Status.START_ASAP;
	}


	@Override
	public void work()
	{
		Model model = ThisApp.getModel();
		model.appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_loading_data));
		model.load();
		if (model.bareModel.locationBareCache.isValid())
		{
            ThisApp.getAppWorkersCtrl().setForecast();
        }
		goToSleep();
	}
}
