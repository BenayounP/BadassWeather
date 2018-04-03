package eu.benayoun.badassweather.badass.backgroundworker.backgroundtasks.tasks.location.fusedlocationapi;

import android.Manifest;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.backgroundworker.backgroundtask.BadassBgndTaskCntrl;
import eu.benayoun.badass.backgroundworker.backgroundtask.BgndTaskCntrlManager;
import eu.benayoun.badass.utility.os.permissions.BadassPermission;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionListener;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.data.cache.bare.location.LocationCache;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 21/05/2017.
 */

public class FusedLocationAPIConnectionBgndCtrl extends BgndTaskCntrlManager implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, BadassPermissionListener
{
	protected GoogleApiClient client = null;
	protected LocationRequest         locationRequest;
	protected boolean askedForLocationUpdate = false;
	BadassPermission badassPermission;

	public FusedLocationAPIConnectionBgndCtrl()
	{
		badassPermission = Badass.getPermission(Manifest.permission.ACCESS_FINE_LOCATION,R.string.permission_location, this);
	}

	public BadassPermission getBadassPermission()
	{
		return badassPermission;
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return BadassBgndTaskCntrl.STATUS_UPDATE_ASAP;
	}

	@Override
	public int getFirstStatusEver()
	{
		return BadassBgndTaskCntrl.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		manageConnection();
	}

	// GOOGLE API CLIENT

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Badass.logInFile("** on connected to FusedLocationAPI client");
		AppBadass.getAppBackgroundWorker().manageFusedLocationAPI();
	}

	@Override
	public void onConnectionSuspended(int i)
	{
		Badass.logInFile("** on connection supended from fusedlocationAPI client!");
		AppBadass.getAppBackgroundWorker().onFusedLocationAPIProblem();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Badass.logInFile("** on connection failed from fusedlocationAPI client: " + connectionResult.toString());
		AppBadass.getAppBackgroundWorker().onFusedLocationAPIProblem();
	}

	@Override
	public void onAppPermissionResult()
	{
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT);
		badassBgndTaskCntrl.performTaskASAP();
		AppBadass.getAppBackgroundWorker().launchBackgroundWork();
	}

	/**
	 * INTERNAL COOKING
	 */

	protected void manageConnection()
	{
		if (badassPermission.isGranted())
		{
			Badass.log("##!! manageConnection badassPermission.isGranted()");
			if (AppBadass.getDataContainer().appPreferencesAndAssets.isUserHasrespondedToPermissionsDemands()==false)
			{
				AppBadass.getDataContainer().appPreferencesAndAssets.setUserHasrespondedToPermissionsDemands();
			}
			manageClientObject();
			if (client.isConnected() == false && client.isConnecting()==false)
			{
				Badass.finalLog("## connecting FusedLocationAPI client");
				AppBadass.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
				askedForLocationUpdate = false;
				client.connect();
				if (badassBgndTaskCntrl.getCurrentStatus() == BadassBgndTaskCntrl.STATUS_UPDATE_ASAP) badassBgndTaskCntrl.sleep();
		}
			else
			{
				if (client.isConnected() && askedForLocationUpdate == false)
				{
					askLocationUpdates();
					badassBgndTaskCntrl.sleep();
				}
				else if (client.isConnecting())
					Badass.finalLog("## No need to manage connection : client connecting");
				else
					Badass.finalLog("## pb in manageConnection : unknown case");
			}
		}
		else
		{
			Badass.log("##!! manageConnection badassPermission.isNOTGranted()");
			Badass.logInFile("##! " + badassBgndTaskCntrl.getName()+ " " + badassBgndTaskCntrl.getCompleteStatusString() + ":  Don't have Location permission");
			badassBgndTaskCntrl.setSpecificReasonProblemStringId(badassPermission.getExplanationStringId());
			badassBgndTaskCntrl.performTaskOnOpportunity();
		}
	}

	void manageClientObject()
	{
		if (client ==null )
		{
			client = new GoogleApiClient.Builder(Badass.getApplicationContext())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
	}


	protected void  askLocationUpdates()
	{
		locationRequest= new LocationRequest();
		locationRequest.setInterval(1);
		locationRequest.setFastestInterval(1);
		locationRequest.setSmallestDisplacement(LocationCache.DELTA_DISTANCE_IN_METERS*2);
		locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, AppBadass.getAppBackgroundWorker().getAppBackgroundTasksConductor().getLocationBgndController(), Looper.getMainLooper());
	}


}
