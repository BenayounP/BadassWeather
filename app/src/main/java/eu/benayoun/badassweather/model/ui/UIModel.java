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

	public void setWeather(String currentWeather, String nextWeather)
	{
	    boolean somethingChanged = false;
	    if (this.currentWeather != currentWeather)
        {
            this.currentWeather = currentWeather;
            somethingChanged = true;
        }
        if (this.nextWeather != nextWeather)
        {
            this.nextWeather = nextWeather;
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
