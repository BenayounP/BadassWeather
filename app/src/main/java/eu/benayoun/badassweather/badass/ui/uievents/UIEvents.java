package eu.benayoun.badassweather.badass.ui.uievents;

import java.util.ArrayList;

import eu.benayoun.badass.ui.events.UIEventsInterface;

/**
 * Created by PierreB on 31/07/2017.
 */

public class UIEvents implements UIEventsInterface
{
	/**
	 * EVENT TYPE
	 */

	public static final int UI_EVENT_RESUME = 1;
	public static final int UI_EVENT_SCREEN_ON                    = 2;

	public static final int UI_EVENT_UIDATA_AVAILABLE                   = 3;

	public static final int UI_EVENT_ASK_FINE_LOCATION_PERMISSION = 4;
	public static final int UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT = 5;

	public static final int UI_EVENT_COMPUTE          = 6;
	public static final int UI_EVENT_APP_STATUS_CHANGE         = 7;

	public static final int UI_EVENT_LOCATION_CHANGE          =8;
	public static final int UI_EVENT_FORECAST_CHANGE_HOURLY  = 9;

	public static final int AMOUNT_OF_REFRESH_REASONS = UI_EVENT_FORECAST_CHANGE_HOURLY +1;


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
			case UI_EVENT_RESUME:
				reasonString = "UI_EVENT_RESUME";break;
			case UI_EVENT_SCREEN_ON:
				reasonString = "UI_EVENT_SCREEN_ON";break;
			case UI_EVENT_UIDATA_AVAILABLE:
				reasonString = "UI_EVENT_UIDATA_AVAILABLE";break;
			case UI_EVENT_ASK_FINE_LOCATION_PERMISSION:
				reasonString = "UI_EVENT_ASK_FINE_LOCATION_PERMISSION";break;

			case UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT:
				reasonString = "UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT";break;
			case UI_EVENT_COMPUTE:
				reasonString = "UI_EVENT_COMPUTE";break;
			case UI_EVENT_APP_STATUS_CHANGE:
				reasonString = "UI_EVENT_APP_STATUS_CHANGE";break;
			case UI_EVENT_LOCATION_CHANGE:
				reasonString = "UI_EVENT_LOCATION_CHANGE";break;
			case UI_EVENT_FORECAST_CHANGE_HOURLY:
				reasonString = "UI_EVENT_FORECAST_CHANGE_HOURLY";break;
		}
		return reasonString;
	}

}
