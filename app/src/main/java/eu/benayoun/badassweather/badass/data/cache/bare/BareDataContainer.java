package eu.benayoun.badassweather.badass.data.cache.bare;


import eu.benayoun.badassweather.badass.data.cache.bare.location.LocationCache;

/**
 * Created by PierreB on 04/02/2018.
 */

public class BareDataContainer
{
	public LocationCache locationCache;


	public BareDataContainer()
	{
		locationCache = new LocationCache();
	}

	public void load()
	{
		locationCache.load();
	}
}
