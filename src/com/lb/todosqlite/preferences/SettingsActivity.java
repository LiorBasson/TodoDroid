package com.lb.todosqlite.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.lb.todosqlite.R;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	final String LOG_TAG = "SettingsActivity";	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Log.d(LOG_TAG, "onCreate() was invoked");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		elementsSpecialHandling();
	}
	
	private void elementsSpecialHandling()
	{
//		@SuppressWarnings("deprecation")
//		EditTextPreference etp = (EditTextPreference) findPreference("pref_key_sms_delete_limit");
//		etp.getText();
//		etp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO: Remove when finish debugging
//				return false;
//			}
//		});				
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) 
	{
		// TODO update summary and etc upon specific pref changed
		Log.d(LOG_TAG, "onSharedPreferenceChanged() was invoked");		
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
