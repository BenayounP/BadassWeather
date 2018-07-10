package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.location.Location;
import android.text.format.DateUtils;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.background.backgroundtask.tasks.TaskWorkerContract;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.TasksListCtrl;

import static eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl.Status.UPDATE_ASAP;


/**
 * Created by PierreB on 21/05/2017.
 */

public class LocationTaskWorker implements TaskWorkerContract, LocationListener
{
	static protected final long LOCATION_CHECK_INTERVAL = DateUtils.HOUR_IN_MILLIS;

	BadassTaskCtrl badassTaskCtrl;
	TasksListCtrl bgndTasksListCtrl;
	protected Location lastFusedLocation = null;



	public LocationTaskWorker(TasksListCtrl bgndTasksListCtrl)
	{
		badassTaskCtrl = new BadassTaskCtrl(this);
		badassTaskCtrl.setGlobalProblemStringId(R.string.app_status_problem_location);
		this.bgndTasksListCtrl = bgndTasksListCtrl;
	}

	public void setLastFusedLocation(Location lastFusedLocation)
	{
		this.lastFusedLocation = lastFusedLocation;
	}

	@Override
	public BadassTaskCtrl.Status getStartingStatus()
	{
		return UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		updateLocation();
	}

	@Override
	public BadassTaskCtrl getBadassTaskCtrl()
	{
		return badassTaskCtrl;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Badass.logInFile("** on location changed");
		setLastFusedLocation(location);
		getBadassTaskCtrl().performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	/**
	 * INTERNAL COOKING
	 */


	protected void updateLocation()
	{
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
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
			badassTaskCtrl.waitForNextCall(BadassTimeUtils.getCurrentTimeInMs()+ LOCATION_CHECK_INTERVAL);
		}
		else
		{
			Badass.log("## last location is null");
			badassTaskCtrl.onProblem();
		}
	}

	protected Location getBestLocation()
	{
		Location lastFetchedLocation  = bgndTasksListCtrl.fetchFusedLocationAPILocation();
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
