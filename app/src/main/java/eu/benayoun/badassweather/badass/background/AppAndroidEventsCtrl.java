package eu.benayoun.badassweather.badass.background;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.androidevents.internetconnectivity.InternetConnectivityListenerBadassContract;
import eu.benayoun.badass.background.androidevents.screen.ScreenActivityListenerBadassContract;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 11/09/2016.
 */
public class AppAndroidEventsCtrl implements InternetConnectivityListenerBadassContract, ScreenActivityListenerBadassContract
{
	AppWorkersCtrl appWorkersCtrl;

	public AppAndroidEventsCtrl(AppWorkersCtrl appWorkersCtrl)
	{
		Badass.listenToInternetConnectivity(this);
		Badass.listenToScreenActivity(this);
		this.appWorkersCtrl = appWorkersCtrl;
	}


// INTERNET

	@Override
	public void onConnectedToInternet()
	{
		appWorkersCtrl.onConnectedToInternet();
	}

	@Override
	public void onDisconnectedToInternet()
	{
		Badass.logInFile("******! onDisconnectedToInternet");
	}


	// SCREEN

	@Override
	public void onScreenOn()
	{
		appWorkersCtrl.onScreenOn();
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
