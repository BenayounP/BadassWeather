package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.location.Location;
import android.os.Looper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassBgndWorker;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;



/**
 * Created by PierreB on 21/05/2017.
 */

public class FusedLocationAPIConnectionWorker extends BadassBgndWorker
{
	protected GoogleApiClient client = null;
	protected LocationRequest         locationRequest;
	protected boolean askedForLocationUpdate = false;

	FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndMngr;


	public FusedLocationAPIConnectionWorker(FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndMngr)
	{
		this.fusedLocationAPIConnectionBgndMngr = fusedLocationAPIConnectionBgndMngr;
	}

	@Override
	public BadassBgndWorker.Status getStartingStatus()
	{
		return Status.WORK_ASAP;
	}


	@Override
	public void work()
	{
		manageConnection();
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
		if (fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().isPermissionGranted())
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
				ThisApp.getModel().appStateCtrl.setBgndTaskOngoing(Badass.getString(R.string.app_status_getting_location));
				askedForLocationUpdate = false;
				client.connect();

				if (status == Status.WORK_ASAP) sleep();
			}
			else
			{
				if (client.isConnected() && askedForLocationUpdate == false)
				{
					askLocationUpdates();
					sleep();
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
			Badass.logInFile("##! " + getName()+ " " + getCompleteStatusString() + ":  Don't have Location permission");
			setSpecificReasonProblemStringId(fusedLocationAPIConnectionBgndMngr.getBadassPermissionCtrl().getExplanationStringId());
			workNextSession();
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
		LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, ThisApp.getBgndTaskCtrl().getLocationWorker(), Looper.getMainLooper());
	}


}
