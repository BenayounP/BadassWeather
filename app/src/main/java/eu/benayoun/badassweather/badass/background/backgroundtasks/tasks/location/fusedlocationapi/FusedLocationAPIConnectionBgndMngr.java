package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTask;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionListener;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class FusedLocationAPIConnectionBgndMngr implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, BadassPermissionListener
{
	BadassPermissionCtrl               badassPermissionCtrl;
	FusedLocationAPIConnectionBgndTask fusedLocationAPIConnectionBgndTask;
	BgndTask                           bgndTask;

	public FusedLocationAPIConnectionBgndMngr()
	{
		badassPermissionCtrl = Badass.getPermissionManager(Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_location, this);
		fusedLocationAPIConnectionBgndTask = new FusedLocationAPIConnectionBgndTask(this);
		bgndTask = new BgndTask(fusedLocationAPIConnectionBgndTask);
	}

	public BadassPermissionCtrl getBadassPermissionCtrl()
	{
		return badassPermissionCtrl;
	}

	public BgndTask getBgndTask()
	{
		return bgndTask;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Badass.logInFile("** on connected to FusedLocationAPI client");
		ThisApp.getThisAppBgndMngr().manageFusedLocationAPI();
	}


	@Override
	public void onConnectionSuspended(int i)
	{
		Badass.logInFile("** on connection supended from fusedlocationAPI client!");
		ThisApp.getThisAppBgndMngr().onFusedLocationAPIProblem();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Badass.logInFile("** on connection failed from fusedlocationAPI client: " + connectionResult.toString());
		ThisApp.getThisAppBgndMngr().onFusedLocationAPIProblem();
	}

	@Override
	public void onAppPermissionResult()
	{

		Badass.broadcastUIEvent(UIEvents.UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT);
		bgndTask.performTaskASAP();
		Badass.launchBackgroundTasks();
	}
}
