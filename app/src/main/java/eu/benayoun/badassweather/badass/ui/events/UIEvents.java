package eu.benayoun.badassweather.badass.ui.events;

import java.util.ArrayList;

import eu.benayoun.badass.ui.events.UIEventsBadassContract;

/**
 * Created by PierreB on 31/07/2017.
 */

public class UIEvents implements UIEventsBadassContract
{
	/**
	 * EVENT TYPE
	 */

	public static final int RESUME = 1;
	public static final int SCREEN_ON                    = 2;

	public static final int ASK_FINE_LOCATION_PERMISSION = 3;
	public static final int PERMISSION_STATUS_CHANGE_RESULT = 4;

	public static final int USER_NOTIFICATION_PREFERENCE = 5;

	public static final int COMPUTE          = 6;
	public static final int APP_STATUS_CHANGE         = 7;

	public static final int WEATHER_CHANGE          =8;

	public static final int WIDGET_INSTALLED          =9;

	public static final int AMOUNT_OF_REFRESH_REASONS = WIDGET_INSTALLED +1;


	public ArrayList<Boolean> getFreshUiEventsList()
	{
		ArrayList<Boolean> defaultRefreshArray = new ArrayList<>(AMOUNT_OF_REFRESH_REASONS);
		for (int i=0;i<AMOUNT_OF_REFRESH_REASONS;i++)
		{
			defaultRefreshArray.add(new Boolean(false));
		}
		return defaultRefreshArray;
	}

	public String getEventName(int eventId)
	{
		String reasonString = "unknown Reason";
		switch (eventId)
		{
			case RESUME:
				reasonString = "RESUME";break;
			case SCREEN_ON:
				reasonString = "SCREEN_ON";break;
			case ASK_FINE_LOCATION_PERMISSION:
				reasonString = "ASK_FINE_LOCATION_PERMISSION";break;

			case PERMISSION_STATUS_CHANGE_RESULT:
				reasonString = "PERMISSION_STATUS_CHANGE_RESULT";break;
			case COMPUTE:
				reasonString = "COMPUTE";break;
			case APP_STATUS_CHANGE:
				reasonString = "APP_STATUS_CHANGE";break;
			case WEATHER_CHANGE:
				reasonString = "WEATHER_CHANGE";break;
			case WIDGET_INSTALLED:
				reasonString = "WIDGET_INSTALLED";break;
		}
		return reasonString;
	}

}
