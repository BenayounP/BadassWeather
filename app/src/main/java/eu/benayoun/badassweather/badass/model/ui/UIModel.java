package eu.benayoun.badassweather.badass.model.ui;

import android.content.SharedPreferences;
import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.cache.SharedPreferencesFile;
import eu.benayoun.badass.utility.cache.SharedPreferencesSubCache;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

/**
 * Created by PierreB on 04/02/2018.
 */

public class UIModel implements SharedPreferencesSubCache
{
	protected SharedPreferencesFile sharedPreferencesFile;

	protected String currentWeather;
	protected String nextWeather;
	protected String noData;

	public UIModel()
	{
		sharedPreferencesFile = new SharedPreferencesFile(Badass.getSimpleClassName(),this);
		currentWeather ="";
		nextWeather="";
		noData = Badass.getString(R.string.no_data);
	}

	public void setWeather(String currentWeather, String nextWeather)
	{
		this.currentWeather = currentWeather;
		this.nextWeather = nextWeather;
		sharedPreferencesFile.save();
		Badass.broadcastUIEvent(UIEvents.WEATHER_CHANGE);
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
		sharedPreferencesFile.load();
		Badass.broadcastUIEvent(UIEvents.WEATHER_CHANGE);
	}

	@Override
	public void loadData(SharedPreferences sharedPreferences)
	{
		currentWeather = sharedPreferences.getString("currentWeather","");
		nextWeather = sharedPreferences.getString("nextWeather","");
	}

	@Override
	public void saveData(SharedPreferences.Editor editor)
	{
		editor.putString("currentWeather",currentWeather);
		editor.putString("nextWeather",nextWeather);
	}
}
