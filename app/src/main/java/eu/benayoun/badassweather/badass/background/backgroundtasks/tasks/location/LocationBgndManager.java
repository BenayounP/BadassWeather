package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTaskCntrl;
import eu.benayoun.badassweather.R;

public class LocationBgndManager implements LocationListener
{

	LocationBgndCtrl locationBgndCtrl;
	BgndTaskCntrl bgndTaskCntrl;


	public LocationBgndManager()
	{
		locationBgndCtrl = new LocationBgndCtrl(this);
		bgndTaskCntrl = new BgndTaskCntrl(locationBgndCtrl);
		bgndTaskCntrl.setGlobalProblemStringId(R.string.app_status_problem_location);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Badass.logInFile("** on location changed");
		locationBgndCtrl.setLastFusedLocation(location);
		bgndTaskCntrl.performTaskASAP();
		Badass.launchBackgroundOperations();
	}


	public BgndTaskCntrl getBgndTaskCntrl()
	{
		return bgndTaskCntrl;
	}

	public void setLastFusedLocation(Location lastFusedLocation)
	{
		locationBgndCtrl.setLastFusedLocation(lastFusedLocation);
	}

	/**
	 * INTERNAL COOKING
	 */


}
