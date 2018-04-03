package eu.benayoun.badassweather.badass.ui.activity.layouts.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import eu.benayoun.badass.ui.layout.RefreshableLayoutTemplate;
import eu.benayoun.badass.utility.ui.ViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 24/07/2017.
 */

public class HomeScreen extends RefreshableLayoutTemplate
{
	SwipeRefreshLayout             swipeRefreshLayout;
	View                           contentView;
	HomeSwipeRefreshLayoutListener homeSwipeRefreshLayoutListener;

	AppStatusLayout          appStatusLayout;

	TextView mainTextView;


	public HomeScreen(View mainViewArg)
	{
		super(mainViewArg);

		// swipeRefreshLayout
		swipeRefreshLayout = (SwipeRefreshLayout)mainView;
		swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.app_primary_color);
		swipeRefreshLayout.setColorSchemeResources(R.color.app_primary_text);
		homeSwipeRefreshLayoutListener = new HomeSwipeRefreshLayoutListener(swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(homeSwipeRefreshLayoutListener);

		contentView = mainView.findViewById(R.id.screen_home_content);

		mainTextView = mainView.findViewById(R.id.screen_home_main_text);

		View appStatusView = mainView.findViewById(R.id.screen_home_status_layout);
		ViewUtils.doNotDisplayOnAndroidNavigationBar(appStatusView);
		appStatusLayout = new AppStatusLayout(appStatusView);
		addSubLayout(appStatusLayout);

		addEventTrigger(UIEvents.UI_EVENT_RESUME);
		addEventTrigger(UIEvents.UI_EVENT_COMPUTE);
		addEventTrigger(UIEvents.UI_EVENT_LOCATION_CHANGE);
	}

	@Override
	public void onEvent(int eventId, long eventTimeInMs)
	{
		super.onEvent(eventId, eventTimeInMs);
	}

	@Override
	protected void internalRefresh(int eventId, long eventTimeInMs)
	{
		homeSwipeRefreshLayoutListener.refresh(eventId);
		String toDisplay ="";
		if (false == AppBadass.getDataContainer().bareDataContainer.locationCache.isEmpty())
		{
			toDisplay= "Lon: " + AppBadass.getDataContainer().bareDataContainer.locationCache.getLastLongitude();
		}
		mainTextView.setText(toDisplay);
	}

	/**
	 * INTERNAL COOKING
	 */


}
