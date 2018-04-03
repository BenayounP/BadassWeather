package eu.benayoun.badassweather.badass.ui.activity.screenmanagement;

import android.view.View;
import android.widget.FrameLayout;

import eu.benayoun.badass.ui.layout.RefreshableLayoutTemplate;
import eu.benayoun.badass.utility.os.time.TimeUtils;
import eu.benayoun.badass.utility.ui.ViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.ui.activity.layouts.home.HomeScreen;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 24/09/2017.
 */

public class ScreenManager
{
	protected FrameLayout               mainView;
	protected View homeView;

	protected HomeScreen homeScreen;

	protected RefreshableLayoutTemplate currentLayout;

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

	public boolean onBackPressed()
	{
		boolean weInterceptedButton=false;
		return weInterceptedButton;
	}

	public void onDestroy()
	{
		if (homeScreen != null) homeScreen.releaseUI();
	}


	public void setHomeScreen()
	{
		if (homeScreen ==null)
		{
			homeView = ViewUtils.inflateView(mainView, R.layout.screen_home);
			mainView.addView(homeView);
			homeScreen = new HomeScreen(homeView);
		}
		homeScreen.setVisible();
		currentLayout = homeScreen;
		onEvent(UIEvents.UI_EVENT_RESUME, TimeUtils.getCurrentTimeInMs());
	}
}
