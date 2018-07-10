package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badass.utility.os.permissions.PermissionListenerBadassContract;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class FusedLocationAPIConnectionBgndCtrl implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, PermissionListenerBadassContract
{
	BadassPermissionCtrl               badassPermissionCtrl;
	FusedLocationAPIConnectionTaskWorker fusedLocationAPIConnectionBgndTask;

	public FusedLocationAPIConnectionBgndCtrl()
	{
		badassPermissionCtrl = Badass.getPermissionManager(Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_location_standard, this);
		badassPermissionCtrl.setExplanationForUsersThatCheckedNeverAskAgainStringId(R.string.permission_location_for_users_that_heck_never_ask_again);
		fusedLocationAPIConnectionBgndTask = new FusedLocationAPIConnectionTaskWorker(this);
	}


	public BadassTaskCtrl getBadassBgndTask()
	{
		return fusedLocationAPIConnectionBgndTask.getBadassTaskCtrl();
	}

	public BadassPermissionCtrl getBadassPermissionCtrl()
	{
		return badassPermissionCtrl;
	}


	public FusedLocationAPIConnectionTaskWorker getFusedLocationAPIConnectionBgndTask() { return fusedLocationAPIConnectionBgndTask; }

	public Location fetchLocation()
	{
		return fusedLocationAPIConnectionBgndTask.fetchLocation();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Badass.logInFile("** on connected to FusedLocationAPI client");
		ThisApp.getBgndTaskCtrl().manageFusedLocationAPI();
	}


	@Override
	public void onConnectionSuspended(int i)
	{
		Badass.logInFile("** on connection supended from fusedlocationAPI client!");
		ThisApp.getBgndTaskCtrl().onFusedLocationAPIProblem();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Badass.logInFile("** on connection failed from fusedlocationAPI client: " + connectionResult.toString());
		ThisApp.getBgndTaskCtrl().onFusedLocationAPIProblem();
	}


	@Override
	public void onPermissionGranted()
	{
		Badass.broadcastUIEvent(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
		fusedLocationAPIConnectionBgndTask.getBadassTaskCtrl().performTaskASAP();
		Badass.launchBackgroundTasks();
	}

	@Override
	public void onPermissionDenied(boolean userHasCheckedNeverAskAgain)
	{
        fusedLocationAPIConnectionBgndTask.getBadassTaskCtrl().setSpecificReasonProblemStringId(badassPermissionCtrl.getExplanationStringId());
        ThisApp.getModel().appStatusCtrl.updateStatus();
	}
}
