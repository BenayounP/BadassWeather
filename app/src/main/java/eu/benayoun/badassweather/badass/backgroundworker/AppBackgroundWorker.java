package eu.benayoun.badassweather.badass.backgroundworker;

import android.location.Location;

import eu.benayoun.badass.backgroundworker.BackgroundWorker;
import eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.AppBackgroundTasksConductor;
import eu.benayoun.badassweather.badass.backgroundworker.receivers.AppAndroidEventsManager;


/**
 * Created by PierreB on 01/10/2017.
 */

public class AppBackgroundWorker extends BackgroundWorker
{
	AppBackgroundTasksConductor appBackgroundTasksConductor;
	AppAndroidEventsManager     androidEventsManager;

	public AppBackgroundWorker()
	{
		androidEventsManager = new AppAndroidEventsManager(this);
		appBackgroundTasksConductor = new AppBackgroundTasksConductor();
		setBgndTasksManager(appBackgroundTasksConductor.getBgndTasksConductor());
		launchBackgroundWork();
	}

	public AppBackgroundTasksConductor getAppBackgroundTasksConductor()
	{
		return appBackgroundTasksConductor;
	}



	public void manageFusedLocationAPI()
	{
		appBackgroundTasksConductor.manageFusedLocationAPI();
		launchBackgroundWork();
	}

	public void onFusedLocationAPIProblem()
	{
		appBackgroundTasksConductor.onFusedLocationAPIProblem();
		launchBackgroundWork();
	}

	public void onScreenOn()
	{
		launchBackgroundWork();
	}

	public void onConnectedToInternet()
	{
		// TMP
//		appBackgroundTasksConductor.onConnectedToInternet();
//		launchBackgroundWork();
	}

	public void onIdleModeOff()
	{
		launchBackgroundWork();
	}


	public void updateAllData()
	{
		appBackgroundTasksConductor.updateDataASAP();
		launchBackgroundWork();
	}

	public void updateLocation(Location lastFusedLocation)
	{
		appBackgroundTasksConductor.updateLocationASAP(lastFusedLocation);
		launchBackgroundWork();
	}
}
