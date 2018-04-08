package eu.benayoun.badassweather.badass.background;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.AppBgndTasksManager;
import eu.benayoun.badass.background.backgroundtask.BadassBgndTasksManager;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionManager;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit.DataInitBgndCtrlrManager;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBgndManager;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi.FusedLocationAPIConnectionBgndManager;
import eu.benayoun.badassweather.badass.background.receivers.AppAndroidEventsManager;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 01/10/2017.
 */

public class ThisAppAppBgndManager implements AppBgndTasksManager
{
	AppAndroidEventsManager               thisAppAndroidEventsManager;

	//tasks
	BadassBgndTasksManager                badassBgndTasksManager;
	FusedLocationAPIConnectionBgndManager fusedLocationAPIConnectionBgndManager;
	LocationBgndManager                   locationBgndManager;

	public ThisAppAppBgndManager()
	{
		thisAppAndroidEventsManager = new AppAndroidEventsManager(this);
		badassBgndTasksManager = new BadassBgndTasksManager(this);

		badassBgndTasksManager.addBgndTask(new DataInitBgndCtrlrManager().getBgndTaskCntrl());
		fusedLocationAPIConnectionBgndManager = new FusedLocationAPIConnectionBgndManager();
		badassBgndTasksManager.addBgndTask(fusedLocationAPIConnectionBgndManager.getBgndTaskCntrl());
		locationBgndManager = new LocationBgndManager();
		badassBgndTasksManager.addBgndTask(locationBgndManager.getBgndTaskCntrl());

		Badass.setBgndTaskManager(badassBgndTasksManager);
		Badass.launchBackgroundOperations();
	}


	public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionBgndManager.getBgndTaskCntrl().performTaskASAP();
		Badass.launchBackgroundOperations();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionBgndManager.getBgndTaskCntrl().onProblem();
		Badass.launchBackgroundOperations();
	}

	public void onScreenOn()
	{
		Badass.launchBackgroundOperations();
	}

	public void onConnectedToInternet()
	{
		// TMP
//		thisAppBgndTaskManagerListener.onConnectedToInternet();
//		launchBackgroundOperations();
	}

	public void onIdleModeOff()
	{
		Badass.launchBackgroundOperations();
	}


	public void updateAllData()
	{
		locationBgndManager.getBgndTaskCntrl().performTaskASAP();
		Badass.launchBackgroundOperations();
	}

	public void updateLocation(Location lastFusedLocation)
	{
		locationBgndManager.setLastFusedLocation(lastFusedLocation);
		locationBgndManager.getBgndTaskCntrl().performTaskASAP();
		Badass.launchBackgroundOperations();
	}

	// GETTERS


	public BadassBgndTasksManager getBadassBgndTasksManager()
	{
		return badassBgndTasksManager;
	}

	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionBgndManager.getBgndTaskCntrl().getProblemString();
	}


	public BadassPermissionManager getFusedLocationAPIPermission()
	{
		return fusedLocationAPIConnectionBgndManager.getBadassPermissionManager();
	}

	public String getLocationPbString()
	{
		return locationBgndManager.getBgndTaskCntrl().getProblemString();
	}

	public LocationBgndManager getLocationBgndManager()
	{
		return locationBgndManager;
	}

	// LISTENER
	@Override
	public void onTaskStart()
	{
		ThisApp.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_bgnd_update_start));
	}

	@Override
	public void onTaskEnd()
	{
		ThisApp.getDataContainer().appStatusManager.updateStatus();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_COMPUTE);
	}

}
