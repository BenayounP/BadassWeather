package eu.benayoun.badassweather.badass.model.application;


import android.content.SharedPreferences;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.storage.SharedPreferencesStorage;
import eu.benayoun.badass.utility.storage.BadassSharedPreferencesStorageContract;

/**
 * Created by Pierre on 02/12/2015.
 */
@SuppressWarnings("ALL")
public class AppPreferencesAndAssets implements BadassSharedPreferencesStorageContract
{
	protected SharedPreferencesStorage sharedPreferencesStorage;
	protected boolean userHasrespondedToPermissionsDemands;

	boolean userWantsToDisplayNotification;


	public AppPreferencesAndAssets()
	{
		sharedPreferencesStorage = new SharedPreferencesStorage(Badass.getSimpleClassName(),this);
		sharedPreferencesStorage.load();

	}

	public boolean isUserHasrespondedToPermissionsDemands()
	{
		return userHasrespondedToPermissionsDemands;
	}

	public void setUserHasrespondedToPermissionsDemands()
	{
		this.userHasrespondedToPermissionsDemands = true;
		sharedPreferencesStorage.save();
	}


	public boolean isUserWantsToDisplayNotification()
	{
		return userWantsToDisplayNotification;
	}

	public void toggleUserWantsToDisplayNotification()
	{
		this.userWantsToDisplayNotification = !userWantsToDisplayNotification;
		sharedPreferencesStorage.save();
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
