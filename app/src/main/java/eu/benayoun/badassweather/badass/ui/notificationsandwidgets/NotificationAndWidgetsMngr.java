package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;


import eu.benayoun.badass.ui.events.UIEventListenerContract;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class NotificationAndWidgetsMngr implements UIEventListenerContract
{
    protected NotificationCtrl notificationCtrl;
    protected RemoteViewCtrlr remoteViewCtrlr;
    protected WidgetsCtrl widgetsCtrl;

    public NotificationAndWidgetsMngr()
    {
        remoteViewCtrlr = new RemoteViewCtrlr();
        notificationCtrl = new NotificationCtrl(remoteViewCtrlr);
        widgetsCtrl = new WidgetsCtrl(remoteViewCtrlr);
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
