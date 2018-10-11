package eu.benayoun.badassweather.background;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.androidevents.internetconnectivity.BadassInternetConnectivityListenerContract;
import eu.benayoun.badass.background.androidevents.screen.BadassScreenActivityListenerContract;
import eu.benayoun.badassweather.ui.events.UIEvents;



/**
 * Created by PierreB on 11/09/2016.
 */
class AppAndroidEventsCtrl implements BadassInternetConnectivityListenerContract, BadassScreenActivityListenerContract
{
	private AppBadassJobList appBadassJobList;

	public AppAndroidEventsCtrl(AppBadassJobList appBadassJobList)
	{
		Badass.listenToInternetConnectivity(this);
		Badass.listenToScreenActivity(this);
		this.appBadassJobList = appBadassJobList;
	}


// INTERNET

	@Override
	public void onConnectedToInternet()
	{
		appBadassJobList.onConnectedToInternet();
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
		appBadassJobList.onScreenOn();
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
