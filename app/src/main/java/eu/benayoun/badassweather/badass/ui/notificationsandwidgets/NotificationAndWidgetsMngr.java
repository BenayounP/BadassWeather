package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;


import eu.benayoun.badass.ui.events.UIEventListenerContract;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class NotificationAndWidgetsMngr implements UIEventListenerContract
{
    protected NotificationCtrl notificationCtrl;
    protected RemoteViewCtrl remoteViewCtrl;
    protected WidgetsCtrl widgetsCtrl;

    public NotificationAndWidgetsMngr()
    {
        remoteViewCtrl = new RemoteViewCtrl();
        notificationCtrl = new NotificationCtrl(remoteViewCtrl);
        widgetsCtrl = new WidgetsCtrl(remoteViewCtrl);
    }

    @Override
    public void onEvent(int eventId, long eventTimeInMs)
    {
        notificationCtrl.onEvent(eventId,eventTimeInMs);
        widgetsCtrl.onEvent(eventId,eventTimeInMs);
    }

    public void onNotificationSwitchClick()
    {
        ThisApp.getModel().appPreferencesAndAssets.toggleUserWantsToDisplayNotification();
        notificationCtrl.onEvent(UIEvents.USER_NOTIFICATION_PREFERENCE, BadassTimeUtils.getCurrentTimeInMs());
    }
}
