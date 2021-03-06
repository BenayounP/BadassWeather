package eu.benayoun.badassweather.ui.notificationsandwidgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.ui.activity.MainActivity;


/**
 * Created by Pierre on 16/11/2014.
 */
public class ThisAppWidgetProvider extends AppWidgetProvider
{
	public static final String ON_WIDGET_CLICK_ACTION = "ON_WIDGET_CLICK_ACTION";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (ON_WIDGET_CLICK_ACTION.equals(intent.getAction()))
		{
			// LAUNCH/Go to activity
            Intent launchActivityIntent = new Intent(context, MainActivity.class);
            launchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(launchActivityIntent);
		}
		else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction()))
		{
            ThisApp.getAppBadassJobList().updateUI();
		}
	}

    /**
	 *  INTERNAL COOKING
	 */

}
