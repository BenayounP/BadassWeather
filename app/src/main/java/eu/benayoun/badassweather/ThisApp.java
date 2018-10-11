package eu.benayoun.badassweather;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.time.BadassUtilsTime;
import eu.benayoun.badassweather.background.AppBadassJobList;
import eu.benayoun.badassweather.background.AppThreadListener;
import eu.benayoun.badassweather.model.Model;
import eu.benayoun.badassweather.ui.events.UIEvents;
import eu.benayoun.badassweather.ui.notificationsandwidgets.NotificationAndWidgetsMngr;
import io.fabric.sdk.android.Fabric;


/**
 * Created by PierreB on 28/04/2016.
 */
public class ThisApp extends Application
{
	static private AppBadassJobList appBadassJobList;
	private static Model model;
	private static NotificationAndWidgetsMngr notificationAndWidgetsMngr;


	@Override
	public void onCreate()
	{
		super.onCreate();
		// BADASS
		Badass.init(this, new UIEvents());

		// Notification and wdgets
		notificationAndWidgetsMngr =new NotificationAndWidgetsMngr();
		Badass.setNotificationAndWidgetsEventsLister(notificationAndWidgetsMngr);

		// Model
		model = new Model();

		// enable logging
		if (model.isDevVersion)
		{
			Badass.enableLogging();
			Badass.beVerbose();
			Badass.defineTag("BADASS_DEBUG");
			if (model.isStrictMode)
			{
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
						.detectAll()
						.penaltyLog()
						.build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
						.detectAll()
						.penaltyLog()
						.build());
			}
			Badass.finalLog("%%(model)##(bgnd)$$(UI)!!!******* INITIALISATION "+ BadassUtilsTime.getDateString(BadassUtilsTime.getCurrentTimeInMs())+" ***********************************************");

			// Crashlytics
			Fabric.with(this, new Crashlytics());
		}

		// THREAD
		appBadassJobList = new AppBadassJobList();
		Badass.setThreadMngr(appBadassJobList.getBadassJobsCtrl(),new AppThreadListener());
		Badass.launchBadassThread();
	}

	public static AppBadassJobList getAppBadassJobList()
	{
		return appBadassJobList;
	}

	public static Model getModel()
	{
		return model;
	}

	public static NotificationAndWidgetsMngr getNotificationAndWidgetsMngr()
	{
		return notificationAndWidgetsMngr;
	}

}
