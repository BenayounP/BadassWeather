package eu.benayoun.badassweather.ui.activity.screens.home;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.layout.ReactiveLayout;
import eu.benayoun.badass.utility.ui.BadassViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.model.application.AppStateCtrl;
import eu.benayoun.badassweather.ui.events.UIEvents;


/**
 * Created by PierreB on 24/07/2017.
 */

class AppStateLayout extends ReactiveLayout
{
	private TextView mainTextView;
	private Button   resolveButton;

	public AppStateLayout(View mainViewArg)
	{
		super(mainViewArg);
		mainTextView = mainView.findViewById(R.id.screen_home_state_text);
		resolveButton = mainView.findViewById(R.id.screen_home_state_resolve);
		resolveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				ThisApp.getModel().appStateCtrl.onUserClick();
			}
		});

		listenTo(UIEvents.BACKGROUND_EVENT);
		listenTo(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
		listenTo(UIEvents.app_state_CHANGE);
		listenTo(UIEvents.RESUME);
	}

    /**
	 * INTERNAL COOKING
	 */



	@Override
	protected void updateMainContent(int eventId, long eventTimeInMs)
	{
		AppStateCtrl appStateCtrl = ThisApp.getModel().appStateCtrl;
		boolean       weNeedUserAction =false;
		if (appStateCtrl.thereIsProblem())
		{
			mainView.setBackgroundResource(R.drawable.gradient_background);
			BadassViewUtils.setGradientBackgroundView(mainView,Badass.getColor(R.color.app_problem_1), Badass.getColor(R.color.app_problem_2));
			if (appStateCtrl.isFineLocationNotGiven())
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
