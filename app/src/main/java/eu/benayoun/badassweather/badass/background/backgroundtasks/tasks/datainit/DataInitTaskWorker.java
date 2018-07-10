package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.background.backgroundtask.tasks.TaskWorkerContract;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.Model;

import static eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl.Status.UPDATE_ASAP;


/**
 * Created by PierreB on 13/10/2017.
 */

public class DataInitTaskWorker implements TaskWorkerContract
{

	BadassTaskCtrl badassTaskCtrl;

	public DataInitTaskWorker()
	{
		badassTaskCtrl = new BadassTaskCtrl(this);
	}

	@Override
	public BadassTaskCtrl.Status getStartingStatus()
	{
		return UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		Badass.log("## DataInitTaskWorker performBgndTask");
		Model model = ThisApp.getModel();
		model.appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_loading_data));
		model.load();
		if (model.bareModel.locationBareCache.isValid())
		{
            ThisApp.getBgndTaskCtrl().setForecast();
        }
		badassTaskCtrl.sleep();
	}

    @Override
    public BadassTaskCtrl getBadassTaskCtrl()
    {
        return badassTaskCtrl;
    }
}
