package eu.benayoun.badassweather.background.jobs.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.model.Model;


/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitJob extends BadassJob
{

	@Override
	public BadassJob.State getStartingState()
	{
		return State.START_ASAP;
	}


	@Override
	public void work()
	{
		Model model = ThisApp.getModel();
		model.appStateCtrl.setJobRunning(Badass.getString(R.string.app_state_loading_data));
		model.load();
		if (model.bareModel.locationBareCache.isValid())
		{
            ThisApp.getAppBadassJobList().setForecast();
        }
		goToSleep();
	}
}
