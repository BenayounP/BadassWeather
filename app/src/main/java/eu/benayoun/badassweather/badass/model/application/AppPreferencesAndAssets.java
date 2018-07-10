package eu.benayoun.badassweather.badass.model.application;


import android.content.SharedPreferences;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.cache.SharedPreferencesFile;
import eu.benayoun.badass.utility.cache.SharedPreferencesSubCacheContract;

/**
 * Created by Pierre on 02/12/2015.
 */
@SuppressWarnings("ALL")
public class AppPreferencesAndAssets implements SharedPreferencesSubCacheContract
{
	protected SharedPreferencesFile sharedPreferencesFile;
	protected boolean userHasrespondedToPermissionsDemands;

	boolean userWantsToDisplayNotification;


	public AppPreferencesAndAssets()
	{
		sharedPreferencesFile = new SharedPreferencesFile(Badass.getSimpleClassName(),this);
		sharedPreferencesFile.load();

	}

	public boolean isUserHasrespondedToPermissionsDemands()
	{
		return userHasrespondedToPermissionsDemands;
	}

	public void setUserHasrespondedToPermissionsDemands()
	{
		this.userHasrespondedToPermissionsDemands = true;
		sharedPreferencesFile.save();
	}


	public boolean isUserWantsToDisplayNotification()
	{
		return userWantsToDisplayNotification;
	}

	public void toggleUserWantsToDisplayNotification()
	{
		this.userWantsToDisplayNotification = !userWantsToDisplayNotification;
		sharedPreferencesFile.save();
	}

	@Override
	public void load(SharedPreferences sharedPreferences)
	{
		userHasrespondedToPermissionsDemands = sharedPreferences.getBoolean("userHasrespondedToPermissionsDemands",false);
		userWantsToDisplayNotification = sharedPreferences.getBoolean("userWantsToDisplayNotification",false);
	}

	@Override
	public void save(SharedPreferences.Editor editor)
	{
		editor.putBoolean("userHasrespondedToPermissionsDemands",userHasrespondedToPermissionsDemands);
		editor.putBoolean("userWantsToDisplayNotification",userWantsToDisplayNotification);
	}


	/**
	 * INTERNAL COOKING
	 */
}
