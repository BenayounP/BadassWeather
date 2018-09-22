package eu.benayoun.badassweather.badass.background;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJobsCtrl;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJobListContract;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.datainit.DataInitWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.forecast.ForecastWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.location.LocationWorker;
import eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.location.fusedlocationapi.FusedLocationAPIConnectionBgndCtrl;
import eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.uiupdate.UiUpdateWorker;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 01/10/2017.
 */

public class AppBadassJobList implements BadassJobListContract
{
	protected AppAndroidEventsCtrl appAndroidEventsCtrl;

	protected BadassJobsCtrl badassJobsCtrl;

	//tasks
	protected FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndCtrl;
	protected LocationWorker locationWorker;
	protected ForecastWorker forecastWorker;
	protected UiUpdateWorker uiUpdateWorker;


	public AppBadassJobList()
	{
		appAndroidEventsCtrl = new AppAndroidEventsCtrl(this);
		badassJobsCtrl = new BadassJobsCtrl(this);

		badassJobsCtrl.addJob(new DataInitWorker());

		fusedLocationAPIConnectionBgndCtrl = new FusedLocationAPIConnectionBgndCtrl();
		badassJobsCtrl.addJob(fusedLocationAPIConnectionBgndCtrl.getFusedLocationAPIConnectionJob());
		locationWorker = new LocationWorker(this);
		badassJobsCtrl.addJob(locationWorker);
		forecastWorker = new ForecastWorker();
		badassJobsCtrl.addJob(forecastWorker);
		uiUpdateWorker = new UiUpdateWorker();
		badassJobsCtrl.addJob(uiUpdateWorker);


		Badass.setJobCtrl(badassJobsCtrl);
		Badass.startBadassThread();
	}


	public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionBgndCtrl.getBadassJob().askToStartAsap();
		Badass.startBadassThread();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionBgndCtrl.getBadassJob().askToStartAsap();
        Badass.startBadassThread();
	}

	public void onScreenOn()
	{
		Badass.startBadassThread();
	}

	public void onConnectedToInternet()
	{
        Badass.startBadassThread();
	}



	public void setForecast()
	{
		forecastWorker.askToStartAsap();
	}

	public void updateAllData()
	{
		locationWorker.askToStartAsap();
		forecastWorker.askToStartAsap();
		Badass.startBadassThread();
	}

	public void updateUI()
	{
		uiUpdateWorker.askToStartAsap();
        Badass.startBadassThread();
	}


	// GETTERS
	public Location fetchFusedLocationAPILocation()
	{
		return fusedLocationAPIConnectionBgndCtrl.fetchLocation();
	}


	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionBgndCtrl.getBadassJob().getProblemString();
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
	public void onJobListStart()
	{
		ThisApp.getModel().appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_bgnd_update_start));
	}

	@Override
	public void onJobListEnd()
	{
		ThisApp.getModel().appStateCtrl.updateState();
		Badass.broadcastUIEvent(UIEvents.COMPUTE);
	}

}
