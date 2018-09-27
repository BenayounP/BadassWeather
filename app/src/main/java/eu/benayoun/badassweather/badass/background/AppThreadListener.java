package eu.benayoun.badassweather.badass.background;


import eu.benayoun.badass.Badass;
import eu.benayoun.badass.background.badassthread.manager.BadassThreadListener;
import eu.benayoun.badassweather.R;
import eu.benayoun.badassweather.ThisApp;
import eu.benayoun.badassweather.badass.ui.events.UIEvents;

public class AppThreadListener implements BadassThreadListener
{
    @Override
    public void onThreadStart()
    {
        ThisApp.getModel().appStateCtrl.setJobRunning(Badass.getString(R.string.app_state_bgnd_update_start));
        Badass.broadcastUIEvent(UIEvents.BACKGROUND_EVENT);
    }

    @Override
    public void onThreadEnd()
    {
        ThisApp.getModel().appStateCtrl.updateState();
        Badass.broadcastUIEvent(UIEvents.BACKGROUND_EVENT);
    }
}
