package eu.benayoun.badassweather.badass.backgroundworker.receivers;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.backgroundworker.androidevents.AndroidEventsManager;
import eu.benayoun.badassweather.badass.backgroundworker.AppBackgroundWorker;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 11/09/2016.
 */
public class AppAndroidEventsManager extends AndroidEventsManager
{
	AppBackgroundWorker                      appBackgroundWorker;

	public AppAndroidEventsManager(AppBackgroundWorker appBackgroundWorker)
	{
		listenToInternetConnectivity();
		listenToScreenActivity();
		this.appBackgroundWorker = appBackgroundWorker;
	}

	// PERMISSION



// INTERNET

	@Override
	public void onConnectedToInternet()
	{
		Badass.logInFile("******! onConnectedToInternet");
		appBackgroundWorker.onConnectedToInternet();
	}

	@Override
	public void onDisconnectedToInternet()
	{
		Badass.logInFile("******! onDisconnectedToInternet");
	}

	public boolean isConnectedToInternet()
	{
		return internetConnectivityReceiver.isConnectedToInternet();
	}

	// SCREEN

	@Override
	public void onScreenOn()
	{
		Badass.logInFile("** onScreenOn");
		appBackgroundWorker.onScreenOn();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_SCREEN_ON);
	}

	@Override
	public void onScreenOff()
	{
		// nothing to do...yet
	}


	/**
	 * INTERNAL COOKING
	 */

}
