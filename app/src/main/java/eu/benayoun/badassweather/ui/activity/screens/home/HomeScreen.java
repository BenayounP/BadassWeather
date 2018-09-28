package eu.benayoun.badassweather.ui.activity.screens.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.LinkMovementMethod;
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
import eu.benayoun.badassweather.model.ui.UIModel;
import eu.benayoun.badassweather.ui.events.UIEvents;



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
    TextView forecastTextView;
    LinearLayout moreLayout;
    TextView moreTextView;
    TextView aboutTextView;
    Button moreButton;
    boolean forecastIsDisplayed =true;


    public HomeScreen(View mainViewArg)
    {
        super(mainViewArg);

        // swipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout)mainView;
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.app_primary_color);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_primary_text);
        homeSwipeRefreshLayoutListener = new HomeSwipeRefreshLayoutListener(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(homeSwipeRefreshLayoutListener);

        // content
        contentView = mainView.findViewById(R.id.screen_home_content);

        mainLayout = mainView.findViewById(R.id.screen_home_main);

        mainLayout.setPadding(0,BadassViewUtils.getAndroidStatusBarHeight()+mainLayout.getPaddingTop(),0,0);
        BadassViewUtils.doNotDisplayOn_landscape_AndroidNavigationBar(mainLayout);

        forecastTextView = mainView.findViewById(R.id.screen_home_forecast_text);
        moreTextView = mainView.findViewById(R.id.screen_home_more_text);

        //more
        moreLayout = mainView.findViewById(R.id.screen_home_more_layout);

        aboutTextView = mainView.findViewById(R.id.screen_home_about_text);
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
        aboutTextView.setLinkTextColor(Badass.getColor(R.color.app_accent_text));

        View appStatusView = mainView.findViewById(R.id.screen_home_state_layout);
        BadassViewUtils.doNotDisplayOn_portrait_AndroidNavigationBar(appStatusView);
        BadassViewUtils.doNotDisplayOn_landscape_AndroidNavigationBar(appStatusView);

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
        moreButton = mainView.findViewById(R.id.screen_home_about_button);
        moreButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {
            onAboutButtonClick(); }});

        // status

        appStateLayout = new AppStateLayout(appStatusView);
        addSubLayout(appStateLayout);

        listenTo(UIEvents.RESUME);
        listenTo(UIEvents.BACKGROUND_EVENT);
        listenTo(UIEvents.WEATHER_CHANGE);
    }

    @Override
    protected void updateMainContent(int eventId, long eventTimeInMs)
    {
        homeSwipeRefreshLayoutListener.refresh(eventId);
        setForecastView();
        setMoreTextView();
    }

    /**
     * INTERNAL COOKING
     */

    protected void setForecastView()
    {
        String  toDisplay ="";
        UIModel uIModel   = ThisApp.getModel().uIModel;
        if (uIModel.isEmpty())
        {
            toDisplay = uIModel.getNoDataString();
        }
        else
        {
            toDisplay = Badass.getString(R.string.tmp_space)+ uIModel.getCurrentWeather();
            if (uIModel.getNextWeather().equals("")==false)
            {
                toDisplay+="\n\n"+uIModel.getNextWeather();
            }
        }
        forecastTextView.setText(toDisplay);
    }

    protected void setMoreTextView()
    {
       moreTextView.setText(getLastLocationUpdate() + "\n\n"+getLastForecastUpdate());

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
        forecastIsDisplayed =!forecastIsDisplayed;
        forecastTextView.setVisibility(getVisibility(forecastIsDisplayed));
        moreLayout.setVisibility(getVisibility(!forecastIsDisplayed));
    }


    protected int getVisibility(boolean isVisible)
    {
        return (isVisible? View.VISIBLE : View.GONE);
    }
}
