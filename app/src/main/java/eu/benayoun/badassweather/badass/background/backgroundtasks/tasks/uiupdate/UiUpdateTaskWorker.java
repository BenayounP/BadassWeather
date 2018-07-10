package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.uiupdate;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.background.backgroundtask.tasks.TaskWorkerContract;
import eu.benayoun.badass.utility.model.ArrayListUtils;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.YrNoWeather.YrNoForecastUtils;
import eu.benayoun.badassweather.badass.model.bare.forecast.AtomicBareForecastModel;

import static eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl.Status.UPDATE_ASAP;

public class UiUpdateTaskWorker implements TaskWorkerContract
{
    BadassTaskCtrl badassTaskCtrl;

	public UiUpdateTaskWorker()
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
		long                               nowInMs = BadassTimeUtils.getCurrentTimeInMs();
		ArrayList<AtomicBareForecastModel> oneHourBareForecastList = ThisApp.getModel().bareModel.forecastBareCache.getOneHourBareForecastList();

		String bareCurrentWeather="";
		String NextWeather="";

		if (ArrayListUtils.isNOTNullOrEmpty(oneHourBareForecastList))
		{
			AtomicBareForecastModel atomicBareForecastModel;

			// Current Weather;
			int currentWeatherIndex;
			for (currentWeatherIndex=0; currentWeatherIndex< oneHourBareForecastList.size();currentWeatherIndex++)
			{
				atomicBareForecastModel = oneHourBareForecastList.get(currentWeatherIndex);
				if (atomicBareForecastModel.getUTCDurationInMs().contains(nowInMs))
				{
					bareCurrentWeather =  YrNoForecastUtils.getWeatherSymbolString(atomicBareForecastModel.getWeatherSymbol());
					break;
				}
			}

			// Next Weather
			String processedWeather;
			for (int nextWeatherIndex=currentWeatherIndex+1; nextWeatherIndex< oneHourBareForecastList.size();nextWeatherIndex++)
			{
				atomicBareForecastModel = oneHourBareForecastList.get(nextWeatherIndex);
				processedWeather = YrNoForecastUtils.getWeatherSymbolString(atomicBareForecastModel.getWeatherSymbol());
				if (processedWeather.equals(bareCurrentWeather)==false)
				{
					NextWeather = Badass.getString(R.string.next_weather,
							BadassTimeUtils.getNiceTimeString(atomicBareForecastModel.getUTCDurationInMs().startTime)
							+ Badass.getString(R.string.colon_with_spaces)+"\n"+processedWeather);
					break;
				}
			}
		}
		String currentWeather ="";
		if (bareCurrentWeather!="")
		{
			currentWeather = Badass.getString(R.string.now_weather,bareCurrentWeather);
		}
		ThisApp.getModel().uIModel.setWeather(currentWeather,NextWeather);
		if (bareCurrentWeather.equals("")==false)
		{
			badassTaskCtrl.waitForNextCall(getStartOfNextHour());
		}
		else
		{
            badassTaskCtrl.sleep();
		}
	}

	@Override
	public BadassTaskCtrl getBadassTaskCtrl()
	{
		return badassTaskCtrl;
	}

	// INTERNAl COOKING
	protected long getStartOfNextHour()
	{
		long     nowInMs  = BadassTimeUtils.getCurrentTimeInMs();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(nowInMs+DateUtils.HOUR_IN_MILLIS);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
}
