package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.os.Looper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.AppBgndTask;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;


/**
 * Created by PierreB on 21/05/2017.
 */

public class FusedLocationAPIConnectionBgndTask implements AppBgndTask
{
	protected GoogleApiClient client = null;
	protected LocationRequest         locationRequest;
	protected boolean askedForLocationUpdate = false;

	FusedLocationAPIConnectionBgndMngr fusedLocationAPIConnectionBgndMngr;


	public FusedLocationAPIConnectionBgndTask(FusedLocationAPIConnectionBgndMngr fusedLocationAPIConnectionBgndMngr)
	{
		this.fusedLocationAPIConnectionBgndMngr = fusedLocationAPIConnectionBgndMngr;
	}

	@Override
	public int getOnAppInitialisationStatus()
	{
		return BgndTask.STATUS_UPDATE_ASAP;
	}

	@Override
	public int getFirstStatusEver()
	{
		return BgndTask.STATUS_UPDATE_ASAP;
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
		BgndTask bgndTask = fusedLocationAPIConnectionBgndMngr.bgndTask;
		if (fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().isGranted())
		{
			Badass.log("##!! manageConnection badassPermissionCtrl.isGranted()");
			if (ThisApp.getModel().appPreferencesAndAssets.isUserHasrespondedToPermissionsDemands()==false)
			{
				ThisApp.getModel().appPreferencesAndAssets.setUserHasrespondedToPermissionsDemands();
			}
			manageClientObject();
			if (client.isConnected() == false && client.isConnecting()==false)
			{
				Badass.finalLog("## connecting FusedLocationAPI client");
				ThisApp.getModel().appStatusCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
				askedForLocationUpdate = false;
				client.connect();

				if (bgndTask.getCurrentStatus() == BgndTask.STATUS_UPDATE_ASAP) bgndTask.sleep();
			}
			else
			{
				if (client.isConnected() && askedForLocationUpdate == false)
				{
					askLocationUpdates();
					bgndTask.sleep();
				}
				else if (client.isConnecting())
					Badass.finalLog("## No need to manage connection : client connecting");
				else
					Badass.finalLog("## pb in manageConnection : unknown case");
			}
		}
		else
		{
			Badass.log("##!! manageConnection badassPermissionCtrl.isNOTGranted()");
			Badass.logInFile("##! " + bgndTask.getName()+ " " + bgndTask.getCompleteStatusString() + ":  Don't have Location permission");
			bgndTask.setSpecificReasonProblemStringId(fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().getExplanationStringId());
			bgndTask.performTaskOnOpportunity();
		}
	}

	void manageClientObject()
	{
		if (client ==null )
		{
			client = new GoogleApiClient.Builder(Badass.getApplicationContext())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(fusedLocationAPIConnectionBgndMngr)
					.addOnConnectionFailedListener(fusedLocationAPIConnectionBgndMngr)
					.build();
		}
	}


	protected void  askLocationUpdates()
	{
		locationRequest= new LocationRequest();
		locationRequest.setInterval(1);
		locationRequest.setFastestInterval(1);
		locationRequest.setSmallestDisplacement(LocationBareCache.DELTA_DISTANCE_IN_METERS*2);
		locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, ThisApp.getThisAppBgndMngr().getLocationBgndCtrlr(), Looper.getMainLooper());
	}


}
