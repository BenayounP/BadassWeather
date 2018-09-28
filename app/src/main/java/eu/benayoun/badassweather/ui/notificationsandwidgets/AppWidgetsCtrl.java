package eu.benayoun.badassweather.ui.notificationsandwidgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.widget.RemoteViews;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.events.BadassUIEventListenerContract;
import eu.benayoun.badassweather.ui.events.UIEvents;


public class AppWidgetsCtrl implements BadassUIEventListenerContract
{
    RemoteViewCtrl remoteViewCtrl;

    public AppWidgetsCtrl(RemoteViewCtrl remoteViewCtrl)
    {
        this.remoteViewCtrl = remoteViewCtrl;
    }

    @Override
    public void onEvent(int eventId, long eventTimeInMs)
    {
        if (eventId == UIEvents.WEATHER_CHANGE)
        {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Badass.getApplicationContext());
            ComponentName cmpName = new ComponentName(Badass.getApplicationContext(), ThisAppWidgetProvider.class);

            int[] widgetIds = appWidgetManager.getAppWidgetIds(cmpName);

            if (widgetIds.length != 0)
            {
                RemoteViews widgetView = remoteViewCtrl.getWidgetsRemoteViews(ThisAppWidgetProvider.class, ThisAppWidgetProvider.ON_WIDGET_CLICK_ACTION);
                for (int wid : widgetIds)
                {
                    appWidgetManager.updateAppWidget(wid, widgetView);
                }
            }
        }
    }


}
