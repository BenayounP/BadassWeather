package eu.benayoun.badassweather.ui.activity.screenmanagement;

import android.view.View;
import android.widget.FrameLayout;

import eu.benayoun.badass.ui.layout.ReactiveLayout;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badass.utility.ui.BadassViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ui.activity.screens.home.HomeScreen;
import eu.benayoun.badassweather.ui.events.UIEvents;


/**
 * Created by PierreB on 24/09/2017.
 */

public class ScreenManager
{
	private FrameLayout               mainView;

	private HomeScreen homeScreen;

	private ReactiveLayout currentLayout;

	public ScreenManager(FrameLayout mainViewArg)
	{
		this.mainView = mainViewArg;
		setHomeScreen();
	}

	public void onEvent(int eventId, long eventTimeInMs)
	{
		if (currentLayout!=null)
		{
			currentLayout.onEvent(eventId,eventTimeInMs);
		}
	}


	public void onDestroy()
	{
		if (homeScreen != null) homeScreen.releaseUI();
	}


	private void setHomeScreen()
	{
		if (homeScreen ==null)
		{
			View homeView = BadassViewUtils.inflateView(mainView, R.layout.screen_home);
			mainView.addView(homeView);
			homeScreen = new HomeScreen(homeView);
		}
		homeScreen.setVisible();
		currentLayout = homeScreen;
		onEvent(UIEvents.RESUME, BadassTimeUtils.getCurrentTimeInMs());
	}
}
