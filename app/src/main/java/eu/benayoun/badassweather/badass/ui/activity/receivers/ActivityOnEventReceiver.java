package eu.benayoun.badassweather.badass.ui.activity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.ref.WeakReference;

import eu.benayoun.badass.ui.BadassUIBroadCastMngr;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.badass.ui.activity.MainActivity;


/**
 * Created by Pierre on 24/01/2015.
 */
@SuppressWarnings("ALL")
public class ActivityOnEventReceiver extends BroadcastReceiver
{
	static protected WeakReference<MainActivity> mainActivityWeakReference = null;

	@Override
    public void onReceive(Context context, Intent intent)
    {
	    int eventId = BadassUIBroadCastMngr.getEventId(intent);
        if (mainActivityWeakReference != null)
        {
	        mainActivityWeakReference.get().onEvent(eventId, BadassTimeUtils.getCurrentTimeInMs());
        }
    }

    public void register(LocalBroadcastManager localBroadcastManager, MainActivity mainActivity)
    {
	    mainActivityWeakReference = new WeakReference<>(mainActivity);
        localBroadcastManager.registerReceiver(this,
                new IntentFilter(
		                BadassUIBroadCastMngr.ACTION_EVENT));
    }

    public void unregister(LocalBroadcastManager localBroadcastManager)
    {
        localBroadcastManager.unregisterReceiver(this);
	    mainActivityWeakReference.clear();
	    mainActivityWeakReference=null;
    }
}
