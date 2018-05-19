package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.widget.RemoteViews;
import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.events.UIEventListenerContract;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class WidgetsCtrl implements UIEventListenerContract
{
    RemoteViewCtrlr remoteViewCtrlr;

    public WidgetsCtrl(RemoteViewCtrlr remoteViewCtrlr)
    {
        this.remoteViewCtrlr = remoteViewCtrlr;
    }

    @Override
    public void onEvent(int eventId, long eventTimeInMs)
    {
        if (eventId == UIEvents.WEATHER_CHANGE || eventId == UIEvents.WIDGET_INSTALLED)
        {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Badass.getApplicationContext());
            ComponentName cmpName = new ComponentName(Badass.getApplicationContext(), Widget.class);

            int[] widgetIds = appWidgetManager.getAppWidgetIds(cmpName);

            if (widgetIds.length != 0)
            {
                RemoteViews widgetView = remoteViewCtrlr.getRemoteViews();
                for (int wid : widgetIds)
                {
                    appWidgetManager.updateAppWidget(wid, widgetView);
                }
            }
        }
    }


}
