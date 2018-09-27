package eu.benayoun.badassweather.badass.background;

import android.location.Location;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJobsCtrl;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badassweather.badass.background.jobs.datainit.DataInitJob;
import eu.benayoun.badassweather.badass.background.jobs.forecast.ForecastJob;
import eu.benayoun.badassweather.badass.background.jobs.location.LocationJob;
import eu.benayoun.badassweather.badass.background.jobs.location.fusedlocationapi.FusedLocationAPIConnectionCtrl;
import eu.benayoun.badassweather.badass.background.jobs.uiupdate.UiUpdateJob;


/**
 * Created by PierreB on 01/10/2017.
 */

public class AppBadassJobList
{
	protected AppAndroidEventsCtrl appAndroidEventsCtrl;

	protected BadassJobsCtrl badassJobsCtrl;

	//tasks
	protected FusedLocationAPIConnectionCtrl fusedLocationAPIConnectionCtrl;
	protected LocationJob locationJob;
	protected ForecastJob forecastJob;
	protected UiUpdateJob uiUpdateJob;


	public AppBadassJobList()
	{
		appAndroidEventsCtrl = new AppAndroidEventsCtrl(this);
		badassJobsCtrl = new BadassJobsCtrl();

		badassJobsCtrl.addJob(new DataInitJob());

		fusedLocationAPIConnectionCtrl = new FusedLocationAPIConnectionCtrl();
		badassJobsCtrl.addJob(fusedLocationAPIConnectionCtrl.getFusedLocationAPIConnectionJob());
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
	public Location fetchFusedLocationAPILocation()
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
