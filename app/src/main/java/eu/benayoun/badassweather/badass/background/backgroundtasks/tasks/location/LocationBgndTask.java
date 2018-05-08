package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.text.format.DateUtils;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.AppBgndTask;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;


/**
 * Created by PierreB on 21/05/2017.
 */

public class LocationBgndTask implements AppBgndTask
{
	protected static long TYPICAL_DELAY= 2* DateUtils.HOUR_IN_MILLIS;

	LocationBgndCtrlr locationBgndCtrlr;
	protected Location lastFusedLocation = null;


	public LocationBgndTask(LocationBgndCtrlr locationBgndCtrlr)
	{
		this.locationBgndCtrlr = locationBgndCtrlr;
	}

	void onLastFusedLocationChange(Location lastFusedLocation)
	{
		this.lastFusedLocation = lastFusedLocation;
	}


	@Override
	public int getOnAppInitialisationStatus()
	{
		return locationBgndCtrlr.getBgndTask().getCurrentStatus();
	}

	@Override
	public int getFirstStatusEver()
	{
		return BgndTask.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		loadIfNecessary();
		updateLocation();
	}




	/**
	 * INTERNAL COOKING
	 */

	protected void loadIfNecessary()
	{
		if (ThisApp.getModel().bareModel.locationBareCache.isLoaded)
		{
			ThisApp.getModel().bareModel.locationBareCache.load();
		}
	}



	protected void updateLocation()
	{
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
		Location bestLastLocation = getBestLastLocation();

		if (bestLastLocation != null)
		{
			double lastLatitude  = bestLastLocation.getLatitude();
			double lastLongitude = bestLastLocation.getLongitude();

			if (ThisApp.getModel().bareModel.locationBareCache.isAwayOfSavedLocation(bestLastLocation))
			{
				Badass.log("## last location is away of last one -> setNextWorkInTheBackground currentAddress and forecast");
				ThisApp.getModel().bareModel.locationBareCache.setLocation(lastLatitude, lastLongitude);
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


	protected Location getBestLastLocation()
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
			if (bestLocationIsFused) locationBgndCtrlr.getBgndTask().sleep();
			else locationBgndCtrlr.getBgndTask().waitForNextCall(BadassTimeUtils.getCurrentTimeInMs()+ TYPICAL_DELAY);
		}
		else
		{
			Badass.logInFile("## LocationBgndTask: location is null");
			locationBgndCtrlr.getBgndTask().setSpecificReasonProblemStringId(R.string.app_status_problem_location_null);
			locationBgndCtrlr.getBgndTask().onProblem();
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
