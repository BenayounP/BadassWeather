package eu.benayoun.badassweather.ui.activity.screens.home;

import android.support.v4.widget.SwipeRefreshLayout;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.ui.events.UIEvents;



/**
 * Created by PierreB on 22/04/2017.
 */

public class HomeSwipeRefreshLayoutListener implements SwipeRefreshLayout.OnRefreshListener
{
	SwipeRefreshLayout swipeRefreshLayout;

	public HomeSwipeRefreshLayoutListener(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
	}

	@Override
	public void onRefresh()
	{
		ThisApp.getAppBadassJobList().updateAllData();
		Badass.broadcastUIEvent(UIEvents.RESUME);
	}


	public void refresh(int eventId)
	{
		if (eventId == UIEvents.RESUME || eventId == UIEvents.BACKGROUND_EVENT)
		{
			if (Badass.isThreadRunning()==false)
			{
				swipeRefreshLayout.setRefreshing(false);
			}
		}
	}

	/**
	 * INTERNAL COOKING
	 */


}
