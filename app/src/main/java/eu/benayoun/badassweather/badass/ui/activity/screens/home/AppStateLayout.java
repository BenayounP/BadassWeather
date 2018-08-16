package eu.benayoun.badassweather.badass.ui.activity.screens.home;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.layout.ReactiveLayout;
import eu.benayoun.badass.utility.ui.BadassViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.application.AppStateCtrl;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

/**
 * Created by PierreB on 24/07/2017.
 */

public class AppStateLayout extends ReactiveLayout
{
	TextView mainTextView;
	Button   resolveButton;

	public AppStateLayout(View mainViewArg)
	{
		super(mainViewArg);
		mainTextView = mainView.findViewById(R.id.screen_home_status_text);
		resolveButton = mainView.findViewById(R.id.screen_home_status_resolve);
		resolveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				ThisApp.getModel().appStateCtrl.onUserClick();
			}
		});

		listenTo(UIEvents.COMPUTE);
		listenTo(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
		listenTo(UIEvents.APP_STATUS_CHANGE);
		listenTo(UIEvents.RESUME);
	}

    @Override
    public void onEvent(int eventId, long eventTimeInMs)
    {
        Badass.log("$$ AppStateLayout on Event: " + Badass.getEventName(eventId));
        super.onEvent(eventId, eventTimeInMs);
    }

    /**
	 * INTERNAL COOKING
	 */



	@Override
	protected void updateMainContent(int eventId, long eventTimeInMs)
	{
        Badass.log("$$ updateMainContent on Event: " + Badass.getEventName(eventId));
		AppStateCtrl appStateCtrl = ThisApp.getModel().appStateCtrl;
		boolean       weNeedUserAction =false;
		if (appStateCtrl.thereIsProblem())
		{
			mainView.setBackgroundResource(R.drawable.gradient_background);
			BadassViewUtils.setGradientBackgroundView(mainView,Badass.getColor(R.color.app_problem_1), Badass.getColor(R.color.app_problem_2));
			if (appStateCtrl.thereIsFineLocationPermissionPb())
			{
				weNeedUserAction = true;
			}
		}
		else
		{
			mainView.setBackgroundResource(0);
		}

		if (weNeedUserAction)
		{
			resolveButton.setVisibility(View.VISIBLE);
		}
		else
		{
			resolveButton.setVisibility(View.GONE);
		}
		mainTextView.setText(appStateCtrl.getDisplayedString());
	}
}
