package eu.benayoun.badassweather.badass.model.application;


import android.content.SharedPreferences;

import eu.benayoun.badass.Badass;
import eu.benayoun.badass.utility.cache.SharedPreferencesFile;
import eu.benayoun.badass.utility.cache.SharedPreferencesSubCache;

/**
 * Created by Pierre on 02/12/2015.
 */
@SuppressWarnings("ALL")
public class AppPreferencesAndAssets implements SharedPreferencesSubCache
{
	protected SharedPreferencesFile sharedPreferencesFile;
	protected boolean userHasrespondedToPermissionsDemands;
	protected boolean userDoesntwantToGiveLocationPermission;

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

	public boolean isUserDoesntwantToGiveLocationPermission()
	{
		return userDoesntwantToGiveLocationPermission;
	}

	public void setUserDoesntwantToGiveLocationPermission(boolean userDoesntwantToGiveLocationPermission)
	{
		this.userDoesntwantToGiveLocationPermission = userDoesntwantToGiveLocationPermission;
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
	public void loadData(SharedPreferences sharedPreferences)
	{
		userHasrespondedToPermissionsDemands = sharedPreferences.getBoolean("userHasrespondedToPermissionsDemands",false);
		userDoesntwantToGiveLocationPermission = sharedPreferences.getBoolean("userDoesntwantToGiveLocationPermission",false);
		userWantsToDisplayNotification = sharedPreferences.getBoolean("userWantsToDisplayNotification",false);
	}

	@Override
	public void saveData(SharedPreferences.Editor editor)
	{
		editor.putBoolean("userHasrespondedToPermissionsDemands",userHasrespondedToPermissionsDemands);
		editor.putBoolean("userDoesntwantToGiveLocationPermission",userDoesntwantToGiveLocationPermission);
		editor.putBoolean("userWantsToDisplayNotification",userWantsToDisplayNotification);
	}


	/**
	 * INTERNAL COOKING
	 */
}
