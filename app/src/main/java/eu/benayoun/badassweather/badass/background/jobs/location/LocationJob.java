package eu.benayoun.badassweather.badass.background.jobs.location;

import android.location.Location;
import android.text.format.DateUtils;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.AppBadassJobList;



/**
 * Created by PierreB on 21/05/2017.
 */

public class LocationJob extends BadassJob implements LocationListener
{
	static protected final long LOCATION_CHECK_INTERVAL = DateUtils.HOUR_IN_MILLIS;
	AppBadassJobList bgndAppWorkersCtrl;
	protected Location lastFusedLocation = null;

	public LocationJob(AppBadassJobList bgndAppWorkersCtrl)
	{
		setGlobalProblemStringId(R.string.app_state_problem_location);
		this.bgndAppWorkersCtrl = bgndAppWorkersCtrl;
	}

	public void setLastFusedLocation(Location lastFusedLocation)
	{
		this.lastFusedLocation = lastFusedLocation;
	}

	@Override
	public BadassJob.State getStartingState()
	{
		return State.START_ASAP;
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
		askToStartAsap();
		Badass.startBadassThread();
	}

	/**
	 * INTERNAL COOKING
	 */


	protected void updateLocation()
	{
		ThisApp.getModel().appStateCtrl.setJobRunning(Badass.getString(R.string.app_state_getting_location));
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
				Badass.log("## last location is NOT away of last one -> update only time");
                ThisApp.getModel().bareModel.locationBareCache.updateLocationSetTime();
			}
			schedule(BadassTimeUtils.getCurrentTimeInMs()+ LOCATION_CHECK_INTERVAL);
		}
		else
		{
			Badass.log("## last location is null");
			askToResolveProblem();
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