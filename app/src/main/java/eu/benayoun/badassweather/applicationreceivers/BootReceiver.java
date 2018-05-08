package eu.benayoun.badassweather.applicationreceivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Pierre on 16/11/2014.
 */
public class BootReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent arg1)
    {
        // nothing to do
        // this receiver is just here to have application launch on boot
    }
}
