package eu.benayoun.badassweather.background;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.androidevents.internetconnectivity.BadassInternetConnectivityListenerContract;
import eu.benayoun.badass.background.androidevents.screen.BadassScreenActivityListenerContract;
import eu.benayoun.badassweather.ui.events.UIEvents;



/**
 * Created by PierreB on 11/09/2016.
 */
public class AppAndroidEventsCtrl implements BadassInternetConnectivityListenerContract, BadassScreenActivityListenerContract
{
	AppBadassJobList appWorkersCtrl;

	public AppAndroidEventsCtrl(AppBadassJobList appWorkersCtrl)
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
