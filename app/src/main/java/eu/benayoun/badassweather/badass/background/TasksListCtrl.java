package eu.benayoun.badassweather.badass.background;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.list.BadassTasksListCtrl;
import eu.benayoun.badass.background.backgroundtask.list.TasksListContract;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit.DataInitTaskWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.ForecastTaskWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationTaskWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi.FusedLocationAPIConnectionBgndCtrl;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.uiupdate.UiUpdateTaskWorker;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 01/10/2017.
 */

public class TasksListCtrl implements TasksListContract
{
	AppAndroidEventsCtrl appAndroidEventsMngr;

	BadassTasksListCtrl badassBadassTasksListCtrl;

	//tasks
	FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndCtrl;
	LocationTaskWorker locationBgndTask;
	ForecastTaskWorker forecastBgndTask;
	UiUpdateTaskWorker uiUpdateBgndTask;


	public TasksListCtrl()
	{
		appAndroidEventsMngr = new AppAndroidEventsCtrl(this);
		badassBadassTasksListCtrl = new BadassTasksListCtrl(this);

		badassBadassTasksListCtrl.addBgndTask(new DataInitTaskWorker());

		fusedLocationAPIConnectionBgndCtrl = new FusedLocationAPIConnectionBgndCtrl();
		badassBadassTasksListCtrl.addBgndTask(fusedLocationAPIConnectionBgndCtrl.getFusedLocationAPIConnectionBgndTask());
		locationBgndTask = new LocationTaskWorker(this);
		badassBadassTasksListCtrl.addBgndTask(locationBgndTask);
		forecastBgndTask = new ForecastTaskWorker();
		badassBadassTasksListCtrl.addBgndTask(forecastBgndTask);
		uiUpdateBgndTask = new UiUpdateTaskWorker();
		badassBadassTasksListCtrl.addBgndTask(uiUpdateBgndTask);


		Badass.setBgndTaskMngr(badassBadassTasksListCtrl);
		Badass.launchBackgroundTasks();
	}


	public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionBgndCtrl.getBadassBgndTask().performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionBgndCtrl.getBadassBgndTask().onProblem();
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
		forecastBgndTask.getBadassTaskCtrl().performTaskASAP();
	}

	public void updateAllData()
	{
		locationBgndTask.getBadassTaskCtrl().performTaskASAP();
		forecastBgndTask.getBadassTaskCtrl().performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	public void updateUiModel()
	{
		uiUpdateBgndTask.getBadassTaskCtrl().performTaskASAP();
	}


	// GETTERS
	public Location fetchFusedLocationAPILocation()
	{
		return fusedLocationAPIConnectionBgndCtrl.fetchLocation();
	}


	public AppAndroidEventsCtrl getAppAndroidEventsMngr()
	{
		return appAndroidEventsMngr;
	}

	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionBgndCtrl.getBadassBgndTask().getProblemString();
	}

	public String getForecastPbString()
	{
		return forecastBgndTask.getBadassTaskCtrl().getProblemString();
	}


	public BadassPermissionCtrl getFusedLocationAPIPermission()
	{
		return fusedLocationAPIConnectionBgndCtrl.getBadassPermissionCtrl();
	}

	public String getLocationPbString()
	{
		return locationBgndTask.getBadassTaskCtrl().getProblemString();
	}

	public LocationTaskWorker getLocationBgndTask()
	{
		return locationBgndTask;
	}

	public FusedLocationAPIConnectionBgndCtrl getFusedLocationAPIConnectionBgndCtrl() {
		return fusedLocationAPIConnectionBgndCtrl;
	}

	// LISTENER
	@Override
	public void onTasksListStart()
	{
		ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_bgnd_update_start));
	}

	@Override
	public void onTasksListEnd()
	{
		ThisApp.getModel().appStatusCtrl.updateStatus();
		Badass.broadcastUIEvent(UIEvents.COMPUTE);
	}

}
