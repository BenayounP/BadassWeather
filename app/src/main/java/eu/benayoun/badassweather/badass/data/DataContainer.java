package eu.benayoun.badassweather.badass.data;


import eu.benayoun.badassweather.badass.data.application.AppPreferencesAndAssets;
import eu.benayoun.badassweather.badass.data.application.AppStatusManager;
import eu.benayoun.badassweather.badass.data.cache.bare.BareDataContainer;
import eu.benayoun.badassweather.badass.data.cache.ui.UIDataContainer;

/**
 * Created by PierreB on 28/04/2016.
 */
public class DataContainer
{
	/***
	 * PUBLISH OPTION
	 */

	// THE ASSET OF THE DEATH !
	public final  boolean isDevVersion = true;
	public final boolean isStrictMode = false;


	// STATUS
	public AppStatusManager appStatusManager;

	// PREFERENCES
	public AppPreferencesAndAssets appPreferencesAndAssets;

	// BARE DATA
	public BareDataContainer bareDataContainer;

	// UI DATA
	public UIDataContainer uIDataContainer;

	public DataContainer()
	{
		appStatusManager = new AppStatusManager();
		appPreferencesAndAssets = new AppPreferencesAndAssets();

		bareDataContainer = new BareDataContainer();
		uIDataContainer = new UIDataContainer();
	}

	public void load()
	{
		uIDataContainer.load();
		bareDataContainer.load();
	}

}
