package eu.benayoun.badassweather.model.bare.forecast;

import android.content.SharedPreferences;
import android.text.format.DateUtils;

import eu.benayoun.badass.utility.model.DurationInMs;
import eu.benayoun.badass.utility.os.time.BadassUtilsTime;
import eu.benayoun.badassweather.background.jobs.forecast.YrNoWeather.YrNoForecastUtils;



/**
 * Created by PierreB on 05/06/2017.
 */

public class AtomicBareForecastModel
{
	static protected float TEMP_EMPTY_VALUE =Float.MIN_VALUE;

	private DurationInMs               UTCDurationInMs;
	private int weatherSymbol;

	public AtomicBareForecastModel()
	{
		UTCDurationInMs = new DurationInMs();
	}

    public AtomicBareForecastModel(AtomicBareForecastModel atomicBareForecastModel)
    {
        this();
        this.UTCDurationInMs = new DurationInMs(atomicBareForecastModel.UTCDurationInMs);
        this.weatherSymbol = atomicBareForecastModel.weatherSymbol;
    }


	// GETTERS AND SETTERS

	// Duration

	public DurationInMs getUTCDurationInMs()
	{
		return UTCDurationInMs;
	}


	// Time
	public void setStartTime(long startTime)
	{
		UTCDurationInMs.startTime = startTime;
	}

	public int getStartTimeInHour()
	{
		return BadassUtilsTime.getHourOfDay24(UTCDurationInMs.startTime);
	}

	public void setEndTime(long endTime)
	{
		UTCDurationInMs.endTime = endTime;
	}

	public AtomicBareForecastModel setOneHourDuration(long startInMS)
	{
		UTCDurationInMs.startTime = startInMS;
		UTCDurationInMs.endTime = startInMS+ DateUtils.HOUR_IN_MILLIS;
		return this;
	}

	public AtomicBareForecastModel setSixHoursDuration(long startInMS)
	{
		UTCDurationInMs.startTime = startInMS;
		UTCDurationInMs.endTime = startInMS+ 6*DateUtils.HOUR_IN_MILLIS;
		return this;
	}

	public long getDeltaDayToToday(long startOfToday)
	{
		long startOfReadCache = UTCDurationInMs.startTime;
		long                    startOfDAYOfReadCache = BadassUtilsTime.getStartOfTheDayInMs(startOfReadCache);
		return (startOfDAYOfReadCache-startOfToday)/ DateUtils.HOUR_IN_MILLIS;
	}

	// Weather Symbol

	public int getWeatherSymbol()
	{
		return weatherSymbol;
	}

	public void setWeatherSymbol(int weatherSymbol)
	{
		this.weatherSymbol = weatherSymbol;
	}

	// SAVED DATA

	public void load(String key, SharedPreferences sharedPref)
	{
		UTCDurationInMs.load(key,sharedPref);
		weatherSymbol = sharedPref.getInt(key + "weatherSymbol",weatherSymbol);
	}

	public void save(String key, SharedPreferences.Editor editor)
	{
		UTCDurationInMs.save(key,editor);
		editor.putInt(key + "weatherSymbol",weatherSymbol);
	}

	public void removeSavedData(String key, SharedPreferences.Editor editor)
	{
		UTCDurationInMs.removeSavedData(key,editor);
		editor.remove(key+"weatherSymbol");
	}



	public String toLogString()
	{
		return UTCDurationInMs.toSimpleString() + ". Forecast: " + YrNoForecastUtils.getWeatherSymbolString(weatherSymbol)+ ".";
	}



	/**
	 * INTERNAL COOKING
	 */



}
