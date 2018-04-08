package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.fusedlocationapi;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BgndTaskCntrl;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionListener;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionManager;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;

public class FusedLocationAPIConnectionBgndManager implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, BadassPermissionListener
{
	BadassPermissionManager            badassPermissionManager;
	FusedLocationAPIConnectionBgndCtrl fusedLocationAPIConnectionBgndCtrl;
	BgndTaskCntrl                      bgndTaskCntrl;

	public FusedLocationAPIConnectionBgndManager()
	{
		badassPermissionManager = Badass.getPermissionManager(Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_location, this);
		fusedLocationAPIConnectionBgndCtrl = new FusedLocationAPIConnectionBgndCtrl(this);
		bgndTaskCntrl = new BgndTaskCntrl(fusedLocationAPIConnectionBgndCtrl);
	}

	public BadassPermissionManager getBadassPermissionManager()
	{
		return badassPermissionManager;
	}

	public BgndTaskCntrl getBgndTaskCntrl()
	{
		return bgndTaskCntrl;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Badass.logInFile("** on connected to FusedLocationAPI client");
		ThisApp.getThisAppBgndManager().manageFusedLocationAPI();
	}


	@Override
	public void onConnectionSuspended(int i)
	{
		Badass.logInFile("** on connection supended from fusedlocationAPI client!");
		ThisApp.getThisAppBgndManager().onFusedLocationAPIProblem();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Badass.logInFile("** on connection failed from fusedlocationAPI client: " + connectionResult.toString());
		ThisApp.getThisAppBgndManager().onFusedLocationAPIProblem();
	}

	@Override
	public void onAppPermissionResult()
	{

		Badass.broadcastUIEvent(UIEvents.UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT);
		bgndTaskCntrl.performTaskASAP();
		Badass.launchBackgroundOperations();
	}
}
