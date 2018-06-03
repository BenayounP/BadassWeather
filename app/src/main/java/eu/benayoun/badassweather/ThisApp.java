package eu.benayoun.badassweather;

import android.app.Application;
import android.os.StrictMode;
import com.crashlytics.android.Crashlytics;
import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.badass.background.ThisAppBgndMngr;
import eu.benayoun.badassweather.badass.model.Model;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;
import eu.benayoun.badassweather.badass.ui.notificationsandwidgets.NotificationAndWidgetsMngr;
import io.fabric.sdk.android.Fabric;


/**
 * Created by PierreB on 28/04/2016.
 */
public class ThisApp extends Application
{
	static protected ThisAppBgndMngr thisAppBgndMngr;
	static protected Model           model;
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
			Badass.allowLogInFile("badassweather_log.txt");
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
			Badass.finalLog("%%!!!(model)##!!!(bgnd)$$!!!(UI)******* INITIALISATION ************************* "+ BadassTimeUtils.getDateString(BadassTimeUtils.getCurrentTimeInMs())+" ***********************************");

			// Badass
			Fabric.with(this, new Crashlytics());
		}

		// Bgnd Manager
		thisAppBgndMngr = new ThisAppBgndMngr();
	}

	public static ThisAppBgndMngr getThisAppBgndMngr()
	{
		return thisAppBgndMngr;
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
