package eu.benayoun.badassweather.badass.background;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.list.BadassWorkersCtrl;
import eu.benayoun.badass.background.backgroundtask.list.WorkersCtrlContract;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.datainit.DataInitWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.ForecastWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi.FusedLocationAPIConnectionBgndCtrl;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.uiupdate.UiUpdateWorker;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 01/10/2017.
 */

public class AppWorkersCtrl implements WorkersCtrlContract
{
	protected AppAndroidEventsCtrl appAndroidEventsCtrl;

	protected BadassWorkersCtrl badassWorkersCtrl;

	//tasks
	protected FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndCtrl;
	protected LocationWorker locationWorker;
	protected ForecastWorker forecastWorker;
	protected UiUpdateWorker uiUpdateWorker;


	public AppWorkersCtrl()
	{
		appAndroidEventsCtrl = new AppAndroidEventsCtrl(this);
		badassWorkersCtrl = new BadassWorkersCtrl(this);

		badassWorkersCtrl.addBgndTask(new DataInitWorker());

		fusedLocationAPIConnectionBgndCtrl = new FusedLocationAPIConnectionBgndCtrl();
		badassWorkersCtrl.addBgndTask(fusedLocationAPIConnectionBgndCtrl.getFusedLocationAPIConnectionBgndTask());
		locationWorker = new LocationWorker(this);
		badassWorkersCtrl.addBgndTask(locationWorker);
		forecastWorker = new ForecastWorker();
		badassWorkersCtrl.addBgndTask(forecastWorker);
		uiUpdateWorker = new UiUpdateWorker();
		badassWorkersCtrl.addBgndTask(uiUpdateWorker);


		Badass.setBgndTaskMngr(badassWorkersCtrl);
		Badass.workInBackground();
	}


	public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionBgndCtrl.getBadassBgndWorker().workASAP();
		Badass.workInBackground();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionBgndCtrl.getBadassBgndWorker().onProblem();
		Badass.workInBackground();
	}

	public void onScreenOn()
	{
		Badass.workInBackground();
	}

	public void onConnectedToInternet()
	{
        Badass.workInBackground();
	}



	public void setForecast()
	{
		forecastWorker.workASAP();
	}

	public void updateAllData()
	{
		locationWorker.workASAP();
		forecastWorker.workASAP();
		Badass.workInBackground();
	}

	public void updateUiModel()
	{
		uiUpdateWorker.workASAP();
	}


	// GETTERS
	public Location fetchFusedLocationAPILocation()
	{
		return fusedLocationAPIConnectionBgndCtrl.fetchLocation();
	}


	public AppAndroidEventsCtrl getAppAndroidEventsCtrl()
	{
		return appAndroidEventsCtrl;
	}

	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionBgndCtrl.getBadassBgndWorker().getProblemString();
	}

	public String getForecastPbString()
	{
		return forecastWorker.getProblemString();
	}


	public BadassPermissionCtrl getFusedLocationAPIPermission()
	{
		return fusedLocationAPIConnectionBgndCtrl.getBadassPermissionCtrl();
	}

	public String getLocationPbString()
	{
		return locationWorker.getProblemString();
	}

	public LocationWorker getLocationWorker()
	{
		return locationWorker;
	}

	public FusedLocationAPIConnectionBgndCtrl getFusedLocationAPIConnectionBgndCtrl() {
		return fusedLocationAPIConnectionBgndCtrl;
	}

	// LISTENER
	@Override
	public void onTasksListStart()
	{
		ThisApp.getModel().appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_bgnd_update_start));
	}

	@Override
	public void onTasksListEnd()
	{
		ThisApp.getModel().appStateCtrl.updateState();
		Badass.broadcastUIEvent(UIEvents.COMPUTE);
	}

}
