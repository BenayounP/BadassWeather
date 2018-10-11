package eu.benayoun.badassweather.model.application;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.os.permissions.BadassPermissionsMngr;
import eu.benayoun.badass.utility.os.time.BadassUtilsTime;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.background.AppBadassJobList;
import eu.benayoun.badassweather.ui.events.UIEvents;

import static eu.benayoun.badassweather.model.application.AppStateCtrl.State.FORECAST_PROBLEM;
import static eu.benayoun.badassweather.model.application.AppStateCtrl.State.IDLE;
import static eu.benayoun.badassweather.model.application.AppStateCtrl.State.JOB_RUNNING;
import static eu.benayoun.badassweather.model.application.AppStateCtrl.State.LOCATION_PROBLEM;
import static eu.benayoun.badassweather.model.application.AppStateCtrl.State.PERMISSION_FINE_LOCATION_NOT_GIVEN;




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
    private String displayedString;
    private State currentState;


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
                statusString = Badass.getString(R.string.app_state_next_weather_report, BadassUtilsTime.getNiceTimeString(nextWeatherReportInMs));
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


    private void setOkStatus(String displayedStringArg)
    {
        currentState = IDLE;
        displayedString = displayedStringArg;
    }

    /**
     * INTERNAL COOKING
     */

    private void setLocationProblem(String displayedStringArg)
    {
        currentState = LOCATION_PROBLEM;
        displayedString = displayedStringArg;
    }

    private void setPermissionFineLocationNotGiven(String displayedStringArg)
    {
        currentState = PERMISSION_FINE_LOCATION_NOT_GIVEN;
        displayedString = displayedStringArg;
    }

    private void setForecastProblem(String displayedStringArg)
    {
        currentState = FORECAST_PROBLEM;
        displayedString = displayedStringArg;
    }
}
