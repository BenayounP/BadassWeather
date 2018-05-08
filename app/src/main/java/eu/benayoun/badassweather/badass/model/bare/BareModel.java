package eu.benayoun.badassweather.badass.model.bare;


import eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.location.LocationBareCache;
import eu.benayoun.badassweather.badass.model.bare.forecast.ForecastBareCacheContainer;

/**
 * Created by PierreB on 04/02/2018.
 */

public class BareModel
{
	public LocationBareCache          locationBareCache;
	public ForecastBareCacheContainer forecastBareCacheContainer;

	public BareModel()
	{
		locationBareCache = new LocationBareCache();
		forecastBareCacheContainer = new ForecastBareCacheContainer();
	}

	public void load()
	{
		locationBareCache.load();
		forecastBareCacheContainer.load();
	}
}
