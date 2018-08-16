package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast;

import android.text.format.DateUtils;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassBgndWorker;
import eu.benayoun.badass.utility.math.MathUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.YrNoWeather.YrNoForecastBgndTask;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;

public class ForecastWorker extends BadassBgndWorker
{
	YrNoForecastBgndTask yrNoForecastBgndTask;

	public ForecastWorker()
	{
		yrNoForecastBgndTask = new YrNoForecastBgndTask(this);
	}

	@Override
	protected Status getStartingStatus()
	{
		return Status.SLEEPING;
	}

	@Override
	protected void work()
	{
		Badass.logInFile("##" + getName() + " performBgndTask()");
		ThisApp.getModel().appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_weather_report));
		getWeatherForecast();
	}

	@Override
	public void setNextWorkingSession(long nextCallTimeInMs)
	{
		long randomInterval = MathUtils.getNextLong(DateUtils.MINUTE_IN_MILLIS * 10);
		super.setNextWorkingSession(nextCallTimeInMs + randomInterval);
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
			if (Badass.isConnectedToInternet())
			{
				yrNoForecastBgndTask.getYrNoForecast(latitude, longitude);
			}
			else
			{
				Badass.logInFile("##!!" + getName() + " no internet connection -> Can't get forecast");
				setGlobalProblemStringId(R.string.app_status_problem_no_internet);
				onProblem();
			}
		}
		else
		{
			Badass.logInFile("##!!" + getName() + " no location -> Can't get forecast");
			setGlobalProblemStringId(R.string.app_status_problem_location);
			onProblem();
		}
	}
}
