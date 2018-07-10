package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.content.SharedPreferences;
import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.cache.SharedPreferencesFile;
import eu.benayoun.badass.utility.cache.SharedPreferencesSubCacheContract;
import eu.benayoun.badass.utility.math.MathUtils;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.ThisApp;


/**
 * Created by PierreB on 22/03/2017.
 */

public class LocationBareCache implements SharedPreferencesSubCacheContract
{
	static public final double INVALID_LATITUDE_VALUE = -100d;
	static public final double INVALID_LONGITUDE_VALUE = -200d;

	public static float DELTA_DISTANCE_IN_METERS = 5000;

	protected SharedPreferencesFile sharedPreferencesFile;

	protected double   lastLatitude;
	protected double   lastLongitude;
	protected long     lastLocationUpdateInMs;

	protected boolean isLoaded=false;


	public LocationBareCache()
	{
		sharedPreferencesFile = new SharedPreferencesFile(Badass.getSimpleClassName(),this);
		lastLatitude = INVALID_LATITUDE_VALUE;
		lastLongitude =INVALID_LONGITUDE_VALUE;
		lastLocationUpdateInMs=-1;
	}

	public void load()
	{
		sharedPreferencesFile.load();
		isLoaded = false;
	}


	public void setLocation(Location location)
	{
		this.lastLatitude = location.getLatitude();
		this.lastLongitude = location.getLongitude();
		lastLocationUpdateInMs = BadassTimeUtils.getCurrentTimeInMs();
		sharedPreferencesFile.save();
		ThisApp.getBgndTaskCtrl().setForecast();
	}


	public double getLastLatitude()
	{
		return lastLatitude;
	}

	public double getLastLongitude()
	{
		return lastLongitude;
	}

	public boolean isValid()
	{
		return lastLatitude!=INVALID_LATITUDE_VALUE && lastLongitude!=INVALID_LONGITUDE_VALUE;
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
	public void load(SharedPreferences sharedPreferences)
	{
		lastLatitude = MathUtils.toDouble(sharedPreferences.getLong("lastLatitude",MathUtils.toLong(INVALID_LATITUDE_VALUE)));
		lastLongitude = MathUtils.toDouble(sharedPreferences.getLong("lastLongitude",MathUtils.toLong(INVALID_LONGITUDE_VALUE)));
		lastLocationUpdateInMs = sharedPreferences.getLong("lastLocationUpdateInMs", -1);
	}

	@Override
	public void save(SharedPreferences.Editor editor)
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
		return lastLatitude == INVALID_LATITUDE_VALUE;
	}


	protected Location getLocationInstance()
	{
		Location location = new Location("");
		location.setLatitude(lastLatitude);
		location.setLongitude(lastLongitude);
		return location;
	}


}
