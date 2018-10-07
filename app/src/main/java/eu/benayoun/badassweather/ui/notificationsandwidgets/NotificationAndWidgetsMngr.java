package eu.benayoun.badassweather.ui.notificationsandwidgets;


import eu.benayoun.badass.ui.events.BadassUIEventListenerContract;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.ui.events.UIEvents;


public class NotificationAndWidgetsMngr
        implements BadassUIEventListenerContract
{
    private AppNotificationCtrl AppNotificationCtrl;
    private AppWidgetsCtrl appWidgetsCtrl;

    public NotificationAndWidgetsMngr()
    {
        RemoteViewCtrl remoteViewCtrl = new RemoteViewCtrl();
        AppNotificationCtrl = new AppNotificationCtrl(remoteViewCtrl);
        appWidgetsCtrl = new AppWidgetsCtrl(remoteViewCtrl);
    }

    @Override
    public void onEvent(int eventId, long eventTimeInMs)
    {
        AppNotificationCtrl.onEvent(eventId,eventTimeInMs);
        appWidgetsCtrl.onEvent(eventId,eventTimeInMs);
    }

    public void onNotificationSwitchClick()
    {
        ThisApp.getModel().appPreferencesAndAssets.toggleUserWantsToDisplayNotification();
        AppNotificationCtrl.onEvent(UIEvents.USER_NOTIFICATION_PREFERENCE, BadassTimeUtils.getCurrentTimeInMs());
    }
}
