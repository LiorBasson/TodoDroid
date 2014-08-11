package com.lb.tododroid.preferences;

import java.net.URI;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.util.Log;

import com.lb.tododroid.R;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener 
{
	
	final String LOG_TAG = "SettingsActivity";	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Log.d(LOG_TAG, "onCreate() was invoked");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);	
		
	}	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{		
		Log.d(LOG_TAG, "onSharedPreferenceChanged() was invoked");		
		
		

		Log.d(LOG_TAG, "onSharedPreferenceChanged() was finished");		
	}
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		Log.d(LOG_TAG, "onResume() was invoked");

	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		Log.d(LOG_TAG, "onPause() was invoked");

	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void finish() 
	{
		Log.d(LOG_TAG, "finish() was invoked");
		setResult(RESULT_OK);
		super.finish();
	}	
}
