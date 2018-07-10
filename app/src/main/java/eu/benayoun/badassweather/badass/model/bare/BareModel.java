package eu.benayoun.badassweather.badass.model.bare;


import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;
import eu.benayoun.badassweather.badass.model.bare.forecast.ForecastBareCache;

/**
 * Created by PierreB on 04/02/2018.
 */

public class BareModel
{
	public LocationBareCache locationBareCache;
	public ForecastBareCache forecastBareCache;

	public BareModel()
	{
		locationBareCache = new LocationBareCache();
		forecastBareCache = new ForecastBareCache();
	}

	public void load()
	{
		locationBareCache.load();
		forecastBareCache.load();
	}
}
