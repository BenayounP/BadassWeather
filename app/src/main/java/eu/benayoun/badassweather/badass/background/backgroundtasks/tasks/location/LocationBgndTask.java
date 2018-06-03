package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.location.Location;
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
	LocationBgndCtrlr locationBgndCtrlr;
	protected Location lastFusedLocation = null;

	static protected final long DEFAULT_INTERVAL = DateUtils.HOUR_IN_MILLIS;


	public LocationBgndTask(LocationBgndCtrlr locationBgndCtrlr)
	{
		this.locationBgndCtrlr = locationBgndCtrlr;
	}

	public void setLastFusedLocation(Location lastFusedLocation)
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
		updateLocation();
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
			locationBgndCtrlr.getBgndTask().waitForNextCall(BadassTimeUtils.getCurrentTimeInMs()+DEFAULT_INTERVAL);
		}
		else
		{
			Badass.log("## last location is null");
            locationBgndCtrlr.getBgndTask().onProblem();
		}
	}

	protected Location getBestLocation()
	{
		Location lastFetchedLocation  =locationBgndCtrlr.fetchFusedLocationAPILocation();
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
