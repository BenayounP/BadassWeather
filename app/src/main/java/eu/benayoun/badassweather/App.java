package eu.benayoun.badassweather;

import android.app.Application;

import eu.benayoun.badassweather.initialisation.InitManager;


/**
 * Created by PierreB on 28/04/2016.
 */
public class App extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		InitManager.init(this);
	}
}
