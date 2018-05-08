package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.YrNoWeather;

import eu.benayoun.badass.Badass;
import eu.benayoun.badassweather.R;


/**
 * Created by PierreB on 26/07/2017.
 */

public class YrNoForecastUtils
{
	
	static public String getWeatherSymbolString(int symbolId)
	{
		StringBuilder stringBuilder = new StringBuilder();
		if (symbolId>100)
		{
			symbolId=symbolId%100;
		}
		switch(symbolId)
		{
			case 1: stringBuilder.append(Badass.getString(R.string.Sun));break;
			case 2: stringBuilder.append(Badass.getString(R.string.LightCloud));break;
			case 3: stringBuilder.append(Badass.getString(R.string.PartlyCloud));break;
			case 4: stringBuilder.append(Badass.getString(R.string.Cloud));break;
			case 5: stringBuilder.append(Badass.getString(R.string.LightRainSun));break;
			case 6: stringBuilder.append(Badass.getString(R.string.LightRainThunderSun));break;
			case 7: stringBuilder.append(Badass.getString(R.string.SleetSun));break;
			case 8: stringBuilder.append(Badass.getString(R.string.SnowSun));break;
			case 9: stringBuilder.append(Badass.getString(R.string.LightRain));break;
			case 10: stringBuilder.append(Badass.getString(R.string.Rain));break;
			case 11: stringBuilder.append(Badass.getString(R.string.RainThunder));break;
			case 12: stringBuilder.append(Badass.getString(R.string.Sleet));break;
			case 13: stringBuilder.append(Badass.getString(R.string.Snow));break;
			case 14: stringBuilder.append(Badass.getString(R.string.SnowThunder));break;
			case 15: stringBuilder.append(Badass.getString(R.string.Fog));break;
			case 20: stringBuilder.append(Badass.getString(R.string.SleetSunThunder));break;
			case 21: stringBuilder.append(Badass.getString(R.string.SnowSunThunder));break;
			case 22: stringBuilder.append(Badass.getString(R.string.LightRainThunder));break;
			case 23: stringBuilder.append(Badass.getString(R.string.SleetThunder));break;
			case 24: stringBuilder.append(Badass.getString(R.string.DrizzleThunderSun));break;
			case 25: stringBuilder.append(Badass.getString(R.string.RainThunderSun));break;
			case 26: stringBuilder.append(Badass.getString(R.string.LightSleetThunderSun));break;
			case 27: stringBuilder.append(Badass.getString(R.string.HeavySleetThunderSun));break;
			case 28: stringBuilder.append(Badass.getString(R.string.LightSnowThunderSun));break;
			case 29: stringBuilder.append(Badass.getString(R.string.HeavySnowThunderSun));break;
			case 30: stringBuilder.append(Badass.getString(R.string.DrizzleThunder));break;
			case 31: stringBuilder.append(Badass.getString(R.string.LightSleetThunder));break;
			case 32: stringBuilder.append(Badass.getString(R.string.HeavySleetThunder));break;
			case 33: stringBuilder.append(Badass.getString(R.string.LightSnowThunder));break;
			case 34: stringBuilder.append(Badass.getString(R.string.HeavySnowThunder));break;
			case 40: stringBuilder.append(Badass.getString(R.string.DrizzleSun));break;
			case 41: stringBuilder.append(Badass.getString(R.string.RainSun));break;
			case 42: stringBuilder.append(Badass.getString(R.string.LightSleetSun));break;
			case 43: stringBuilder.append(Badass.getString(R.string.HeavySleetSun));break;
			case 44: stringBuilder.append(Badass.getString(R.string.LightSnowSun));break;
			case 45: stringBuilder.append(Badass.getString(R.string.HeavySnowSun));break;
			case 46: stringBuilder.append(Badass.getString(R.string.Drizzle));break;
			case 47: stringBuilder.append(Badass.getString(R.string.LightSleet));break;
			case 48: stringBuilder.append(Badass.getString(R.string.HeavySleet));break;
			case 49: stringBuilder.append(Badass.getString(R.string.LightSnow));break;
			case 50: stringBuilder.append(Badass.getString(R.string.HeavySnow));break;
			default: stringBuilder.append("UNKNOWN: " +symbolId);break;
		}
		return stringBuilder.toString();
	}
}
