package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

/**
 * Created by Pierre on 16/11/2014.
 */
public class Widget extends AppWidgetProvider
{


	@Override
	public void onEnabled(Context context)
	{
		Badass.broadcastUIEvent(UIEvents.WIDGET_INSTALLED);
	}

	@Override
	public void onDisabled(Context context)
	{
		disableWidget(context);
	}



	/**
	 *  INTERNAL COOKING
	 */

	void disableWidget(Context context)
	{
		// nothing to do
	}
}
