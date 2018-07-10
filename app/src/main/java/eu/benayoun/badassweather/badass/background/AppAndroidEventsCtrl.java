package eu.benayoun.badassweather.badass.background;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.androidevents.AndroidEventsCtrl;
import eu.benayoun.badass.background.androidevents.internetconnectivity.InternetConnectivityListenerBadassContract;
import eu.benayoun.badass.background.androidevents.screen.ScreenActivityListenerBadassContract;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 11/09/2016.
 */
public class AppAndroidEventsCtrl implements InternetConnectivityListenerBadassContract, ScreenActivityListenerBadassContract
{
	TasksListCtrl thisAppBgndMngr;

	public AppAndroidEventsCtrl(TasksListCtrl thisAppBgndMngr)
	{
		Badass.listenToInternetConnectivity(this);
		Badass.listenToScreenActivity(this);
		this.thisAppBgndMngr = thisAppBgndMngr;
	}


// INTERNET

	@Override
	public void onConnectedToInternet()
	{
		thisAppBgndMngr.onConnectedToInternet();
	}

	@Override
	public void onDisconnectedToInternet()
	{
		Badass.logInFile("******! onDisconnectedToInternet");
	}

	public boolean isConnectedToInternet()
	{
		AndroidEventsCtrl androidEventsCtrl = Badass.getAndroidEventsCtrl();

		return (androidEventsCtrl == null ? false : androidEventsCtrl.isConnectedToInternet());
	}

	// SCREEN

	@Override
	public void onScreenOn()
	{
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
