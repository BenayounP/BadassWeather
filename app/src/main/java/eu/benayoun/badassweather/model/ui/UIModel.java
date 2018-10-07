package eu.benayoun.badassweather.model.ui;

import android.content.SharedPreferences;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.storage.BadassSharedPreferencesStorageContract;
import eu.benayoun.badass.utility.storage.SharedPreferencesStorage;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ui.events.UIEvents;


/**
 * Created by PierreB on 04/02/2018.
 */

public class UIModel implements BadassSharedPreferencesStorageContract
{
	private SharedPreferencesStorage sharedPreferencesStorage;

	private String currentWeather;
	private String nextWeather;
	private String noData;

	public UIModel()
	{
		sharedPreferencesStorage = new SharedPreferencesStorage(Badass.getSimpleClassName(),this);
		currentWeather ="";
		nextWeather="";
		noData = Badass.getString(R.string.no_data);
	}

	public void setWeather(String newCurrentWeather, String newNextWeather)
	{
	    boolean somethingChanged = false;
	    if (newCurrentWeather != null && false == currentWeather.equals(newCurrentWeather))
        {
            currentWeather = newCurrentWeather;
            somethingChanged = true;
        }
        if (newNextWeather != null && false == nextWeather.equals(newNextWeather))
        {
            nextWeather = newNextWeather;
            somethingChanged = true;
        }
        if (somethingChanged)
        {
            sharedPreferencesStorage.save();
            Badass.broadcastUIEvent(UIEvents.WEATHER_CHANGE);
        }
	}

	public boolean isEmpty()
	{
		return currentWeather.equals("") && nextWeather.equals("");
	}

	public String getNoDataString()
	{
		return noData;
	}

	public String getCurrentWeather()
	{
		return currentWeather;
	}

	public String getNextWeather()
	{
		return nextWeather;
	}


	public void load()
	{
		sharedPreferencesStorage.load();
		Badass.broadcastUIEvent(UIEvents.WEATHER_CHANGE);
	}

	@Override
	public void load(SharedPreferences sharedPreferences)
	{
		currentWeather = sharedPreferences.getString("currentWeather","");
		nextWeather = sharedPreferences.getString("nextWeather","");
	}

	@Override
	public void save(SharedPreferences.Editor editor)
	{
		editor.putString("currentWeather",currentWeather);
		editor.putString("nextWeather",nextWeather);
	}
}
