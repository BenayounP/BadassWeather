package eu.benayoun.badassweather.ui.events;

import java.util.ArrayList;

import eu.benayoun.badass.ui.events.BadassUIEventsContract;

/**
 * Created by PierreB on 31/07/2017.
 */

public class UIEvents implements BadassUIEventsContract
{
	/**
	 * EVENT TYPE
	 */

	public static final int RESUME = 0;
	public static final int SCREEN_ON                    = 1;

	public static final int ASK_FINE_LOCATION_PERMISSION = 2;
	public static final int PERMISSION_STATUS_CHANGE_RESULT = 3;

	public static final int USER_NOTIFICATION_PREFERENCE = 4;

	public static final int BACKGROUND_EVENT = 5;
	public static final int app_state_CHANGE         = 6;

	public static final int WEATHER_CHANGE          =7;

	private static final int AMOUNT_OF_EVENTS = WEATHER_CHANGE+1;


	public ArrayList<Boolean> getFreshUiEventsList()
	{
		ArrayList<Boolean> defaultRefreshArray = new ArrayList<>(AMOUNT_OF_EVENTS);
		for (int i = 0; i< AMOUNT_OF_EVENTS; i++)
		{
			defaultRefreshArray.add(Boolean.FALSE);
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
			case BACKGROUND_EVENT:
				reasonString = "BACKGROUND_EVENT";break;
			case app_state_CHANGE:
				reasonString = "app_state_CHANGE";break;
			case WEATHER_CHANGE:
				reasonString = "WEATHER_CHANGE";break;
		}
		return reasonString;
	}

}
