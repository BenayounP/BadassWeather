package eu.benayoun.badassweather.badass.ui.activity.layouts.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import eu.benayoun.badass.ui.layout.RefreshableLayoutTemplate;
import eu.benayoun.badass.utility.ui.BadassViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.ui.UIModel;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


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
		BadassViewUtils.doNotDisplayOnHorizontalAndroidNavigationBar(mainTextView);

		View appStatusView = mainView.findViewById(R.id.screen_home_status_layout);
		BadassViewUtils.doNotDisplayOnVerticalAndroidNavigationBar(appStatusView);
		BadassViewUtils.doNotDisplayOnHorizontalAndroidNavigationBar(appStatusView);

		// switch
		final Switch notificationSwitch = mainView.findViewById(R.id.screen_home_switch_notification);
		notificationSwitch.setChecked(ThisApp.getModel().appPreferencesAndAssets.isUserWantsToDisplayNotification());
		notificationSwitch.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
					ThisApp.getNotificationAndWidgetsMngr().onNotificationSwitchClick();
			}
		});

	appStatusLayout = new AppStatusLayout(appStatusView);
	addSubLayout(appStatusLayout);



	addEventTrigger(UIEvents.RESUME);
	addEventTrigger(UIEvents.COMPUTE);
	addEventTrigger(UIEvents.WEATHER_CHANGE);
}


	@Override
	protected void internalRefresh(int eventId, long eventTimeInMs)
	{
		homeSwipeRefreshLayoutListener.refresh(eventId);
		String  toDisplay ="";
		UIModel uIModel   = ThisApp.getModel().uIModel;
		if (uIModel.isEmpty())
		{
			toDisplay = uIModel.getNoDataString();
		}
		else
		{
			toDisplay = uIModel.getCurrentWeather();
			if (uIModel.getNextWeather().equals("")==false)
			{
				toDisplay+="\n\n"+uIModel.getNextWeather();
			}
		}
		mainTextView.setText(toDisplay);
	}

	/**
	 * INTERNAL COOKING
	 */


}
