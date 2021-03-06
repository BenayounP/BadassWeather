package eu.benayoun.badassweather.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.activity.BadassActivity;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionCtrl;
import eu.benayoun.badass.utility.os.time.BadassUtilsTime;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.ui.activity.receivers.ActivityReceiverManager;
import eu.benayoun.badassweather.ui.activity.screenmanagement.ScreenManager;
import eu.benayoun.badassweather.ui.events.UIEvents;



/**
 * Created by PierreB on 01/05/2016.
 */
public class MainActivity extends BadassActivity
{
	private ScreenManager screenManager;
	private ActivityReceiverManager activityReceiverManager = null;

	static {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	/**
	 * LIFE CYCLE
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		manageAndroidStatusBar();

		screenManager = new ScreenManager((FrameLayout)findViewById(android.R.id.content));

		activityReceiverManager = new ActivityReceiverManager();
		activityReceiverManager.registerInActivity(this);
	}



	@Override
	protected void onDestroy()
	{
		screenManager.onDestroy();
		activityReceiverManager.unregisterInActivity();
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(Badass.isAllowLogInFile())
		{
			BadassPermissionCtrl logInFilePermissionManager = Badass.getLogInFilePermissionCtrl();
			if (logInFilePermissionManager.isPermissionGranted()==false)
			{
				Badass.requestPermission(this,logInFilePermissionManager);
			}
		}
		onEvent(UIEvents.RESUME, BadassUtilsTime.getCurrentTimeInMs());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		onEvent(UIEvents.RESUME, BadassUtilsTime.getCurrentTimeInMs());
	}

	public void onEvent(int eventId, long onEventTimeInMs)
	{
		if (eventId == UIEvents.ASK_FINE_LOCATION_PERMISSION)
		{
			Badass.requestPermission(this, ThisApp.getAppBadassJobList().getFusedLocationAPIPermission());
		}
		else
		{
			if (eventId == UIEvents.SCREEN_ON) eventId = UIEvents.RESUME;
			if (screenManager != null)
			{
				screenManager.onEvent(eventId, onEventTimeInMs);
			}
		}
	}

	/**
	 * INTERNAL COOKING
	 */

    private void manageAndroidStatusBar()
	{
		if (Badass.androidVersionSuperiorOrEqualTo(Build.VERSION_CODES.LOLLIPOP))
		{
			setStatusBarColor();
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor()
	{
		//iab_status
		Window window = getWindow();

		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

		// finally change the colorId
		window.setStatusBarColor(Badass.getColor(R.color.app_state_bar_color));
	}

}
