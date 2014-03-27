package com.lb.todosqlite.preferences;

import com.lb.todosqlite.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	final String LOG_TAG = "SettingsActivity";
	// ColorPicker vars
	int initialColor = 0;
	int selectedColor = 0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Log.d(LOG_TAG, "onCreate() was called");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		elementsSpecialHandling();
	}
	
	private void elementsSpecialHandling()
	{
		@SuppressWarnings("deprecation")
		EditTextPreference etp = (EditTextPreference) findPreference("pref_key_sms_delete_limit");
		etp.getText();
		etp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				//launchColorPicker();
				return false;
			}
		});
		
		@SuppressWarnings("deprecation")
		Preference p_color = (Preference) findPreference("pref_key_picked_color");		
		//p_color.set
		
		
//		@SuppressWarnings("deprecation")
//		Preference p_color = (Preference) findPreference("pref_key_picked_color");		
//		p_color.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO: refer to its previous color code and store current selected color
//				//preference.gettext
//				
//				launchColorPicker();
//				
//				return false;
//			}
//		});
		
		
	}

//	private void launchColorPicker() 
//	{
//		Log.d(LOG_TAG, "launchColorPicker() was called");
//		try {
//			// initialColor is the initially-selected color to be shown in the
//			// rectangle on the left of the arrow.
//			// for example, 0xff000000 is black, 0xff0000ff is blue. Please be
//			// aware of the initial 0xff which is the alpha.
//
//			AmbilWarnaDialog am = new AmbilWarnaDialog(this,
//					initialColor, new OnAmbilWarnaListener() {
//
//						@Override
//						public void onOk(AmbilWarnaDialog arg0, int color) {
//							Log.d(LOG_TAG, "onOk() was invoked");
//							selectedColor = color; // the selected color
//							initialColor = color;
////							TextView vColorCode = (TextView) findViewById(R.id.sandbox_colorcode);
////							vColorCode.setText(String.valueOf(selectedColor));
////							TextView vColorex = (TextView) findViewById(R.id.editText_SelColor);
////							vColorex.setBackgroundColor(selectedColor);
//						}
//
//						@Override
//						public void onCancel(AmbilWarnaDialog arg0) {
//							Log.d(LOG_TAG, "onCancel() was invoked");
//						}
//					});
//
//			am.show();
//		} catch (Exception e) {
//			Log.d(LOG_TAG, "launchColorPicker() thrown an exception:  ", e);
//		}
//		Log.d(LOG_TAG, "launchColorPicker() is about to exit method");
//	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) 
	{
		// TODO update summary and etc upon specific pref changed
		Log.d(LOG_TAG, "launchColorPicker() was called");

		
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
