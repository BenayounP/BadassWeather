package eu.benayoun.badassweather.background.jobs.uiupdate;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badass.utility.model.ArrayListUtils;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.background.jobs.forecast.YrNoWeather.YrNoForecastUtils;
import eu.benayoun.badassweather.model.bare.forecast.AtomicBareForecastModel;



public class UiUpdateJob extends BadassJob
{


    @Override
    public BadassJob.State getStartingState()
    {
        return State.START_ASAP;
    }

    @Override
    public void work()
    {
        long                               nowInMs = BadassTimeUtils.getCurrentTimeInMs();
        ArrayList<AtomicBareForecastModel> oneHourBareForecastList = ThisApp.getModel().bareModel.forecastBareCache.getOneHourBareForecastList();

        String bareCurrentWeather="";
        String nextWeather="";
        boolean goToSleep=true;

        if (ArrayListUtils.isNOTNullOrEmpty(oneHourBareForecastList))
        {
            AtomicBareForecastModel atomicBareForecastModel;

            // Current Weather;
            int currentWeatherIndex;
            int oneHourBareForecastListSize = oneHourBareForecastList.size();
            for (currentWeatherIndex=0; currentWeatherIndex< oneHourBareForecastListSize; currentWeatherIndex++)
            {
                atomicBareForecastModel = oneHourBareForecastList.get(currentWeatherIndex);
                if (atomicBareForecastModel!=null && atomicBareForecastModel.getUTCDurationInMs().contains(nowInMs))
                {
                    bareCurrentWeather =  YrNoForecastUtils.getWeatherSymbolString(atomicBareForecastModel.getWeatherSymbol());
                    break;
                }
            }
            if (false==bareCurrentWeather.equals(""))
            {
                // Next Weather
                String processedWeather;
                int nextWeatherIndex = currentWeatherIndex + 1;
                long nextWeatherStartInMs = -1;
                while (nextWeatherIndex < oneHourBareForecastListSize)
                {
                    atomicBareForecastModel = oneHourBareForecastList.get(nextWeatherIndex);
                    processedWeather = YrNoForecastUtils.getWeatherSymbolString(atomicBareForecastModel.getWeatherSymbol());
                    if (processedWeather.equals(bareCurrentWeather) == false)
                    {
                        nextWeather = Badass.getString(R.string.next_weather,
                                BadassTimeUtils.getNiceTimeString(atomicBareForecastModel.getUTCDurationInMs().startTime)
                                        + Badass.getString(R.string.colon_with_spaces) + "\n" + processedWeather);
                        nextWeatherStartInMs = atomicBareForecastModel.getUTCDurationInMs().startTime;
                        break;
                    }
                    else
                    {
                        nextWeatherIndex++;
                    }
                }
                String uiCurrentWeather = Badass.getString(R.string.now_weather, bareCurrentWeather);
                ThisApp.getModel().uIModel.setWeather(uiCurrentWeather, nextWeather);
               if (nextWeatherStartInMs!=-1)
               {
                   schedule(nextStartTimeInMs);
                   goToSleep = false;
               }
            }
        }
        if (goToSleep)
        {
            goToSleep();
        }

    }
}
