package eu.benayoun.badassweather.badass.ui.activity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.ref.WeakReference;

import eu.benayoun.badass.ui.UIBroadCastManager;
import eu.benayoun.badass.utility.os.time.TimeUtils;
import eu.benayoun.badassweather.badass.ui.activity.AppActivity;


/**
 * Created by Pierre on 24/01/2015.
 */
@SuppressWarnings("ALL")
public class ActivityOnEventReceiver extends BroadcastReceiver
{
	static protected WeakReference<AppActivity> mainActivityWeakReference = null;

	@Override
    public void onReceive(Context context, Intent intent)
    {
	    int eventId = UIBroadCastManager.getEventId(intent);
        if (mainActivityWeakReference != null)
        {
	        mainActivityWeakReference.get().onEvent(eventId, TimeUtils.getCurrentTimeInMs());
        }
    }

    public void register(LocalBroadcastManager localBroadcastManager, AppActivity mainActivity)
    {
	    mainActivityWeakReference = new WeakReference<>(mainActivity);
        localBroadcastManager.registerReceiver(this,
                new IntentFilter(
		                UIBroadCastManager.ACTION_EVENT));
    }

    public void unregister(LocalBroadcastManager localBroadcastManager)
    {
        localBroadcastManager.unregisterReceiver(this);
	    mainActivityWeakReference.clear();
	    mainActivityWeakReference=null;
    }
}
