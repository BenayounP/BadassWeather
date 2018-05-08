package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.AppBgndTask;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.YrNoWeather.YrNoForecastBgndCtrl;
import eu.benayoun.badassweather.badass.model.LocationUtils;

public class ForecastBgndTask implements AppBgndTask
{
	ForecastBgndCtrl     forecastBgndCtrl;
	YrNoForecastBgndCtrl yrNoForecastBgndCtrl;

	public ForecastBgndTask(ForecastBgndCtrl forecastBgndCtrl)
	{
		this.forecastBgndCtrl = forecastBgndCtrl;
		yrNoForecastBgndCtrl = new YrNoForecastBgndCtrl(forecastBgndCtrl);
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return forecastBgndCtrl.getBgndTask().getCurrentStatus();
	}

	@Override
	public int getFirstStatusEver()
	{
		return BgndTask.STATUS_SLEEPING;
	}

	@Override
	public void performBgndTask()
	{
		Badass.logInFile("##" + forecastBgndCtrl.getBgndTask().getName() + " performBgndTask()");
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_weather_report));
		getWeatherForecast();
	}

	/**
	 * INTERNAL COOKING
	 */



	protected void getWeatherForecast()
	{
		double latitude = ThisApp.getModel().bareModel.locationBareCache.getLastLatitude();
		double longitude = ThisApp.getModel().bareModel.locationBareCache.getLastLongitude();

		if (latitude != LocationUtils.INVALID_LATITUDE_VALUE)
		{
			if (ThisApp.getThisAppBgndMngr().getThisAppAndroidEventsMngr().isConnectedToInternet())
			{
				yrNoForecastBgndCtrl.getYrNoForecast(latitude, longitude);
			}
			else
			{
				Badass.logInFile("##!!" + forecastBgndCtrl.getBgndTask().getName() + " no internet connection -> Can't get forecast");
				forecastBgndCtrl.getBgndTask().setGlobalProblemStringId(R.string.app_status_problem_no_internet);
				forecastBgndCtrl.getBgndTask().onProblem();
			}
		}
		else
		{
			Badass.logInFile("##!!" + forecastBgndCtrl.getBgndTask().getName() + " no location -> Can't get forecast");
			forecastBgndCtrl.getBgndTask().setGlobalProblemStringId(R.string.app_status_problem_location);
			forecastBgndCtrl.getBgndTask().onProblem();
		}
	}
}
