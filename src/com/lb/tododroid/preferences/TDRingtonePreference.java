package com.lb.tododroid.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;
import android.view.View;

public class TDRingtonePreference extends RingtonePreference 
{
	Context context;
	
	public TDRingtonePreference(Context context) 
	{
		super(context);
		this.context = context;
	}

	public TDRingtonePreference(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		this.context = context;
	}

	public TDRingtonePreference(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	@Override
	protected void onBindView(View view) 
	{
		String ringtoneName = "none";
		
		SharedPreferences sharedPreferences = getSharedPreferences();
		
		
		String ringtoneNameURI = sharedPreferences.getString(getKey(), null);
			if (!(ringtoneNameURI == null))
			{
				Ringtone ringtone = RingtoneManager.getRingtone(context, Uri.parse( ringtoneNameURI));
				ringtoneName = ringtone.getTitle(context);								
			}			
			
		setSummary(ringtoneName);
		
		super.onBindView(view);
	}
	
	@Override
	protected void onSaveRingtone(Uri ringtoneUri) 
	{
		Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
		String ringtoneName = ringtone.getTitle(context);	
		setSummary(ringtoneName);
		
		super.onSaveRingtone(ringtoneUri);
	}

}
