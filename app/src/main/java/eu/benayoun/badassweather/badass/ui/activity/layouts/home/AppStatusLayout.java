package eu.benayoun.badassweather.badass.ui.activity.layouts.home;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.layout.RefreshableLayoutTemplate;
import eu.benayoun.badass.utility.ui.BadassViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.application.AppStatusCtrl;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

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
				ThisApp.getModel().appStatusCtrl.onUserAction();
			}
		});
		dismissButton = mainView.findViewById(R.id.screen_home_status_dismiss);
		dismissButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{
			ThisApp.getModel().appStatusCtrl.onUserDismiss();
		}
	});
		addEventTrigger(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
		addEventTrigger(UIEvents.APP_STATUS_CHANGE);
		addEventTrigger(UIEvents.RESUME);
	}

	/**
	 * INTERNAL COOKING
	 */

	@Override
	protected void internalRefresh(int eventId, long eventTimeInMs)
	{
		AppStatusCtrl appStatusCtrl    = ThisApp.getModel().appStatusCtrl;
		boolean       weNeedUserAction =true;
		if (appStatusCtrl.thereIsProblem())
		{
			mainView.setBackgroundResource(R.drawable.gradient_background);
			BadassViewUtils.setGradientBackgroundView(mainView,Badass.getColor(R.color.app_problem_1), Badass.getColor(R.color.app_problem_2));
			if (appStatusCtrl.thereIsFineLocationPermissionPb())
			{
				weNeedUserAction = false;
			}
		}
		else
		{
			mainView.setBackgroundResource(0);
		}
		if (weNeedUserAction)
		{
			resolveButton.setVisibility(View.GONE);
			dismissButton.setVisibility(View.GONE);
		}
		else
		{
			resolveButton.setVisibility(View.VISIBLE);
			dismissButton.setVisibility(View.VISIBLE);
		}
		mainTextView.setText(appStatusCtrl.getDisplayedString());
	}
}
