package eu.benayoun.badassweather.initialisation;

import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.time.TimeUtils;
import eu.benayoun.badass.utility.ui.log.LogConfig;
import eu.benayoun.badassweather.App;
import eu.benayoun.badassweather.badass.AppBadass;
import eu.benayoun.badassweather.badass.data.DataContainer;
import io.fabric.sdk.android.Fabric;


/**
 * Created by PierreB on 28/04/2016.
 */
public class InitManager
{
	static public void init(App appArg)
	{
		// leakCanary
//		if (LeakCanary.isInAnalyzerProcess(thisApplication)) {
//			// This process is dedicated to LeakCanary for heap analysis.
//			// You should not reset your app in this process.
//			return;
//		}
//		AppBadass.getDataContainer().initLeakCanaryRefWatcher(thisApplication);


		// BADASS
		AppBadass.init(appArg);

		// enable logging
		if (AppBadass.getDataContainer().isDevVersion)
		{
			LogConfig.enableLogging(AppBadass.getDataContainer().isDevVersion);
			LogConfig.allowLogInFile("badassweather_log.txt");
			LogConfig.defineTag("BADASS_DEBUG");
			if (AppBadass.getDataContainer().isStrictMode)
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
			Fabric.with(appArg, new Crashlytics());

		}

	}

	/**
	 * INTERNAL COOKING
	 */

}
