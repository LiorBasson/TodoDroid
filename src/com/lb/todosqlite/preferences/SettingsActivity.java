package com.lb.todosqlite.preferences;

import com.lb.todosqlite.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity 
{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public void finish() 
	{
		setResult(RESULT_OK);
		super.finish();		
	}
}
