package eu.benayoun.badassweather.badass.ui.notifications;


import android.support.v4.app.NotificationCompat;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.notification.factory.BadassNotificationMngr;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.applicationreceivers.NotificationDeleteReceiver;
import eu.benayoun.badassweather.badass.ui.activity.AppActivity;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;
import eu.benayoun.badassweather.badass.ui.notifications.remoteview.RemoteViewCtrlr;



public class NotificationMngr extends BadassNotificationMngr
{
	final protected int GLOBAL_NOTIFICATION_ID = 1976;
	final public int ALERT_NOTIFICATION_ID = 1;
	protected RemoteViewCtrlr remoteViewCtrlr;

	/**
	 * METHODS
	 */

	@Override
	public void onEvent(int eventId)
	{
		if (eventId == UIEvents.UI_EVENT_WEATHER_CHANGE)
		{
			manageLaunch();
		}
	}


	/**
	 * INTERNAL COOKING
	 */

	protected void manageLaunch()
	{
		if (ThisApp.getModel().appPreferencesAndAssets != null && ThisApp.getModel().appPreferencesAndAssets.isUserWantsToDisplayNotification())
		{
			if (remoteViewCtrlr ==null)
			{
				remoteViewCtrlr = new RemoteViewCtrlr();
			}
			if (notificationChannelDataContainer==null)
			{
				String appName = Badass.getString(R.string.app_name);
				String channelId = Badass.getString(R.string.notification_channel_ID,appName);
				String channelName = Badass.getString(R.string.notification_channel_name_ID,appName);
				String channelDescription = Badass.getString(R.string.notification_channel_description_ID,appName);

				setNotificationChannelDataContainer(channelId,channelName,channelDescription);
			}
			setCustomNotification(GLOBAL_NOTIFICATION_ID, ALERT_NOTIFICATION_ID, NotificationCompat.PRIORITY_DEFAULT,IS_NOT_ONGOING, R.drawable.ic_notification, remoteViewCtrlr.getRemoteViews(),AppActivity.class, NotificationDeleteReceiver.class);
		}
		else cancelNotification(GLOBAL_NOTIFICATION_ID);
	}

}
