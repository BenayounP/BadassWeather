package eu.benayoun.badassweather.model.bare.forecast;

import android.content.SharedPreferences;

import java.util.ArrayList;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.model.ArrayListUtils;
import eu.benayoun.badass.utility.os.time.BadassUtilsTime;
import eu.benayoun.badass.utility.storage.BadassSharedPreferencesStorageContract;
import eu.benayoun.badass.utility.storage.BadassSharedPreferencesStorage;


/**
 * Created by PierreB on 17/07/2017.
 */

public class ForecastBareCache implements BadassSharedPreferencesStorageContract
{

	private final String KEY = "ForecastBareCacheContainer_";

	private BadassSharedPreferencesStorage sharedPreferencesStorage;

	// setNextWorkInTheBackground
    private long lastForecastUpdateInMs;

	// weather report
    private long nextWeatherReportInMs;

	private ArrayList<AtomicBareForecastModel> oneHourBareForecastList;

	public ForecastBareCache()
	{
		sharedPreferencesStorage = new BadassSharedPreferencesStorage(Badass.getSimpleClassName(),this);
		oneHourBareForecastList = new ArrayList<>();
	}

	// MAIN METHODS

	public void updateAndSave(ArrayList<AtomicBareForecastModel> newOneHourBareForecastList, long nextWeatherReportInMsArg)
	{
        oneHourBareForecastList = newOneHourBareForecastList;
		nextWeatherReportInMs = nextWeatherReportInMsArg;
		sharedPreferencesStorage.save();
		lastForecastUpdateInMs = BadassUtilsTime.getCurrentTimeInMs();
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
        AtomicBareForecastModel atomicBareForecastModel;
		for (int i = 0; i< oneHourBareForecastList.size(); i++)
		{
            atomicBareForecastModel = oneHourBareForecastList.get(i);
            if (atomicBareForecastModel!=null)
            {
                atomicBareForecastModel.save(KEY + i, editor);
            }
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
			stringBuilder.append(atomicBareForecastModel.toLogString()).append("\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * INTERNAL COOKING
	 */



}
