package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.background.ThisAppBgndMngr;

public class LocationBgndCtrlr implements LocationListener
{
	LocationBgndTask locationBgndTask;
	BgndTask         bgndTask;
    ThisAppBgndMngr thisAppBgndMngr;




	public LocationBgndCtrlr(ThisAppBgndMngr thisAppBgndMngr)
	{
	this.thisAppBgndMngr = thisAppBgndMngr;
	    locationBgndTask = new LocationBgndTask(this);
		bgndTask = new BgndTask(locationBgndTask);
		bgndTask.setGlobalProblemStringId(R.string.app_status_problem_location);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Badass.logInFile("** on location changed");
		locationBgndTask.setLastFusedLocation(location);
		bgndTask.performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	public BgndTask getBgndTask()
	{
		return bgndTask;
	}

	protected Location fetchFusedLocationAPILocation()
    {
        return thisAppBgndMngr.fetchFusedLocationAPILocation();
    }

	/**
	 * INTERNAL COOKING
	 */


}
