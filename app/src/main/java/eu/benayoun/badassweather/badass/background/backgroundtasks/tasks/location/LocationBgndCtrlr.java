package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badassweather.R;

public class LocationBgndCtrlr implements LocationListener
{
	LocationBgndTask locationBgndTask;
	BgndTask         bgndTask;


	public LocationBgndCtrlr()
	{
		locationBgndTask = new LocationBgndTask(this);
		bgndTask = new BgndTask(locationBgndTask);
		bgndTask.setGlobalProblemStringId(R.string.app_status_problem_location);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Badass.logInFile("** on location changed");
		locationBgndTask.onLastFusedLocationChange(location);
		bgndTask.performTaskASAP();
		Badass.launchBackgroundTasks();
	}


	public BgndTask getBgndTask()
	{
		return bgndTask;
	}

	/**
	 * INTERNAL COOKING
	 */


}
