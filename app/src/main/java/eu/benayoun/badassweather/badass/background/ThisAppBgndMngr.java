package eu.benayoun.badassweather.badass.background;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.AppBgndTasksCtrl;
import eu.benayoun.badass.background.backgroundtask.BadassBgndTasksMngr;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit.DataInitBgndCtrlr;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.ForecastBgndCtrl;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBgndCtrlr;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi.FusedLocationAPIConnectionBgndMngr;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.uiupdate.UiUpdateBgndCtrl;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 01/10/2017.
 */

public class ThisAppBgndMngr implements AppBgndTasksCtrl
{
	AppAndroidEventsMngr thisAppAndroidEventsMngr;

	BadassBgndTasksMngr badassBgndTasksMngr;

	//tasks
	FusedLocationAPIConnectionBgndMngr fusedLocationAPIConnectionBgndMngr;
	LocationBgndCtrlr                  locationBgndCtrlr;
	ForecastBgndCtrl forecastBgndCtrl;
	UiUpdateBgndCtrl uiUpdateBgndCtrl;


	public ThisAppBgndMngr()
	{
		thisAppAndroidEventsMngr = new AppAndroidEventsMngr(this);
		badassBgndTasksMngr = new BadassBgndTasksMngr(this);

		badassBgndTasksMngr.addBgndTask(new DataInitBgndCtrlr().getBgndTask());

		fusedLocationAPIConnectionBgndMngr = new FusedLocationAPIConnectionBgndMngr();
		badassBgndTasksMngr.addBgndTask(fusedLocationAPIConnectionBgndMngr.getBgndTask());
		locationBgndCtrlr = new LocationBgndCtrlr();
		badassBgndTasksMngr.addBgndTask(locationBgndCtrlr.getBgndTask());
		forecastBgndCtrl = new ForecastBgndCtrl();
		badassBgndTasksMngr.addBgndTask(forecastBgndCtrl.getBgndTask());
		uiUpdateBgndCtrl = new UiUpdateBgndCtrl();
		badassBgndTasksMngr.addBgndTask(uiUpdateBgndCtrl.getBgndTask());


		Badass.setBgndTaskMngr(badassBgndTasksMngr);
		Badass.launchBackgroundTasks();
	}


	public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionBgndMngr.getBgndTask().performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionBgndMngr.getBgndTask().onProblem();
		Badass.launchBackgroundTasks();
	}

	public void onScreenOn()
	{
		Badass.launchBackgroundTasks();
	}

	public void onConnectedToInternet()
	{
		// TMP
//		thisAppBgndTaskManagerListener.onConnectedToInternet();
//		launchBackgroundTasks();
	}

	public void onIdleModeOff()
	{
		Badass.launchBackgroundTasks();
	}


	public void setForecast()
	{
		forecastBgndCtrl.getBgndTask().performTaskASAP();
	}

	public void updateAllData()
	{
		locationBgndCtrlr.getBgndTask().performTaskASAP();
		forecastBgndCtrl.getBgndTask().performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	public void updateUiModel()
	{
		uiUpdateBgndCtrl.getBgndTask().performTaskASAP();
	}


	// GETTERS


	public AppAndroidEventsMngr getThisAppAndroidEventsMngr()
	{
		return thisAppAndroidEventsMngr;
	}

	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionBgndMngr.getBgndTask().getProblemString();
	}

	public String getForecastPbString()
	{
		return forecastBgndCtrl.getBgndTask().getProblemString();
	}


	public BadassPermissionCtrl getFusedLocationAPIPermission()
	{
		return fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl();
	}

	public String getLocationPbString()
	{
		return locationBgndCtrlr.getBgndTask().getProblemString();
	}

	public LocationBgndCtrlr getLocationBgndCtrlr()
	{
		return locationBgndCtrlr;
	}

	// LISTENER
	@Override
	public void onTaskStart()
	{
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_bgnd_update_start));
	}

	@Override
	public void onTaskEnd()
	{
		ThisApp.getModel().appStatusCtrl.updateStatus();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_COMPUTE);
	}

}
