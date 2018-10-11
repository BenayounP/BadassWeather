package eu.benayoun.badassweather.model;


import eu.benayoun.badassweather.model.application.AppPreferencesAndAssets;
import eu.benayoun.badassweather.model.application.AppStateCtrl;
import eu.benayoun.badassweather.model.bare.BareModel;
import eu.benayoun.badassweather.model.ui.UIModel;

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
	public AppStateCtrl appStateCtrl;

	// PREFERENCES
	public AppPreferencesAndAssets appPreferencesAndAssets;


	// BARE
	public BareModel bareModel;

	// UI
	public UIModel uIModel;

	public Model()
	{
		appStateCtrl = new AppStateCtrl();
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
