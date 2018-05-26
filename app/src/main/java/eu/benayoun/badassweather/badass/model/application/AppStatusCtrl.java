package eu.benayoun.badassweather.badass.model.application;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.ThisAppBgndMngr;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 23/12/2017.
 */

public class AppStatusCtrl
{

	static final int STATUS_BGND_TASKS_ONGOING          =1;
	static final int STATUS_OK                          =2;
	static final int STATUS_PERMISSION_FINE_LOCATION_PB = 3;
	static final int STATUS_LOCATION_PROBLEM            = 4;
	static final int STATUS_FORECAST_PROBLEM            = 5;



	// displayed String and status
	protected String displayedString = null;
	protected int currentStatus;


	public AppStatusCtrl()
	{
		displayedString = Badass.getString(R.string.app_status_init);
		currentStatus = STATUS_BGND_TASKS_ONGOING;
	}

	// GETTERS

	public String getDisplayedString()
	{
		return displayedString;
	}

	public boolean thereIsProblem()
	{
		return currentStatus == STATUS_LOCATION_PROBLEM || currentStatus == STATUS_PERMISSION_FINE_LOCATION_PB || currentStatus == STATUS_FORECAST_PROBLEM;
	}

	public boolean thereIsFineLocationPermissionPb()
	{
		return currentStatus == STATUS_PERMISSION_FINE_LOCATION_PB;
	}



	// SETTERS

	public void setBgndTaskOngoing(String displayedStringArg)
	{
		currentStatus = STATUS_BGND_TASKS_ONGOING;
		displayedString = displayedStringArg;
		Badass.broadcastUIEvent(UIEvents.COMPUTE);
	}

	public void updateStatus()
	{
		ThisAppBgndMngr thisAppBgndMngr               = ThisApp.getThisAppBgndMngr();
		String          locationPbString              = thisAppBgndMngr.getLocationPbString();
		String          fusedLocationApiProblemString = thisAppBgndMngr.getFusedLocationAPIPbString();
		String          forecastProblemString         = thisAppBgndMngr.getForecastPbString();

		if (fusedLocationApiProblemString != null  && ThisApp.getModel().appPreferencesAndAssets.isUserDoesntwantToGiveLocationPermission()==false)
		{
			setPermissionFineLocationPB(fusedLocationApiProblemString);
			Badass.broadcastUIEvent(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
		}
		else if (locationPbString != null)
		{
			setLocationProblem(locationPbString);
		}
		else if (forecastProblemString != null)
		{
			setForecastProblem(forecastProblemString);
		}
		else
		{
			setWait(Badass.getString(R.string.app_status_next_weather_report, BadassTimeUtils.getNiceTimeString(ThisApp.getModel().bareModel.forecastBareCacheContainer.getNextWeatherReportInMs())));
		}
		Badass.broadcastUIEvent(UIEvents.APP_STATUS_CHANGE);
	}

	// ACTION
	public void onUserAction()
	{
		if (currentStatus==STATUS_PERMISSION_FINE_LOCATION_PB)
		{
			Badass.broadcastUIEvent(UIEvents.ASK_FINE_LOCATION_PERMISSION);
		}
	}

	public void onUserDismiss()
	{
		if (currentStatus==STATUS_PERMISSION_FINE_LOCATION_PB)
		{
			ThisApp.getModel().appPreferencesAndAssets.setUserDoesntwantToGiveLocationPermission(true);
			updateStatus();
		}
	}




	public void setWait(String displayedStringArg)
	{
		currentStatus = STATUS_OK;
		displayedString = displayedStringArg;
	}

	/**
	 * INTERNAL COOKING
	 */

	protected void setLocationProblem(String displayedStringArg)
	{
		currentStatus = STATUS_LOCATION_PROBLEM;
		displayedString = displayedStringArg;
	}

	protected void setPermissionFineLocationPB(String displayedStringArg)
	{
		currentStatus = STATUS_PERMISSION_FINE_LOCATION_PB;
		displayedString = displayedStringArg;
	}

	protected void setForecastProblem(String displayedStringArg)
	{
		currentStatus = STATUS_FORECAST_PROBLEM;
		displayedString = displayedStringArg;
	}



}
