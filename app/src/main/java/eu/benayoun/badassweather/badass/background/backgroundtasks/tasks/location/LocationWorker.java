package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.location.Location;
import android.text.format.DateUtils;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassBgndWorker;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.AppWorkersCtrl;



/**
 * Created by PierreB on 21/05/2017.
 */

public class LocationWorker extends BadassBgndWorker implements LocationListener
{
	static protected final long LOCATION_CHECK_INTERVAL = DateUtils.HOUR_IN_MILLIS;
	AppWorkersCtrl bgndAppWorkersCtrl;
	protected Location lastFusedLocation = null;

	public LocationWorker(AppWorkersCtrl bgndAppWorkersCtrl)
	{
		setGlobalProblemStringId(R.string.app_status_problem_location);
		this.bgndAppWorkersCtrl = bgndAppWorkersCtrl;
	}

	public void setLastFusedLocation(Location lastFusedLocation)
	{
		this.lastFusedLocation = lastFusedLocation;
	}

	@Override
	public BadassBgndWorker.Status getStartingStatus()
	{
		return Status.WORK_ASAP;
	}


	@Override
	public void work()
	{
		updateLocation();
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Badass.logInFile("** on location changed");
		setLastFusedLocation(location);
		workASAP();
		Badass.workInBackground();
	}

	/**
	 * INTERNAL COOKING
	 */


	protected void updateLocation()
	{
		ThisApp.getModel().appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
        if (ThisApp.getModel().bareModel.locationBareCache.isLoaded)
        {
            ThisApp.getModel().bareModel.locationBareCache.load();
        }

        Location bestLocation = getBestLocation();
		if (bestLocation != null)
		{
			if (ThisApp.getModel().bareModel.locationBareCache.isAwayOfSavedLocation(bestLocation))
			{
				Badass.log("## lastFusedLocation is away of last one -> setNextWorkInTheBackground currentAddress and forecast");
				ThisApp.getModel().bareModel.locationBareCache.setLocation(bestLocation);
			}
			else
			{
				Badass.log("## last location is NOT away of last one -> do nothing");
			}
			setNextWorkingSession(BadassTimeUtils.getCurrentTimeInMs()+ LOCATION_CHECK_INTERVAL);
		}
		else
		{
			Badass.log("## last location is null");
			onProblem();
		}
	}

	protected Location getBestLocation()
	{
		Location lastFetchedLocation  = bgndAppWorkersCtrl.fetchFusedLocationAPILocation();
		if (lastFusedLocation == null) return  lastFetchedLocation;
		else
		{
			if (lastFetchedLocation==null || lastFusedLocation.getTime() > lastFetchedLocation.getTime())
			{
				return lastFusedLocation;
			}
			else
			{
				return lastFetchedLocation;
			}
		}
	}
}
