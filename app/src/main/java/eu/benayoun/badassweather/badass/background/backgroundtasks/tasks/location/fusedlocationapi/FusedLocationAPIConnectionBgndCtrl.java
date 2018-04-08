package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.os.Looper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.AppBgndTaskCntrl;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTaskCntrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.data.cache.bare.location.LocationCache;


/**
 * Created by PierreB on 21/05/2017.
 */

public class FusedLocationAPIConnectionBgndCtrl implements AppBgndTaskCntrl
{
	protected GoogleApiClient client = null;
	protected LocationRequest         locationRequest;
	protected boolean askedForLocationUpdate = false;

	FusedLocationAPIConnectionBgndManager fusedLocationAPIConnectionBgndManager;


	public FusedLocationAPIConnectionBgndCtrl(FusedLocationAPIConnectionBgndManager fusedLocationAPIConnectionBgndManager)
	{
		this.fusedLocationAPIConnectionBgndManager = fusedLocationAPIConnectionBgndManager;
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return BgndTaskCntrl.STATUS_UPDATE_ASAP;
	}

	@Override
	public int getFirstStatusEver()
	{
		return BgndTaskCntrl.STATUS_UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		manageConnection();
	}

	// GOOGLE API CLIENT



	/**
	 * INTERNAL COOKING
	 */

	protected void manageConnection()
	{
		BgndTaskCntrl bgndTaskCntrl = fusedLocationAPIConnectionBgndManager.bgndTaskCntrl;
		if (fusedLocationAPIConnectionBgndManager.getBadassPermissionManager().isGranted())
		{
			Badass.log("##!! manageConnection badassPermissionManager.isGranted()");
			if (ThisApp.getDataContainer().appPreferencesAndAssets.isUserHasrespondedToPermissionsDemands()==false)
			{
				ThisApp.getDataContainer().appPreferencesAndAssets.setUserHasrespondedToPermissionsDemands();
			}
			manageClientObject();
			if (client.isConnected() == false && client.isConnecting()==false)
			{
				Badass.finalLog("## connecting FusedLocationAPI client");
				ThisApp.getDataContainer().appStatusManager.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
				askedForLocationUpdate = false;
				client.connect();

				if (bgndTaskCntrl.getCurrentStatus() == BgndTaskCntrl.STATUS_UPDATE_ASAP) bgndTaskCntrl.sleep();
			}
			else
			{
				if (client.isConnected() && askedForLocationUpdate == false)
				{
					askLocationUpdates();
					bgndTaskCntrl.sleep();
				}
				else if (client.isConnecting())
					Badass.finalLog("## No need to manage connection : client connecting");
				else
					Badass.finalLog("## pb in manageConnection : unknown case");
			}
		}
		else
		{
			Badass.log("##!! manageConnection badassPermissionManager.isNOTGranted()");
			Badass.logInFile("##! " + bgndTaskCntrl.getName()+ " " + bgndTaskCntrl.getCompleteStatusString() + ":  Don't have Location permission");
			bgndTaskCntrl.setSpecificReasonProblemStringId(fusedLocationAPIConnectionBgndManager.getBadassPermissionManager().getExplanationStringId());
			bgndTaskCntrl.performTaskOnOpportunity();
		}
	}

	void manageClientObject()
	{
		if (client ==null )
		{
			client = new GoogleApiClient.Builder(Badass.getApplicationContext())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(fusedLocationAPIConnectionBgndManager)
					.addOnConnectionFailedListener(fusedLocationAPIConnectionBgndManager)
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
		LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, ThisApp.getThisAppBgndManager().getLocationBgndManager(), Looper.getMainLooper());
	}


}
