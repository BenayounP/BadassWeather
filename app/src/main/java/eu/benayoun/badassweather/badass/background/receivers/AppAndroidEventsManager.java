package eu.benayoun.badassweather.badass.background.receivers;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.androidevents.AndroidEventsManager;
import eu.benayoun.badassweather.badass.background.ThisAppAppBgndManager;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 11/09/2016.
 */
public class AppAndroidEventsManager extends AndroidEventsManager
{
	ThisAppAppBgndManager thisAppBgndManager;

	public AppAndroidEventsManager(ThisAppAppBgndManager thisAppBgndManager)
	{
		listenToInternetConnectivity();
		listenToScreenActivity();
		this.thisAppBgndManager = thisAppBgndManager;
	}

	// PERMISSION



// INTERNET

	@Override
	public void onConnectedToInternet()
	{
		Badass.logInFile("******! onConnectedToInternet");
		thisAppBgndManager.onConnectedToInternet();
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
		thisAppBgndManager.onScreenOn();
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
