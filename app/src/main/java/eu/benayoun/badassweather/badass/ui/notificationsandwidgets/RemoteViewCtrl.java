package eu.benayoun.badassweather.badass.ui.notificationsandwidgets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.ui.UIModel;


/**
 * Created by PierreB on 14/02/2017.
 */
@SuppressWarnings("ALL")
public class RemoteViewCtrl
{
	RemoteViews notificationRemoteViews;
	RemoteViews widgetsRemoteViews;

	public RemoteViews getNotificationRemoteViews()
	{
		if (notificationRemoteViews == null)
		{
			notificationRemoteViews = new RemoteViews(Badass.getApplicationContext().getPackageName(), R.layout.layout_remoteview);
		}
		updateViews(notificationRemoteViews);
		return notificationRemoteViews;
	}

	public RemoteViews getWidgetsRemoteViews(Class<?> widgetClass, String widgetActionName)
	{
		if (widgetsRemoteViews == null)
		{
			Context applicationContext = Badass.getApplicationContext();
			widgetsRemoteViews = new RemoteViews(applicationContext.getPackageName(), R.layout.layout_remoteview);
			Intent intent = new Intent(applicationContext, ThisAppWidgetProvider.class);
			intent.setAction(widgetActionName);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0);
			widgetsRemoteViews.setOnClickPendingIntent(R.id.layout_remoteview_main_layout,pendingIntent);
		}
		updateViews(widgetsRemoteViews);
		return widgetsRemoteViews;
	}

	/**
	 * INTERNAL COOKING
	 */

	protected void updateViews(RemoteViews remoteViews)
	{
		String  currentWeatherString ="";
		String  nextWeatherString ="";
		UIModel uIModel   = ThisApp.getModel().uIModel;

		// VISIILITY
		int viewVisibility = (uIModel.isEmpty() ? View.GONE : View.VISIBLE);
		int noDataViewVisibility = (uIModel.isEmpty() ? View.VISIBLE : View.GONE);
		remoteViews.setViewVisibility(R.id.layout_remoteview_next_weather_text,viewVisibility);
		remoteViews.setViewVisibility(R.id.layout_remoteview_separator,viewVisibility);
		remoteViews.setViewVisibility(R.id.layout_remoteview_current_weather_text,viewVisibility);
		remoteViews.setViewVisibility(R.id.layout_remoteview_no_data_text,noDataViewVisibility) ;

		//CONTENT
		if (uIModel.isEmpty()==false)
		{
			currentWeatherString = uIModel.getCurrentWeather();
			if (uIModel.getNextWeather().equals("") == false)
			{
				nextWeatherString = uIModel.getNextWeather();
			}

			remoteViews.setTextViewText(R.id.layout_remoteview_current_weather_text, currentWeatherString);
			boolean nextWeatherIsEmpty = nextWeatherString.equals("");
			int nextWeatherViewVisibility = (nextWeatherIsEmpty ? View.GONE : View.VISIBLE);
			remoteViews.setViewVisibility(R.id.layout_remoteview_next_weather_text, nextWeatherViewVisibility);
			if (nextWeatherIsEmpty == false)
			{
				remoteViews.setTextViewText(R.id.layout_remoteview_next_weather_text, nextWeatherString);
			}
		}
	}

	protected PendingIntent getPendingSelfIntent(Context context, String action) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}
}
