package eu.benayoun.badassweather.badass.model.application;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionsMngr;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.background.AppBadassJobList;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

import static eu.benayoun.badassweather.badass.model.application.AppStateCtrl.State.*;


/**
 * Created by PierreB on 23/12/2017.
 */

public class AppStateCtrl
{
    static enum State {
        OK,
        BGND_TASKS_ONGOING,
        PERMISSION_FINE_LOCATION_NOT_GIVEN,
        LOCATION_PROBLEM,  
        FORECAST_PROBLEM
    }
     

    // displayed String and status
    protected String displayedString = null;
    protected  State currentState;


    public AppStateCtrl()
    {
        displayedString = Badass.getString(R.string.app_status_init);
        currentState = BGND_TASKS_ONGOING;
    }

    // GETTERS

    public String getDisplayedString()
    {
        return displayedString;
    }

    public boolean thereIsProblem()
    {
        return currentState == LOCATION_PROBLEM || currentState == PERMISSION_FINE_LOCATION_NOT_GIVEN || currentState == FORECAST_PROBLEM;
    }

    public boolean isFineLocationNotGiven()
    {
        return currentState == PERMISSION_FINE_LOCATION_NOT_GIVEN;
    }

    // SETTERS

    public void setBgndTaskOngoing(String displayedStringArg)
    {
        currentState = BGND_TASKS_ONGOING;
        displayedString = displayedStringArg;
        Badass.broadcastUIEvent(UIEvents.COMPUTE);
    }

    public void updateState()
    {
        AppBadassJobList thisAppBgndMngr               = ThisApp.getAppWorkersCtrl();
        String          locationPbString              = thisAppBgndMngr.getLocationPbString();
        String          fusedLocationApiProblemString = thisAppBgndMngr.getFusedLocationAPIPbString();
        String          forecastProblemString         = thisAppBgndMngr.getForecastPbString();

        if (fusedLocationApiProblemString != null)
        {
            setPermissionFineLocationNotGiven(fusedLocationApiProblemString);
            Badass.broadcastUIEvent(UIEvents.PERMISSION_STATUS_CHANGE_RESULT);
        }
        else if (locationPbString != null)
        {
            setLocationProblem(locationPbString);
        }
        else if (forecastProblemString != null)
        {
            setForecastProblem(forecastProblemString);
        }
        else // NO PROBLEM_RESOLUTION !!
        {

            long nextWeatherReportInMs = ThisApp.getModel().bareModel.forecastBareCache.getNextWeatherReportInMs();
            String statusString ="";
            if (nextWeatherReportInMs!=-1)
            {
                statusString = Badass.getString(R.string.app_status_next_weather_report, BadassTimeUtils.getNiceTimeString(nextWeatherReportInMs));
            }
            setOkStatus(statusString);
        }
        Badass.log("%% updateState: " + currentState.name());
        Badass.broadcastUIEvent(UIEvents.APP_STATUS_CHANGE);
    }

    // ACTION
    public void onUserClick()
    {
        if (currentState== PERMISSION_FINE_LOCATION_NOT_GIVEN)
        {
            if (ThisApp.getAppWorkersCtrl().getFusedLocationAPIConnectionBgndCtrl().getBadassPermissionCtrl().isUserHasCheckedNeverAskAgain())
            {
                BadassPermissionsMngr.goToSettingsPage();
            }
            else
            {
                Badass.broadcastUIEvent(UIEvents.ASK_FINE_LOCATION_PERMISSION);
            }
        }
    }


    public void setOkStatus(String displayedStringArg)
    {
        currentState = OK;
        displayedString = displayedStringArg;
    }

    /**
     * INTERNAL COOKING
     */

    protected void setLocationProblem(String displayedStringArg)
    {
        currentState = LOCATION_PROBLEM;
        displayedString = displayedStringArg;
    }

    protected void setPermissionFineLocationNotGiven(String displayedStringArg)
    {
        currentState = PERMISSION_FINE_LOCATION_NOT_GIVEN;
        displayedString = displayedStringArg;
    }

    protected void setForecastProblem(String displayedStringArg)
    {
        currentState = FORECAST_PROBLEM;
        displayedString = displayedStringArg;
    }
}
