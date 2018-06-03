package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.content.SharedPreferences;
import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.cache.SharedPreferencesFile;
import eu.benayoun.badass.utility.cache.SharedPreferencesSubCache;
import eu.benayoun.badass.utility.math.MathUtils;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.LocationUtils;


/**
 * Created by PierreB on 22/03/2017.
 */

public class LocationBareCache implements SharedPreferencesSubCache
{
	public static float DELTA_DISTANCE_IN_METERS = 5000;

	protected SharedPreferencesFile sharedPreferencesFile;

	protected double   lastLatitude;
	protected double   lastLongitude;
	protected long     lastLocationUpdateInMs;

	protected boolean isLoaded=false;


	public LocationBareCache()
	{
		sharedPreferencesFile = new SharedPreferencesFile(Badass.getSimpleClassName(),this);
		lastLatitude = LocationUtils.INVALID_LATITUDE_VALUE;
		lastLongitude = LocationUtils.INVALID_LONGITUDE_VALUE;
		lastLocationUpdateInMs=-1;
	}

	public void load()
	{
		sharedPreferencesFile.load();
		isLoaded = false;
	}


	public void setLocation(Location location)
	{
		Badass.log("!!! setLocation: lastLatitude|lastLongitude: " + lastLatitude + "|" + lastLongitude);
		this.lastLatitude = location.getLatitude();
		this.lastLongitude = location.getLongitude();
		lastLocationUpdateInMs = BadassTimeUtils.getCurrentTimeInMs();
		sharedPreferencesFile.save();
		ThisApp.getThisAppBgndMngr().setForecast();
	}


	public double getLastLatitude()
	{
		return lastLatitude;
	}

	public double getLastLongitude()
	{
		return lastLongitude;
	}

	public boolean isAwayOfSavedLocation(Location newLocation)
	{
		boolean awayOfSavedLocation;
		if (isInvalid()) awayOfSavedLocation = true;
		else
		{
			awayOfSavedLocation = getLocationInstance().distanceTo(newLocation) >= DELTA_DISTANCE_IN_METERS;
		}
		return awayOfSavedLocation;
	}


	@Override
	public void loadData(SharedPreferences sharedPreferences)
	{
		lastLatitude = MathUtils.toDouble(sharedPreferences.getLong("lastLatitude",MathUtils.toLong(LocationUtils.INVALID_LATITUDE_VALUE)));
		lastLongitude = MathUtils.toDouble(sharedPreferences.getLong("lastLongitude",MathUtils.toLong(LocationUtils.INVALID_LONGITUDE_VALUE)));
		lastLocationUpdateInMs = sharedPreferences.getLong("lastLocationUpdateInMs", -1);
	}

	@Override
	public void saveData(SharedPreferences.Editor editor)
	{
		editor.putLong("lastLatitude",MathUtils.toLong(lastLatitude));
		editor.putLong("lastLongitude",MathUtils.toLong(lastLongitude));
		editor.putLong("lastLocationUpdateInMs", lastLocationUpdateInMs);
	}

	/**
	 * INTERNAL COOKING
	 */

	protected boolean isInvalid()
	{
		return lastLatitude == LocationUtils.INVALID_LATITUDE_VALUE;
	}


	protected Location getLocationInstance()
	{
		Location location = new Location("");
		location.setLatitude(lastLatitude);
		location.setLongitude(lastLongitude);
		return location;
	}


}
