package eu.benayoun.badassweather.background;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJobsCtrl;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badassweather.background.jobs.datainit.DataInitJob;
import eu.benayoun.badassweather.background.jobs.forecast.ForecastJob;
import eu.benayoun.badassweather.background.jobs.location.fusedlocationapi.FusedLocationAPIConnectionCtrl;
import eu.benayoun.badassweather.background.jobs.location.main.LocationJob;
import eu.benayoun.badassweather.background.jobs.uiupdate.UiUpdateJob;



/**
 * Created by PierreB on 01/10/2017.
 */

public class AppBadassJobList
{
	private BadassJobsCtrl badassJobsCtrl;

	//tasks
    private FusedLocationAPIConnectionCtrl fusedLocationAPIConnectionCtrl;
	private LocationJob locationJob;
	private ForecastJob forecastJob;
	private UiUpdateJob uiUpdateJob;


	public AppBadassJobList()
	{
        AppAndroidEventsCtrl appAndroidEventsCtrl = new AppAndroidEventsCtrl(this);
		Badass.listenToInternetConnectivity(appAndroidEventsCtrl);
		Badass.listenToScreenActivity(appAndroidEventsCtrl);

		badassJobsCtrl = new BadassJobsCtrl();

		badassJobsCtrl.addJob(new DataInitJob());

		fusedLocationAPIConnectionCtrl = new FusedLocationAPIConnectionCtrl();
		badassJobsCtrl.addJob(fusedLocationAPIConnectionCtrl.getBadassJob());

		locationJob = new LocationJob(this);
		badassJobsCtrl.addJob(locationJob);

		forecastJob = new ForecastJob();
		badassJobsCtrl.addJob(forecastJob);

		uiUpdateJob = new UiUpdateJob();
		badassJobsCtrl.addJob(uiUpdateJob);
	}

    public BadassJobsCtrl getBadassJobsCtrl()
    {
        return badassJobsCtrl;
    }

    public void manageFusedLocationAPI()
	{
		fusedLocationAPIConnectionCtrl.getBadassJob().askToStartAsap();
		Badass.startBadassThread();
	}

	public void onFusedLocationAPIProblem()
	{
		fusedLocationAPIConnectionCtrl.getBadassJob().askToStartAsap();
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
		forecastJob.askToStartAsap();
	}

	public void updateAllData()
	{
		locationJob.askToStartAsap();
		forecastJob.askToStartAsap();
		Badass.startBadassThread();
	}

	public void updateUI()
	{
		uiUpdateJob.askToStartAsap();
        Badass.startBadassThread();
	}


	// GETTERS
	public Location fetchLastAndroidLocation()
	{
		return fusedLocationAPIConnectionCtrl.fetchLocation();
	}


	public String getFusedLocationAPIPbString()
	{
		return fusedLocationAPIConnectionCtrl.getBadassJob().getProblemString();
	}

	public String getForecastPbString()
	{
		return forecastJob.getProblemString();
	}


	public BadassPermissionCtrl getFusedLocationAPIPermission()
	{
		return fusedLocationAPIConnectionCtrl.getBadassPermissionCtrl();
	}

	public String getLocationPbString()
	{
		return locationJob.getProblemString();
	}

	public LocationJob getLocationJob()
	{
		return locationJob;
	}

	public FusedLocationAPIConnectionCtrl getFusedLocationAPIConnectionCtrl() {
		return fusedLocationAPIConnectionCtrl;
	}
}
