package eu.benayoun.badassweather.badass.ui.notifications.remoteview;

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
public class RemoteViewCtrlr
{
	protected RemoteViews remoteViews;

	public RemoteViews getRemoteViews()
	{
		setView();
		return remoteViews;
	}

	/**
	 * INTERNAL COOKING
	 */

	protected void setView()
	{
		if (remoteViews == null)
		{
			remoteViews = new RemoteViews(Badass.getApplicationContext().getPackageName(), R.layout.layout_remoteview);
		}
		String  currentWeatherString ="";
		String  nextWeatherString ="";
		UIModel uIModel   = ThisApp.getModel().uIModel;
		if (false==uIModel.isEmpty())
		{
			currentWeatherString = uIModel.getCurrentWeather();
			if (uIModel.getNextWeather().equals("")==false)
			{
				nextWeatherString=uIModel.getNextWeather();
			}
		}
		remoteViews.setTextViewText(R.id.layout_remoteview_current_weather_text, currentWeatherString);

		boolean nextWeatherIsEmpty                    = nextWeatherString.equals("");
		int     nextWeatherViewVisibility = (nextWeatherIsEmpty ? View.GONE : View.VISIBLE );
		remoteViews.setViewVisibility(R.id.layout_remoteview_next_weather_text,nextWeatherViewVisibility);
		if (nextWeatherIsEmpty==false)
		{
			remoteViews.setTextViewText(R.id.layout_remoteview_next_weather_text, nextWeatherString);
		}
	}

}
