package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.background.backgroundtask.tasks.TaskWorkerContract;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.YrNoWeather.YrNoForecastBgndTask;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;

import static eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl.Status.SLEEPING;

public class ForecastTaskWorker implements TaskWorkerContract
{
	YrNoForecastBgndTask yrNoForecastBgndTask;
	BadassTaskCtrl badassTaskCtrl;

	public ForecastTaskWorker()
	{
		badassTaskCtrl = new BadassTaskCtrl(this);
		yrNoForecastBgndTask = new YrNoForecastBgndTask(badassTaskCtrl);
	}

	@Override
	public BadassTaskCtrl.Status getStartingStatus()
	{
		return SLEEPING;
	}

	@Override
	public void performBgndTask()
	{
		Badass.logInFile("##" + badassTaskCtrl.getName() + " performBgndTask()");
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_weather_report));
		getWeatherForecast();
	}

	@Override
	public BadassTaskCtrl getBadassTaskCtrl()
	{
		return badassTaskCtrl;
	}

	/**
	 * INTERNAL COOKING
	 */



	protected void getWeatherForecast()
	{
		double latitude = ThisApp.getModel().bareModel.locationBareCache.getLastLatitude();
		double longitude = ThisApp.getModel().bareModel.locationBareCache.getLastLongitude();

		if (latitude != LocationBareCache.INVALID_LATITUDE_VALUE)
		{
			if (ThisApp.getBgndTaskCtrl().getAppAndroidEventsMngr().isConnectedToInternet())
			{
				yrNoForecastBgndTask.getYrNoForecast(latitude, longitude);
			}
			else
			{
				Badass.logInFile("##!!" + badassTaskCtrl.getName() + " no internet connection -> Can't get forecast");
				badassTaskCtrl.setGlobalProblemStringId(R.string.app_status_problem_no_internet);
				badassTaskCtrl.onProblem();
			}
		}
		else
		{
			Badass.logInFile("##!!" + badassTaskCtrl.getName() + " no location -> Can't get forecast");
			badassTaskCtrl.setGlobalProblemStringId(R.string.app_status_problem_location);
			badassTaskCtrl.onProblem();
		}
	}
}
