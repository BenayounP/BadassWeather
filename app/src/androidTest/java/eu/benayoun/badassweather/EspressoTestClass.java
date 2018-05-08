package eu.benayoun.badassweather;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.benayoun.badassweather.badass.ui.activity.AppActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTestClass
{
	private String mStringToBetyped;

	@Rule
	public ActivityTestRule<AppActivity> mActivityRule = new ActivityTestRule<>(
			AppActivity.class);

	@Before
	public void init() {
		// Specify a valid string.
		mStringToBetyped = "Espresso";
	}

	@Test
	public void changeText_sameActivity() {
		// Type text and then press the button.
		onView(withId(R.id.screen_home_swiperefreshlayout))
				.perform(swipeDown());

	}
}
