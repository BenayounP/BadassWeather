package eu.benayoun.badassweather.badass.ui.activity.layouts.home;

import android.support.v4.widget.SwipeRefreshLayout;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


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
		AppBadass.getAppBackgroundWorker().updateAllData();
		Badass.broadcastUIEvent(UIEvents.UI_EVENT_RESUME);
	}


	public void refresh(int eventId)
	{
		if (eventId == UIEvents.UI_EVENT_RESUME || eventId == UIEvents.UI_EVENT_COMPUTE)
		{
			if (AppBadass.getAppBackgroundWorker().isDoingTasks()==false)
			{
				swipeRefreshLayout.setRefreshing(false);
			}

		}
	}

	/**
	 * INTERNAL COOKING
	 */


}
