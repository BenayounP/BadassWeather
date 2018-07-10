package eu.benayoun.badassweather.badass.background.backgroundtasks.tasks.forecast.YrNoWeather;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.backgroundtask.tasks.BadassTaskCtrl;
import eu.benayoun.badass.utility.model.XmlParser;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.bare.forecast.AtomicBareForecastModel;


/**
 * Created by PierreB on 05/06/2017.
 */

public class YrNoForecastBgndTask
{
    long UTCOffsetInMs;
    long nextWeatherReportInMs =-1;
	ArrayList<AtomicBareForecastModel> oneHourForecastList;
	BadassTaskCtrl badassTaskCtrl;


	AtomicBareForecastModel readAtomicBareForecastModel;

	int readDurationInHours;




	public YrNoForecastBgndTask(BadassTaskCtrl badassTaskCtrl)
	{
		this.badassTaskCtrl = badassTaskCtrl;
	}

	public void getYrNoForecast(double latitude, double longitude)
	{
		String websiteStringResponse=getWebsiteStringResponse(latitude,longitude);
		if (websiteStringResponse!=null)
		{
			// MAIN METHOD
			setForecastData(websiteStringResponse);
			UTCOffsetInMs = BadassTimeUtils.getUTCOffsetInMs();
		}
	}


	/**
	 * INTERNAL COOKING
	 */

	protected String getWebsiteStringResponse(double latitude, double longitude)
	{
		String                url          = "https://api.met.no/weatherapi/locationforecast/1.9/?lat="+latitude+";lon="+longitude;
		RequestFuture<String> future       = RequestFuture.newFuture();
		StringRequest         request      = new StringRequest(url, future, future);
		RequestQueue          requestQueue = Volley.newRequestQueue(Badass.getApplicationContext());

		requestQueue.add(request);
		String websiteStringResponse=null;
		boolean thereWasAProblem = false;

		try
		{
			websiteStringResponse = future.get();
		}
		catch (InterruptedException | ExecutionException e)
		{
			thereWasAProblem = true;
			Badass.logInFile("##! YrNoForecastBgndCtrl exception in future.get(): " + e.toString());
			if(e.getCause() instanceof VolleyError)
			{
				VolleyError volleyError = (VolleyError)e.getCause();
				if (volleyError!=null && volleyError.networkResponse!=null)
				{
					Badass.logInFile("##! YrNoForecastBgndCtrl volleyError.networkResponse.statusCode: " + volleyError.networkResponse.statusCode);
				}
				else
				{
					Badass.logInFile("##! YrNoForecastBgndCtrl volleyError or volleyError.networkResponse is NULL");
				}
			}
			badassTaskCtrl.setGlobalProblemStringId(R.string.app_status_problem_forecast);
            badassTaskCtrl.onProblem();
		}
		if (thereWasAProblem == false && websiteStringResponse==null)
		{
			Badass.logInFile("##! " + Badass.getString(R.string.app_status_forecast_problem_server_null_data));
			badassTaskCtrl.setGlobalProblemStringId(R.string.app_status_forecast_problem_server_null_data);
			badassTaskCtrl.onProblem();
		}
		return websiteStringResponse;
	}

	protected void setForecastData(String websiteStringResponse)
	{
		XmlPullParser xmlPullParser     = XmlParser.getXmlParser(websiteStringResponse);

		int eventType = getNextEvent(xmlPullParser);
		Badass.logInFile("## YrNoForecastBgndCtrl set forecast from events");

		// PARSE FILE
		if (eventType!=-1)
		{
			oneHourForecastList = new ArrayList<>();

			while (eventType != -1 && eventType != XmlPullParser.END_DOCUMENT)
			{
				setForecastDataFromEvent(eventType, xmlPullParser);
				eventType = getNextEvent(xmlPullParser);
			}
		}

		if (eventType==-1)
		{
			Badass.logInFile("##! YrNoForecastBgndCtrl eventType==-1");
			badassTaskCtrl.setGlobalProblemStringId(R.string.app_status_problem_server_compute);
			badassTaskCtrl.onProblem();
		}
		else
		{
			Badass.logInFile("## YrNoForecastBgndCtrl OK, save data");
			// SAVE DATA
			saveForecastData();
		}
	}


	protected void setForecastDataFromEvent(int eventType, XmlPullParser xmlPullParser)
	{
		String tag = xmlPullParser.getName();
		if (eventType == XmlPullParser.START_TAG)
		{
			if (tag.equals("model"))
			{
				setNextRun(xmlPullParser);
			}
			if (tag.equals("time"))
			{
				setYrNoTimeForecastTimeData(xmlPullParser);
			}
			else if (tag.equals("symbol"))
			{
				setSymbolId(xmlPullParser);
			}
		}
		else if (eventType == XmlPullParser.END_TAG)
		{
			if (tag.equals("time")) addForecastToList();
		}
	}

	protected void setNextRun(XmlPullParser xmlParser)
	{
		int attributesCount = xmlParser.getAttributeCount();
		for (int i = 0; i < attributesCount; i++)
		{
			if (xmlParser.getAttributeName(i).equals("nextrun"))
			{
				nextWeatherReportInMs = getTimeInMs(xmlParser.getAttributeValue(i));
				break;
			}
		}
	}

	protected void setYrNoTimeForecastTimeData(XmlPullParser xmlParser)
	{
		int attributesCount = xmlParser.getAttributeCount();
		for (int i = 0; i < attributesCount; i++)
		{
			if (xmlParser.getAttributeName(i).equals("from"))
			{
				readAtomicBareForecastModel = new AtomicBareForecastModel();
				readAtomicBareForecastModel.setStartTime(getTimeInMs(xmlParser.getAttributeValue(i)));
			}
			if (xmlParser.getAttributeName(i).equals("to"))
			{
				readAtomicBareForecastModel.setEndTime(getTimeInMs(xmlParser.getAttributeValue(i)));
				readDurationInHours = readAtomicBareForecastModel.getUTCDurationInMs().getDurationInHours();
			}
		}
	}

	protected long getTimeInMs(String timeString)
	{
		int year = Integer.valueOf(timeString.substring(0,4));
		int month = Integer.valueOf(timeString.substring(5,7));
		int dayOfMonth = Integer.valueOf(timeString.substring(8,10));
		int hour = Integer.valueOf(timeString.substring(11,13));
		return getMsFromCalendar(hour,dayOfMonth,month,year)-UTCOffsetInMs;
	}


	protected void setSymbolId(XmlPullParser xmlParser)
	{
		String attributeName;
		String attributeValue;

		int attributesCount = xmlParser.getAttributeCount();
		for (int i = 0; i < attributesCount; i++)
		{
			attributeName = xmlParser.getAttributeName(i);
			attributeValue = xmlParser.getAttributeValue(i);
			if (attributeName.equals("number"))
			{
				readAtomicBareForecastModel.setWeatherSymbol(Integer.valueOf(attributeValue));
				break;
			}
		}
	}


	protected void addForecastToList()
	{
		if (readDurationInHours == 1)
		{
			oneHourForecastList.add(readAtomicBareForecastModel);
		}
	}


	protected long getMsFromCalendar(int hourOfDay, int dayOfMonth, int realMonthOfYear, int year)
	{
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		calendar.set(Calendar.MONTH, realMonthOfYear-1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar.getTimeInMillis();
	}

	protected int getNextEvent(XmlPullParser xmlParser)
	{
		int eventType=-1;
		try
		{
			try
			{
				xmlParser.next();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			eventType = xmlParser.getEventType();
		} catch (XmlPullParserException e)
		{
			Badass.log("##! YrNoForecastBgndCtrl XmlPullParserException "+ e.toString());
		}
		return eventType;
	}

	protected void saveForecastData()
	{
		ThisApp.getModel().bareModel.forecastBareCache.updateAndSave(oneHourForecastList,nextWeatherReportInMs);
        badassTaskCtrl.waitForNextCall(nextWeatherReportInMs);
		ThisApp.getBgndTaskCtrl().updateUiModel();
	}

}
