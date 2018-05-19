package eu.benayoun.badassweather.badass.background;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.androidevents.AndroidEventsMngr;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 11/09/2016.
 */
public class AppAndroidEventsMngr extends AndroidEventsMngr
{
	ThisAppBgndMngr thisAppBgndMngr;

	public AppAndroidEventsMngr(ThisAppBgndMngr thisAppBgndMngr)
	{
		listenToInternetConnectivity();
		listenToScreenActivity();
		this.thisAppBgndMngr = thisAppBgndMngr;
	}




// INTERNET

	@Override
	public void onConnectedToInternet()
	{
		Badass.logInFile("******! onConnectedToInternet");
		thisAppBgndMngr.onConnectedToInternet();
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
		thisAppBgndMngr.onScreenOn();
		Badass.broadcastUIEvent(UIEvents.SCREEN_ON);
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
