package eu.benayoun.badassweather;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.badass.background.AppBadassJobList;
import eu.benayoun.badassweather.badass.model.Model;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;
import eu.benayoun.badassweather.badass.ui.notificationsandwidgets.NotificationAndWidgetsMngr;
import io.fabric.sdk.android.Fabric;


/**
 * Created by PierreB on 28/04/2016.
 */
public class ThisApp extends Application
{
	static private AppBadassJobList appWorkersCtrl;
	static protected Model model;
	static protected NotificationAndWidgetsMngr notificationAndWidgetsMngr;


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
			Badass.finalLog("%%(model)##(bgnd)$$(UI)!!!******* INITIALISATION "+ BadassTimeUtils.getDateString(BadassTimeUtils.getCurrentTimeInMs())+" ***********************************************");

			// Crashlytics
			Fabric.with(this, new Crashlytics());
		}

		// Bgnd Manager
		appWorkersCtrl = new AppBadassJobList();
	}

	public static AppBadassJobList getAppWorkersCtrl()
	{
		return appWorkersCtrl;
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
