package eu.benayoun.badassweather.badass.background.jobs.location.fusedlocationapi;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.jobs.location.main.LocationBareCache;



/**
 * Created by PierreB on 21/05/2017.
 */

public class FusedLocationAPIConnectionJob extends BadassJob
{
	protected GoogleApiClient client = null;
	protected boolean askedForLocationUpdate = false;

	FusedLocationAPIConnectionCtrl fusedLocationAPIConnectionBgndMngr;


	public FusedLocationAPIConnectionJob(FusedLocationAPIConnectionCtrl fusedLocationAPIConnectionBgndMngr)
	{
		this.fusedLocationAPIConnectionBgndMngr = fusedLocationAPIConnectionBgndMngr;
	}

	@Override
	public BadassJob.State getStartingState()
	{
		return State.START_ASAP;
	}


	@Override
	public void work()
	{
		manageConnection();
	}


	// GOOGLE API CLIENT


	@SuppressLint("MissingPermission")
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
		if (fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().isPermissionGranted())
		{
			if (ThisApp.getModel().appPreferencesAndAssets.isUserHasrespondedToPermissionsDemands()==false)
			{
				ThisApp.getModel().appPreferencesAndAssets.setUserHasrespondedToPermissionsDemands();
			}
			manageClientObject();
			if (client.isConnected() == false && client.isConnecting()==false)
			{
				Badass.finalLog("## connecting FusedLocationAPI client");
				ThisApp.getModel().appStateCtrl.setJobRunning(Badass.getString(R.string.app_state_connect_api));
				askedForLocationUpdate = false;
				client.connect();

				if (state == State.START_ASAP) goToSleep();
			}
			else
			{
				if (client.isConnected() && askedForLocationUpdate == false)
				{
					askLocationUpdates();
					goToSleep();
				}
				else if (client.isConnecting())
					Badass.finalLog("## No need to manage connection : client connecting");
				else
					Badass.finalLog("## pb in manageLocationUpdates : unknown case");
			}
		}
		else // No permission
		{
			Badass.log("##!! manageLocationUpdates badassPermissionCtrl.isNOTGranted()");
			Badass.logInFile("##! " + getName()+ " " + getCompleteStatusString() + ":  Don't have Location permission");
			setSpecificProblemStringId(fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().getExplanationStringId());
			prepareToStartAtNextCall();
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


	@SuppressLint("MissingPermission")
    protected void  askLocationUpdates()
	{
		LocationRequest locationRequest= new LocationRequest();
		locationRequest.setInterval(1);
		locationRequest.setFastestInterval(1);
		locationRequest.setSmallestDisplacement(LocationBareCache.DELTA_DISTANCE_IN_METERS*2);
		locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, ThisApp.getAppBadassJobList().getLocationJob(), Looper.getMainLooper());
	}


}
