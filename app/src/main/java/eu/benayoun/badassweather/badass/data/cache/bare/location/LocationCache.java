package eu.benayoun.badassweather.badass.data.cache.bare.location;

import android.content.SharedPreferences;
import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.cache.SharedPreferencesFile;
import eu.benayoun.badass.utility.cache.SharedPreferencesSubCache;
import eu.benayoun.badass.utility.os.time.TimeUtils;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 22/03/2017.
 */

public class LocationCache implements SharedPreferencesSubCache
{
	static public final double INVALID_LATITUDE_VALUE = -100d;
	static public final double INVALID_LONGITUDE_VALUE = -200d;

	public static float DELTA_DISTANCE_IN_METERS = 5000;

	protected SharedPreferencesFile sharedPreferencesFile;

	protected double   lastLatitude;
	protected double   lastLongitude;
	protected long     lastLocationUpdateInMs;
	protected long     lastAddressUpdateInMs;
	protected Location lastLocation;

	public LocationCache()
	{
		sharedPreferencesFile = new SharedPreferencesFile(Badass.getSimpleClassName(),this);
		lastLatitude = INVALID_LATITUDE_VALUE;
		lastLongitude = INVALID_LONGITUDE_VALUE;
		lastLocationUpdateInMs=-1;
		lastAddressUpdateInMs=-1;
		lastLocation = null;
	}

	public void load()
	{
		sharedPreferencesFile.load();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_LOCATION_CHANGE);
	}


	public void setLocation(double lastLatitude, double lastLongitude)
	{
		this.lastLatitude = lastLatitude;
		this.lastLongitude = lastLongitude;
		lastLocationUpdateInMs = TimeUtils.getCurrentTimeInMs();
		lastLocation= getLocationInstance();
		sharedPreferencesFile.save();
	}

	public boolean isEmpty()
	{
		return lastLongitude==INVALID_LONGITUDE_VALUE;
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
		if (lastLocation==null) awayOfSavedLocation = true;
		else
		{
			awayOfSavedLocation = lastLocation.distanceTo(newLocation) >= DELTA_DISTANCE_IN_METERS;
		}
		return awayOfSavedLocation;
	}

	public long getLastLocationUpdateInMs()
	{
		return lastLocationUpdateInMs;
	}

	public long getLastAddressUpdateInMs()
	{
		return lastAddressUpdateInMs;
	}

	@Override
	public void loadData(SharedPreferences sharedPreferences)
	{
		lastLatitude = toDouble(sharedPreferences.getLong("lastLatitude",toLong(INVALID_LATITUDE_VALUE)));
		lastLongitude = toDouble(sharedPreferences.getLong("lastLongitude",toLong(INVALID_LONGITUDE_VALUE)));
		lastLocationUpdateInMs = sharedPreferences.getLong("lastLocationUpdateInMs", -1);
		lastAddressUpdateInMs = sharedPreferences.getLong("lastAddressUpdateInMs", -1);
		lastLocation = getLocationInstance();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_LOCATION_CHANGE);
	}

	@Override
	public void saveData(SharedPreferences.Editor editor)
	{
		editor.putLong("lastLatitude",toLong(lastLatitude));
		editor.putLong("lastLongitude",toLong(lastLongitude));
		editor.putLong("lastLocationUpdateInMs", lastLocationUpdateInMs);
		editor.putLong("lastAddressUpdateInMs", lastAddressUpdateInMs);
	}

	/**
	 * INTERNAL COOKING
	 */

	protected Location getLocationInstance()
	{
		Location lastLocation = new Location("");
		lastLocation.setLatitude(lastLatitude);
		lastLocation.setLongitude(lastLongitude);
		lastLocation.setTime(lastLocationUpdateInMs);
		return lastLocation;
	}

	protected long toLong(double doubleValue)
	{
		return Double.doubleToRawLongBits(doubleValue);
	}

	protected double toDouble(long longValue)
	{
		return Double.longBitsToDouble(longValue);
	}


}
