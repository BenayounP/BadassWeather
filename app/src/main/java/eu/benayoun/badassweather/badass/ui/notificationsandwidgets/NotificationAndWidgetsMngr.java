package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;


import eu.benayoun.badass.ui.events.BadassUIEventListenerContract;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class NotificationAndWidgetsMngr implements BadassUIEventListenerContract
{
    protected NotificationCtrl notificationCtrl;
    protected RemoteViewCtrl remoteViewCtrl;
    protected AppWidgetsCtrl appWidgetsCtrl;

    public NotificationAndWidgetsMngr()
    {
        remoteViewCtrl = new RemoteViewCtrl();
        notificationCtrl = new NotificationCtrl(remoteViewCtrl);
        appWidgetsCtrl = new AppWidgetsCtrl(remoteViewCtrl);
    }

    @Override
    public void onEvent(int eventId, long eventTimeInMs)
    {
        notificationCtrl.onEvent(eventId,eventTimeInMs);
        appWidgetsCtrl.onEvent(eventId,eventTimeInMs);
    }

    public void onNotificationSwitchClick()
    {
        ThisApp.getModel().appPreferencesAndAssets.toggleUserWantsToDisplayNotification();
        notificationCtrl.onEvent(UIEvents.USER_NOTIFICATION_PREFERENCE, BadassTimeUtils.getCurrentTimeInMs());
    }
}
