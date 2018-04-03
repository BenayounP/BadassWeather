package eu.benayoun.badassweather.badass;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.App;
import eu.benayoun.badassweather.badass.backgroundworker.AppBackgroundWorker;
import eu.benayoun.badassweather.badass.data.DataContainer;
import eu.benayoun.badassweather.badass.ui.uievents.UIEvents;


/**
 * Created by PierreB on 18/01/2018.
 */

public class AppBadass
{
	static protected AppBackgroundWorker appBackgroundWorker;
	static protected DataContainer dataContainer;

	public static void init(App appArg)
	{
		Badass.init(appArg, new UIEvents());
		dataContainer = new DataContainer();
		appBackgroundWorker = new AppBackgroundWorker();
		Badass.setBackgroundWorker(appBackgroundWorker);
	}

	// GETTERS

	public static AppBackgroundWorker getAppBackgroundWorker()
	{
		return appBackgroundWorker;
	}

	public static DataContainer getDataContainer()
	{
		return dataContainer;
	}
}
