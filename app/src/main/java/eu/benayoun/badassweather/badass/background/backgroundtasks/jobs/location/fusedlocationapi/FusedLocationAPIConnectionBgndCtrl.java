package eu.benayoun.badassweather.badass.background.backgroundtasks.jobs.location.fusedlocationapi;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.badassjob.BadassJob;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badass.utility.os.permissions.PermissionListenerContract;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class FusedLocationAPIConnectionBgndCtrl
		implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, PermissionListenerContract
{
	BadassPermissionCtrl               badassPermissionCtrl;
	FusedLocationAPIConnectionJob fusedLocationAPIConnectionJob;

	public FusedLocationAPIConnectionBgndCtrl()
	{
		badassPermissionCtrl = Badass.getPermissionManager(Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_location_standard, this);
		badassPermissionCtrl.setExplanationForUsersThatCheckedNeverAskAgainStringId(R.string.permission_location_for_users_that_heck_never_ask_again);
		fusedLocationAPIConnectionJob = new FusedLocationAPIConnectionJob(this);
	}

	public BadassJob getBadassJob()
	{
		return fusedLocationAPIConnectionJob;
	}

	public BadassPermissionCtrl getBadassPermissionCtrl()
	{
		return badassPermissionCtrl;
	}


	public FusedLocationAPIConnectionJob getFusedLocationAPIConnectionJob() { return fusedLocationAPIConnectionJob; }

	public Location fetchLocation()
	{
		return fusedLocationAPIConnectionJob.fetchLocation();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Badass.logInFile("** on connected to FusedLocationAPI client");
		ThisApp.getAppWorkersCtrl().manageFusedLocationAPI();
	}


	@Override
	public void onConnectionSuspended(int i)
	{
		Badass.logInFile("** on connection supended from fusedlocationAPI client!");
		ThisApp.getAppWorkersCtrl().onFusedLocationAPIProblem();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Badass.logInFile("** on connection failed from fusedlocationAPI client: " + connectionResult.toString());
		ThisApp.getAppWorkersCtrl().onFusedLocationAPIProblem();
	}


	@Override
	public void onPermissionGranted()
	{
		Badass.broadcastUIEvent(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
		fusedLocationAPIConnectionJob.prepareToStartAtNextCall();
		Badass.startBadassThread();
	}

	@Override
	public void onPermissionDenied(boolean userHasCheckedNeverAskAgain)
	{
        fusedLocationAPIConnectionJob.setSpecificProblemStringId(badassPermissionCtrl.getExplanationStringId());
        ThisApp.getModel().appStateCtrl.updateState();
	}
}
