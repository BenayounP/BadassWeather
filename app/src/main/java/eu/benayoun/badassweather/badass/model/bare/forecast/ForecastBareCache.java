package eu.benayoun.badassweather.badass.model.bare.forecast;

import android.content.SharedPreferences;

import java.util.ArrayList;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.storage.SharedPreferencesStorage;
import eu.benayoun.badass.utility.storage.BadassSharedPreferencesStorageContract;
import eu.benayoun.badass.utility.model.ArrayListUtils;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;


/**
 * Created by PierreB on 17/07/2017.
 */

public class ForecastBareCache implements BadassSharedPreferencesStorageContract
{

	protected final String KEY = "ForecastBareCacheContainer_";

	protected SharedPreferencesStorage sharedPreferencesStorage;

	// setNextWorkInTheBackground
	long lastForecastUpdateInMs;

	// weather report
	long nextWeatherReportInMs;

	protected ArrayList<AtomicBareForecastModel> oneHourBareForecastList;

	public ForecastBareCache()
	{
		sharedPreferencesStorage = new SharedPreferencesStorage(Badass.getSimpleClassName(),this);
		oneHourBareForecastList = new ArrayList<>();
	}

	// MAIN METHODS

	public void updateAndSave(ArrayList<AtomicBareForecastModel> oneHourForecastList, long nextWeatherReportInMsArg)
	{
		this.oneHourBareForecastList = oneHourForecastList;
		nextWeatherReportInMs = nextWeatherReportInMsArg;
		sharedPreferencesStorage.save();
		lastForecastUpdateInMs = BadassTimeUtils.getCurrentTimeInMs();
	}


	// GETTERS

	public long getLastForecastUpdateInMs()
	{
		return lastForecastUpdateInMs;
	}

	public long getNextWeatherReportInMs()
	{
		return nextWeatherReportInMs;
	}



	// LOAD AND SAVE

	public void load()
	{
		sharedPreferencesStorage.load();
	}

	@Override
	public void load(SharedPreferences sharedPreferences)
	{
		lastForecastUpdateInMs = sharedPreferences.getLong("lastForecastUpdateInMs", -1);
		nextWeatherReportInMs = sharedPreferences.getLong("nextWeatherReportInMs", -1);
		int bareOneHourListSize = sharedPreferences.getInt("bareOneHourListSize",0);

		if (bareOneHourListSize != 0)
		{
			AtomicBareForecastModel atomicBareForecastModel;
			for (int i = 0; i< bareOneHourListSize ; i++)
			{
				atomicBareForecastModel = new AtomicBareForecastModel();
				atomicBareForecastModel.load(KEY+i,sharedPreferences);
				oneHourBareForecastList.add(atomicBareForecastModel);
			}
		}

	}

	@Override
	public void save(SharedPreferences.Editor editor)
	{
		editor.putLong("lastForecastUpdateInMs", lastForecastUpdateInMs);
		editor.putLong("nextWeatherReportInMs", nextWeatherReportInMs);
		editor.putInt("bareOneHourListSize", oneHourBareForecastList.size());
		for (int i = 0; i< oneHourBareForecastList.size(); i++)
		{
			oneHourBareForecastList.get(i).save(KEY+i,editor);
		}
	}


	// GETTERS


	public ArrayList<AtomicBareForecastModel> getOneHourBareForecastList()
	{
		return oneHourBareForecastList;
	}

	public int getSize()
	{
		return ArrayListUtils.getSize(oneHourBareForecastList);
	}

	// containers



	public String toLogString()
	{
		StringBuilder        stringBuilder = new StringBuilder();
		AtomicBareForecastModel atomicBareForecastModel;
		for (int i = 0; i < oneHourBareForecastList.size(); i++)
		{
			atomicBareForecastModel = oneHourBareForecastList.get(i);
			stringBuilder.append(atomicBareForecastModel.toLogString()+"\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * INTERNAL COOKING
	 */

}
