package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.badass.ui.activity.AppActivity;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

/**
 * Created by Pierre on 16/11/2014.
 */
public class Widget extends AppWidgetProvider
{
	public static final String ON_WIDGET_CLICK_ACTION = "ON_WIDGET_CLICK_ACTION";



	@Override
	public void onReceive(Context context, Intent intent)
	{

		if (ON_WIDGET_CLICK_ACTION.equals(intent.getAction()))
		{
			// LAUNCH/Go to activitu
			context.startActivity(new Intent(context, AppActivity.class) );
		}
		else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(intent.getAction()))
		{
			Badass.broadcastUIEvent(UIEvents.WIDGET_INSTALLED);
		}
	};

	/**
	 *  INTERNAL COOKING
	 */

}
