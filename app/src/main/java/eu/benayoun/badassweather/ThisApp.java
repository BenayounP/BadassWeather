package eu.benayoun.badassweather;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.time.TimeUtils;
import eu.benayoun.badassweather.badass.background.ThisAppAppBgndManager;
import eu.benayoun.badassweather.badass.data.DataContainer;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;
import io.fabric.sdk.android.Fabric;


/**
 * Created by PierreB on 28/04/2016.
 */
public class ThisApp extends Application
{
	static protected ThisAppAppBgndManager thisAppBgndManager;
	static protected DataContainer         dataContainer;

	@Override
	public void onCreate()
	{
		super.onCreate();
		// BADASS
		Badass.init(this, new UIEvents());
		dataContainer = new DataContainer();

		// enable logging
		if (dataContainer.isDevVersion)
		{
			Badass.enableLogging();
			Badass.allowLogInFile("badassweather_log.txt");
			Badass.defineTag("BADASS_DEBUG");
			if (dataContainer.isStrictMode)
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
			Badass.finalLog("%%!!!(model)##!!!(bgnd/ctrler)$$!!!(UI)******* INITIALISATION ************************* "+ TimeUtils.getDateString(TimeUtils.getCurrentTimeInMs())+" ***********************************");

			// Badass
			Fabric.with(this, new Crashlytics());
		}
		thisAppBgndManager = new ThisAppAppBgndManager();
	}

	public static ThisAppAppBgndManager getThisAppBgndManager()
	{
		return thisAppBgndManager;
	}

	public static DataContainer getDataContainer()
	{
		return dataContainer;
	}
}
