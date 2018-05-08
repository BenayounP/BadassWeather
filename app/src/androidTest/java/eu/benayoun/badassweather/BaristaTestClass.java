package eu.benayoun.badassweather;

import com.schibsted.spain.barista.rule.BaristaRule;

import org.junit.Rule;
import org.junit.Test;

import eu.benayoun.badassweather.badass.ui.activity.AppActivity;

import static com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh;

public class BaristaTestClass
{
	@Rule
	public BaristaRule<AppActivity> baristaRule = BaristaRule.create(AppActivity.class);

	@Test
	public void testSwipeRefreshLayout()
	{
		baristaRule.launchActivity();
		refresh();
	}
}
