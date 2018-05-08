package eu.benayoun.badassweather.badass.model;


import eu.benayoun.badassweather.badass.model.application.AppPreferencesAndAssets;
import eu.benayoun.badassweather.badass.model.application.AppStatusCtrl;
import eu.benayoun.badassweather.badass.model.bare.BareModel;
import eu.benayoun.badassweather.badass.model.ui.UIModel;

/**
 * Created by PierreB on 28/04/2016.
 */
public class Model
{
	/***
	 * PUBLISH OPTION
	 */

	// THE ASSET OF THE DEATH !
	public final  boolean isDevVersion = true;
	public final boolean isStrictMode = false;


	// STATUS
	public AppStatusCtrl appStatusCtrl;

	// PREFERENCES
	public AppPreferencesAndAssets appPreferencesAndAssets;


	// BARE
	public BareModel bareModel;

	// UI
	public UIModel uIModel;

	public Model()
	{
		appStatusCtrl = new AppStatusCtrl();
		appPreferencesAndAssets = new AppPreferencesAndAssets();
		uIModel = new UIModel();
		bareModel = new BareModel();
	}

	public void load()
	{
		uIModel.load();
		bareModel.load();
	}

}
