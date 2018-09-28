package eu.benayoun.badassweather.model.application;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionsMngr;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.background.AppBadassJobList;
import eu.benayoun.badassweather.ui.events.UIEvents;

import static eu.benayoun.badassweather.model.application.AppStateCtrl.State.*;




/**
 * Created by PierreB on 23/12/2017.
 */

public class AppStateCtrl
{
    enum State {
        IDLE,
        JOB_RUNNING,
        PERMISSION_FINE_LOCATION_NOT_GIVEN,
        LOCATION_PROBLEM,  
        FORECAST_PROBLEM
    }
     

    // displayed String and status
    protected String displayedString = null;
    protected  State currentState;


    public AppStateCtrl()
    {
        displayedString = Badass.getString(R.string.app_state_init);
        currentState = JOB_RUNNING;
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

    public void setJobRunning(String displayedStringArg)
    {
        currentState = JOB_RUNNING;
        displayedString = displayedStringArg;
        Badass.broadcastUIEvent(UIEvents.BACKGROUND_EVENT);
    }

    public void updateState()
    {
        AppBadassJobList thisAppBgndMngr               = ThisApp.getAppBadassJobList();
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
                statusString = Badass.getString(R.string.app_state_next_weather_report, BadassTimeUtils.getNiceTimeString(nextWeatherReportInMs));
            }
            setOkStatus(statusString);
        }
        Badass.log("%% updateState: " + currentState.name());
        Badass.broadcastUIEvent(UIEvents.app_state_CHANGE);
    }

    // ACTION
    public void onUserClick()
    {
        if (currentState== PERMISSION_FINE_LOCATION_NOT_GIVEN)
        {
            if (ThisApp.getAppBadassJobList().getFusedLocationAPIConnectionCtrl().getBadassPermissionCtrl().isUserHasCheckedNeverAskAgain())
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
        currentState = IDLE;
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
