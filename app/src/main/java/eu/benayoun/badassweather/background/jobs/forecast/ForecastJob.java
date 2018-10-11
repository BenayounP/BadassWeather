package eu.benayoun.badassweather.background.jobs.forecast;

import android.text.format.DateUtils;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badass.utility.math.MathUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.background.jobs.forecast.YrNoWeather.YrNoForecastJob;
import eu.benayoun.badassweather.background.jobs.location.main.LocationBareCache;


public class ForecastJob extends BadassJob
{
	private YrNoForecastJob yrNoForecastJob;

	public ForecastJob()
	{
		yrNoForecastJob = new YrNoForecastJob(this);
	}

	@Override
	protected State getStartingState()
	{
		return State.SLEEPING;
	}

	@Override
	protected void work()
	{
		Badass.logInFile("##" + getName() + " performBgndTask()");
		ThisApp.getModel().appStateCtrl.setJobRunning(Badass.getString(R.string.app_state_getting_weather_report));
		getWeatherForecast();
	}

	@Override
	public void schedule(long nextCallTimeInMs)
	{
		long randomInterval = MathUtils.getNextLong(DateUtils.MINUTE_IN_MILLIS * 10);
		super.schedule(nextCallTimeInMs + randomInterval);
	}

	/**
	 * INTERNAL COOKING
	 */


    private void getWeatherForecast()
	{
		double latitude = ThisApp.getModel().bareModel.locationBareCache.getLastLatitude();
		double longitude = ThisApp.getModel().bareModel.locationBareCache.getLastLongitude();

		if (latitude != LocationBareCache.INVALID_LATITUDE_VALUE)
		{
			if (Badass.isConnectedToInternet())
			{
				yrNoForecastJob.getYrNoForecast(latitude, longitude);
			}
			else
			{
				Badass.logInFile("##!!" + getName() + " no internet connection -> Can't get forecast");
				setGlobalProblemStringId(R.string.app_state_problem_no_internet);
				askToResolveProblem();
			}
		}
		else
		{
			Badass.logInFile("##!!" + getName() + " no location -> Can't get forecast");
			setGlobalProblemStringId(R.string.app_state_problem_location);
			askToResolveProblem();
		}
	}
}
