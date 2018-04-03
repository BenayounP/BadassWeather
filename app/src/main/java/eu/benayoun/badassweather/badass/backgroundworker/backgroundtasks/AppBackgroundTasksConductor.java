package eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.backgroundworker.backgroundtask.BgndTaskConductorListener;
import eu.benayoun.badass.backgroundworker.backgroundtask.BgndTasksConductor;
import eu.benayoun.badass.utility.os.permissions.BadassPermission;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.tasks.datainit.DataInitBgndCtrl;
import eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.tasks.location.LocationBgndCtrl;
import eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.tasks.location.fusedlocationapi.FusedLocationAPIConnectionBgndCtrl;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 25/05/2017.
 */

public class AppBackgroundTasksConductor implements BgndTaskConductorListener
{
	BgndTasksConductor                 bgndTasksConductor;
	FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionCtrl;
	LocationBgndCtrl                   locationBgndController;


	public AppBackgroundTasksConductor()
	{
		bgndTasksConductor = new BgndTasksConductor().setBgndTaskConductorListener(this);

		bgndTasksConductor.addBgndTask(new DataInitBgndCtrl());
		fusedLocationAPIConnectionCtrl = new FusedLocationAPIConnectionBgndCtrl();
		bgndTasksConductor.addBgndTask(fusedLocationAPIConnectionCtrl);
		locationBgndController = new LocationBgndCtrl(this);
		bgndTasksConductor.addBgndTask(locationBgndController);

	}



	// UPDATE

	public void updateDataASAP()
	{
		locationBgndController.performTaskASAP();
	}

	public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionCtrl.performTaskASAP();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionCtrl.onProblem();
	}


	public void updateLocationASAP(Location lastFusedLocation)
	{
		locationBgndController.setLastFusedLocation(lastFusedLocation);
		locationBgndController.performTaskASAP();
	}



	// GETTERS


	public BgndTasksConductor getBgndTasksConductor()
	{
		return bgndTasksConductor;
	}

	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionCtrl.getProblemString();
	}


	public BadassPermission getFusedLocationAPIPermission()
	{
		return fusedLocationAPIConnectionCtrl.getBadassPermission();
	}

	public String getLocationPbString()
	{
		return locationBgndController.getProblemString();
	}

	public LocationBgndCtrl getLocationBgndController()
	{
		return locationBgndController;
	}

	// LISTENER
	@Override
	public void onTaskStart()
	{
		AppBadass.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_bgnd_update_start));
	}

	@Override
	public void onTaskEnd()
	{
		AppBadass.getDataContainer().appStatusManager.updateStatus();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_COMPUTE);
	}

	/**
	 * INTERNAL COOKING
	 */

}
