package eu.benayoun.badassweather.ui.notificationsandwidgets;


import android.support.v4.app.NotificationCompat;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.notification.BadassNotificationCtrl;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.applicationreceivers.NotificationDeleteReceiver;
import eu.benayoun.badassweather.ui.activity.MainActivity;
import eu.benayoun.badassweather.ui.events.UIEvents;


class AppNotificationCtrl extends BadassNotificationCtrl
{
	private final int NOTIFICATION_ID = 1976;
	private final int INTENT_ID = 1;

	private RemoteViewCtrl remoteViewCtrl;

	public AppNotificationCtrl(RemoteViewCtrl remoteViewCtrl)
	{
		this.remoteViewCtrl = remoteViewCtrl;
	}

	/**
	 * METHODS
	 */

	@Override
	public void onEvent(int eventId, long eventTimeInMs)
	{
		if (eventId == UIEvents.WEATHER_CHANGE || eventId == UIEvents.USER_NOTIFICATION_PREFERENCE)
		{
			manageLaunch();
		}
	}


	/**
	 * INTERNAL COOKING
	 */

    private void manageLaunch()
	{
		if (ThisApp.getModel().appPreferencesAndAssets != null && ThisApp.getModel().appPreferencesAndAssets.isUserWantsToDisplayNotification())
		{
			if (notificationChannelDataContainer==null)
			{
				String appName = Badass.getString(R.string.app_name);
				String channelId = Badass.getString(R.string.notification_channel_ID,appName);
				String channelName = Badass.getString(R.string.notification_channel_name_description_ID,appName);
				String channelDescription = Badass.getString(R.string.notification_channel_name_description_ID,appName);

				setNotificationChannelDataContainer(channelId,channelName,channelDescription);
			}
			notifyCustom(NOTIFICATION_ID, INTENT_ID, NotificationCompat.PRIORITY_DEFAULT,IS_NOT_ONGOING,remoteViewCtrl.getNotificationRemoteViews(), R.drawable.ic_notification,MainActivity.class, NotificationDeleteReceiver.class);
		}
		else cancelNotification(NOTIFICATION_ID);
	}

}
