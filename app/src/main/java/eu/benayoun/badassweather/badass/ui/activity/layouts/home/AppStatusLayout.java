package eu.benayoun.badassweather.badass.ui.activity.layouts.home;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.layout.RefreshableLayoutTemplate;
import eu.benayoun.badass.utility.ui.ViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.data.application.AppStatusManager;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;

/**
 * Created by PierreB on 24/07/2017.
 */

public class AppStatusLayout extends RefreshableLayoutTemplate
{
	TextView mainTextView;
	Button   resolveButton;
	Button dismissButton;

	public AppStatusLayout(View mainViewArg)
	{
		super(mainViewArg);
		mainTextView = mainView.findViewById(R.id.screen_home_status_text);
		resolveButton = mainView.findViewById(R.id.screen_home_status_resolve);
		resolveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				AppBadass.getDataContainer().appStatusManager.onUserAction();
			}
		});
		dismissButton = mainView.findViewById(R.id.screen_home_status_dismiss);
		dismissButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{
			AppBadass.getDataContainer().appStatusManager.onUserDismiss();
		}
	});
		mainTextView = mainView.findViewById(R.id.screen_home_status_text);
		addEventTrigger(UIEvents.UI_EVENT_PERMISSION_STATUS_CHANGE_RESULT);
		addEventTrigger(UIEvents.UI_EVENT_APP_STATUS_CHANGE);
		addEventTrigger(UIEvents.UI_EVENT_RESUME);
	}

	/**
	 * INTERNAL COOKING
	 */

	@Override
	protected void internalRefresh(int eventId, long eventTimeInMs)
	{
		AppStatusManager appStatusManager = AppBadass.getDataContainer().appStatusManager;
		boolean thereIsNoSolution=true;
		if (appStatusManager.thereIsProblem())
		{
			mainView.setBackgroundResource(R.drawable.gradient_background);
			ViewUtils.setGradientBackgroundView(mainView,Badass.getColor(R.color.app_problem_1), Badass.getColor(R.color.app_problem_2));
			if (appStatusManager.thereIsFineLocationPermissionPb())
			{
				thereIsNoSolution = false;
			}
		}
		else
		{
			mainView.setBackgroundResource(0);
		}
		if (thereIsNoSolution)
		{
			Badass.log("$$!! AppStatusLayout there is no solution");
			resolveButton.setVisibility(View.GONE);
			dismissButton.setVisibility(View.GONE);
		}
		else
		{
			Badass.log("$$!! AppStatusLayout there is solution");
			resolveButton.setVisibility(View.VISIBLE);
			dismissButton.setVisibility(View.VISIBLE);
		}
		Badass.log("$$!! getDisplayedString" + appStatusManager.getDisplayedString());
		mainTextView.setText(appStatusManager.getDisplayedString());
	}
}
