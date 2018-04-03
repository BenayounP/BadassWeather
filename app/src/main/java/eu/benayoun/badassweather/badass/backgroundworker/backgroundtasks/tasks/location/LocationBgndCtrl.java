package eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.tasks.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.backgroundworker.backgroundtask.BadassBgndTaskCntrl;
import eu.benayoun.badass.backgroundworker.backgroundtask.BgndTaskCntrlManager;
import eu.benayoun.badass.utility.os.time.TimeCST;
import eu.benayoun.badass.utility.os.time.TimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.AppBackgroundTasksConductor;


/**
 * Created by PierreB on 21/05/2017.
 */

public class LocationBgndCtrl extends BgndTaskCntrlManager implements LocationListener
{
	protected static long TYPICAL_DELAY= 2*TimeCST.HOUR_IN_MS;


	protected AppBackgroundTasksConductor appBackgroundTasksConductor;
	protected Location lastFusedLocation = null;

	public LocationBgndCtrl(AppBackgroundTasksConductor appBackgroundTasksConductor)
	{
		badassBgndTaskCntrl.setGlobalProblemStringId(R.string.app_status_problem_location);
		this.appBackgroundTasksConductor = appBackgroundTasksConductor;
	}

	public void setLastFusedLocation(Location lastFusedLocation)
	{
		this.lastFusedLocation = lastFusedLocation;
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return badassBgndTaskCntrl.getCurrentStatus();
	}

	@Override
	public int getFirstStatusEver()
	{
		return BadassBgndTaskCntrl.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		getLocation();
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Badass.logInFile("** on location changed");
		setLastFusedLocation(lastFusedLocation);
		badassBgndTaskCntrl.performTaskASAP();
		AppBadass.getAppBackgroundWorker().launchBackgroundWork();
	}



	/**
	 * INTERNAL COOKING
	 */



	protected void getLocation()
	{
		AppBadass.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
		Location lastLocation = getBestLocation();

		if (lastLocation != null)
		{
			double lastLatitude  = lastLocation.getLatitude();
			double lastLongitude = lastLocation.getLongitude();

			if (AppBadass.getDataContainer().bareDataContainer.locationCache.isAwayOfSavedLocation(lastLocation))
			{
				Badass.log("## last location is away of last one -> setNextWorkInTheBackground currentAddress and forecast");
				AppBadass.getDataContainer().bareDataContainer.locationCache.setLocation(lastLatitude, lastLongitude);
			}
			else
			{
				Badass.log("## last location is NOT away of last one -> do nothing");
			}
		}
		else
		{
			Badass.log("## last location is null");
		}
	}


	protected Location getBestLocation()
	{
		boolean bestLocationIsFused = false;
		Location bestLocation = null;
		long bestTimeInMs = -1;
		LocationManager locationManager = (LocationManager)Badass.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		Location locationByNetwork = getLocationByNetwork(locationManager);
		Location locationByGPS = getLocationByGPS(locationManager);

		if (lastFusedLocation!=null)
		{
			bestLocation = lastFusedLocation;
			bestTimeInMs = bestLocation.getTime();
			bestLocationIsFused = true;
			Badass.logInFile("## best location: fusedLocation");
		}
		if (locationByNetwork!=null && locationByNetwork.getTime()>= bestTimeInMs)
		{
			bestLocation = locationByNetwork;
			bestTimeInMs = bestLocation.getTime();
			Badass.logInFile("## best location: locationByNetwork");
		}
		if (locationByGPS!=null && locationByGPS.getTime()>= bestTimeInMs)
		{
			bestLocation = locationByGPS;
			Badass.logInFile("## best location: locationByGPS");
		}

		if (bestLocation!=null)
		{
			if (bestLocationIsFused) badassBgndTaskCntrl.sleep();
			else badassBgndTaskCntrl.waitForNextCall(TimeUtils.getCurrentTimeInMs()+ TYPICAL_DELAY);
		}
		else
		{
			Badass.logInFile("## LocationBgndCtrl: location is null");
			badassBgndTaskCntrl.setSpecificReasonProblemStringId(R.string.app_status_problem_location_null);
			badassBgndTaskCntrl.onProblem();
		}
		return bestLocation;
	}

	protected Location getLocationByNetwork(LocationManager locationManager)
	{
		Location lastLocation = null;


		if (locationManager!=null)
		{
			if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			{
				lastLocation = getLastLocation(LocationManager.NETWORK_PROVIDER,locationManager);
			}
		}
		return lastLocation;
	}

	protected Location getLocationByGPS(LocationManager locationManager)
	{
		Location lastLocation = null;
		if (locationManager!=null)
		{
			if (lastLocation == null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				lastLocation = getLastLocation(LocationManager.GPS_PROVIDER,locationManager);
			}
		}
		return lastLocation;
	}

	protected Location getLastLocation(String provider,LocationManager locationManager)
	{
		Location lastLocation=null;
		// try/catch MANDATORY to avoid error
		try
		{
			lastLocation = locationManager.getLastKnownLocation(provider);
		} catch (SecurityException e)
		{
			Badass.log("SecurityException: " + e.toString());
		}
		return lastLocation;
	}
}
