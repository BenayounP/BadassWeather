package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.location.Location;
import android.os.Looper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.background.backgroundtask.tasks.TaskWorkerContract;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;

import static eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl.Status.UPDATE_ASAP;


/**
 * Created by PierreB on 21/05/2017.
 */

public class FusedLocationAPIConnectionTaskWorker implements TaskWorkerContract
{
	protected BadassTaskCtrl badassTaskCtrl;
	protected GoogleApiClient client = null;
	protected LocationRequest         locationRequest;
	protected boolean askedForLocationUpdate = false;

	FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndMngr;


	public FusedLocationAPIConnectionTaskWorker(FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndMngr)
	{
        badassTaskCtrl = new BadassTaskCtrl(this);
		this.fusedLocationAPIConnectionBgndMngr = fusedLocationAPIConnectionBgndMngr;
	}

	@Override
	public BadassTaskCtrl.Status getStartingStatus()
	{
		return UPDATE_ASAP;
	}


	@Override
	public void performBgndTask()
	{
		manageConnection();
	}

	@Override
	public BadassTaskCtrl getBadassTaskCtrl()
	{
		return badassTaskCtrl;
	}

	// GOOGLE API CLIENT


	public Location fetchLocation()
	{
		if (client !=null)
		{
			return LocationServices.FusedLocationApi.getLastLocation(client);
		}
		else return null;

	}

	/**
	 * INTERNAL COOKING
	 */

	protected void manageConnection()
	{
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

				if (badassTaskCtrl.getStatus() == UPDATE_ASAP) badassTaskCtrl.sleep();
			}
			else
			{
				if (client.isConnected() && askedForLocationUpdate == false)
				{
					askLocationUpdates();
					badassTaskCtrl.sleep();
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
			Badass.logInFile("##! " + badassTaskCtrl.getName()+ " " + badassTaskCtrl.getCompleteStatusString() + ":  Don't have Location permission");
			badassTaskCtrl.setSpecificReasonProblemStringId(fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().getExplanationStringId());
			badassTaskCtrl.performTaskOnOpportunity();
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
		LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, ThisApp.getBgndTaskCtrl().getLocationBgndTask(), Looper.getMainLooper());
	}


}
