package eu.benayoun.badassweather.badass.ui.activity.screens.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.ui.layout.ReactiveLayout;
import eu.benayoun.badass.utility.os.time.BadassTimeUtils;
import eu.benayoun.badass.utility.ui.BadassViewUtils;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.model.ui.UIModel;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;


/**
 * Created by PierreB on 24/07/2017.
 */

public class HomeScreen extends ReactiveLayout
{
    SwipeRefreshLayout             swipeRefreshLayout;
    View                           contentView;
    HomeSwipeRefreshLayoutListener homeSwipeRefreshLayoutListener;

    AppStateLayout appStateLayout;

    LinearLayout mainLayout;
    TextView nominalTextView;
    TextView detailsTextView;
    Button aboutButton;
    boolean currentDisplayIsNominal=true;



    public HomeScreen(View mainViewArg)
    {
        super(mainViewArg);

        // swipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout)mainView;
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.app_primary_color);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_primary_text);
        homeSwipeRefreshLayoutListener = new HomeSwipeRefreshLayoutListener(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(homeSwipeRefreshLayoutListener);

        contentView = mainView.findViewById(R.id.screen_home_content);

        mainLayout = mainView.findViewById(R.id.screen_home_main);
        BadassViewUtils.doNotDisplayOnPortraitAndroidNavigationBar(mainLayout);

        nominalTextView = mainView.findViewById(R.id.screen_home_nominal_text);
        detailsTextView = mainView.findViewById(R.id.screen_home_about_text);
        detailsTextView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());


        View appStatusView = mainView.findViewById(R.id.screen_home_status_layout);
        BadassViewUtils.doNotDisplayOnLandscapeAndroidNavigationBar(appStatusView);
        BadassViewUtils.doNotDisplayOnPortraitAndroidNavigationBar(appStatusView);

        // notification switch
        final Switch notificationSwitch = mainView.findViewById(R.id.screen_home_switch_notification);
        notificationSwitch.setChecked(ThisApp.getModel().appPreferencesAndAssets.isUserWantsToDisplayNotification());
        notificationSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ThisApp.getNotificationAndWidgetsMngr().onNotificationSwitchClick();
            }
        });

        // about button
        aboutButton = mainView.findViewById(R.id.screen_home_about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {
            onAboutButtonClick(); }});

        // status

        appStateLayout = new AppStateLayout(appStatusView);
        addSubLayout(appStateLayout);

        listenTo(UIEvents.RESUME);
        listenTo(UIEvents.COMPUTE);
        listenTo(UIEvents.WEATHER_CHANGE);
    }

    @Override
    protected void updateMainContent(int eventId, long eventTimeInMs)
    {
        Badass.log("$$ HomeScreen updateMainContent: " + Badass.getEventName(eventId));
        homeSwipeRefreshLayoutListener.refresh(eventId);
        String  toDisplay ="";
        UIModel uIModel   = ThisApp.getModel().uIModel;
        if (uIModel.isEmpty())
        {
            toDisplay = uIModel.getNoDataString();
        }
        else
        {
            toDisplay = uIModel.getCurrentWeather();
            if (uIModel.getNextWeather().equals("")==false)
            {
                toDisplay+="\n\n"+uIModel.getNextWeather();
            }
        }
        nominalTextView.setText(toDisplay);
        setDetailedTextView();
    }

    /**
     * INTERNAL COOKING
     */

    protected void setDetailedTextView()
    {
        SpannableStringBuilder stringBuilder= new SpannableStringBuilder();
        stringBuilder.append(getLastLocationUpdate()).append("\n\n");
        stringBuilder.append(getLastForecastUpdate()).append("\n\n");
        stringBuilder.append(Badass.getText(R.string.home_notification_about));
        detailsTextView.setText(stringBuilder);

    }

    protected String getLastLocationUpdate()
    {
        long lastLocationUpdateInMs = ThisApp.getModel().bareModel.locationBareCache.getLastLocationUpdateInMs();
        if (lastLocationUpdateInMs==-1)
        {
            return Badass.getString(R.string.location_update_empty);
        }
        else
        {
            return Badass.getString(R.string.location_update_nominal, BadassTimeUtils.getNiceTimeString(lastLocationUpdateInMs));
        }
    }

    protected String getLastForecastUpdate()
    {
        long lastForecastUpdateInMs = ThisApp.getModel().bareModel.forecastBareCache.getLastForecastUpdateInMs();
        if (lastForecastUpdateInMs==-1)
        {
            return Badass.getString(R.string.forecast_update_empty);
        }
        else
        {
            return Badass.getString(R.string.forecast_update_nominal, BadassTimeUtils.getNiceTimeString(lastForecastUpdateInMs));
        }
    }

    protected void onAboutButtonClick()
    {
        currentDisplayIsNominal=!currentDisplayIsNominal;
        nominalTextView.setVisibility(getVisibility(currentDisplayIsNominal));
        detailsTextView.setVisibility(getVisibility(!currentDisplayIsNominal));
    }


    protected int getVisibility(boolean isVisible)
    {
        return (isVisible? View.VISIBLE : View.GONE);
    }


}
