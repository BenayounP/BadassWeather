package eu.benayoun.badassweather.badass.ui.activity.screens.home;

import android.support.v4.widget.SwipeRefreshLayout;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


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
		ThisApp.getBgndTaskCtrl().updateAllData();
		Badass.broadcastUIEvent(UIEvents.RESUME);
	}


	public void refresh(int eventId)
	{
		if (eventId == UIEvents.RESUME || eventId == UIEvents.COMPUTE)
		{
			if (Badass.getBadassBackgroundMngr().isDoingTasks()==false)
			{
				swipeRefreshLayout.setRefreshing(false);
			}

		}
	}

	/**
	 * INTERNAL COOKING
	 */


}