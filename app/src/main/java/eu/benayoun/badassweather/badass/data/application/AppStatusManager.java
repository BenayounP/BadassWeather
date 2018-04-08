package eu.benayoun.badassweather.badass.data.application;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 23/12/2017.
 */

public class AppStatusManager
{

	static final int STATUS_BGND_TASKS_ONGOING =1;
	static final int STATUS_OK                 =2;
	static final int STATUS_LOCATION_PROBLEM   = 3;
	static final int STATUS_PERMISSION_FINE_LOCATION_PB                   = 4;

	// displayed String and status
	protected String displayedString = null;
	protected int currentStatus;


	public AppStatusManager()
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
		return currentStatus == STATUS_LOCATION_PROBLEM || currentStatus == STATUS_PERMISSION_FINE_LOCATION_PB;
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
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_COMPUTE);
	}

	public void updateStatus()
	{
		String locationPbString = ThisApp.getThisAppBgndManager().getLocationPbString();
		String fusedLocationApiProblemString = ThisApp.getThisAppBgndManager().getFusedLocationAPIPbString();

		if (fusedLocationApiProblemString != null  && ThisApp.getDataContainer().appPreferencesAndAssets.isUserDoesntwantToGiveLocationPermission()==false)
		{
			setPermissionFineLocationPB(fusedLocationApiProblemString);
			Badass.broadcastUIEvent(UIEvents.UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT);
		}
		else if (locationPbString != null)
		{
			setLocationProblem(locationPbString);
		}
		else
		{
			setWait(Badass.getString(R.string.app_status_next_weather_report,"STATUS OK"));
		}
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_APP_STATUS_CHANGE);
	}

	// ACTION
	public void onUserAction()
	{
		if (currentStatus==STATUS_PERMISSION_FINE_LOCATION_PB)
		{
			Badass.broadcastUIEvent(UIEvents.UI_EVENT_ASK_FINE_LOCATION_PERMISSION);
		}
	}

	public void onUserDismiss()
	{
		if (currentStatus==STATUS_PERMISSION_FINE_LOCATION_PB)
		{
			ThisApp.getDataContainer().appPreferencesAndAssets.setUserDoesntwantToGiveLocationPermission(true);
			updateStatus();
		}
	}

	/**
	 * INTERNAL COOKING
	 */

	public void setLocationProblem(String displayedStringArg)
	{
		currentStatus = STATUS_LOCATION_PROBLEM;
		displayedString = displayedStringArg;
	}

	public void setPermissionFineLocationPB(String displayedStringArg)
	{
		currentStatus = STATUS_PERMISSION_FINE_LOCATION_PB;
		displayedString = displayedStringArg;
	}


	public void setWait(String displayedStringArg)
	{
		currentStatus = STATUS_OK;
		displayedString = displayedStringArg;
	}



}
