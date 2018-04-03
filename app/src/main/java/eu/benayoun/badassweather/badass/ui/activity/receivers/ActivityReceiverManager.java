package eu.benayoun.badassweather.badass.ui.activity.receivers;

import android.support.v4.content.LocalBroadcastManager;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.badass.ui.activity.AppActivity;


/**
 * Created by Pierre on 11/02/2015.
 */
@SuppressWarnings("ALL")
public class ActivityReceiverManager
{
	ActivityOnEventReceiver MainActivityOnEventReceiver = null;

	public void registerInActivity(AppActivity mainActivity)
	{
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
				.getInstance(Badass.getApplicationContext());
		registerRefreshReceiver(localBroadcastManager, mainActivity);
	}

	public void unregisterInActivity()
	{
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
				.getInstance(Badass.getApplicationContext());
		unregisterRefreshReceiver(localBroadcastManager);
	}

	/**
	 * INTERNAL COOKING
	 */

	// broadcastUIEvent

	protected void  registerRefreshReceiver(LocalBroadcastManager localBroadcastManager, AppActivity mainActivity)
	{
		if (null == MainActivityOnEventReceiver)
		{
			MainActivityOnEventReceiver = new ActivityOnEventReceiver();
		}
		MainActivityOnEventReceiver.register(localBroadcastManager,mainActivity);
	}

	protected void  unregisterRefreshReceiver(LocalBroadcastManager localBroadcastManager)
	{
		if (null != MainActivityOnEventReceiver)
		{
			MainActivityOnEventReceiver.unregister(localBroadcastManager);
		}
	}
}
