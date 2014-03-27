package com.lb.todosqlite.preferences;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ColorPickerPreference extends Preference 
{
	final String LOG_TAG = "ColorPickerPreference";
	final int DEFAULT_VALUE = -13907694;
	int initialColor = 0;
	int selectedColor = 0;

	public ColorPickerPreference(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		Log.d(LOG_TAG, "ColorPickerPreference(Context context, AttributeSet attrs) constructor was called");

		// TODO: set values 
		//initialColor = get from pref by key? how generalize key, with parameter?
	}
	
	public ColorPickerPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		Log.d(LOG_TAG, "ColorPickerPreferenceContext context, AttributeSet attrs, int defStyle) constructor was called");

		// TODO Auto-generated constructor stub
	}

	public ColorPickerPreference(Context context) {
		super(context);
		Log.d(LOG_TAG, "ColorPickerPreference(Context context) constructor was called");

		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onClick() {
		super.onClick();
		launchColorPicker();
	}
	
	
	private void launchColorPicker() 
	{
		Log.d(LOG_TAG, "launchColorPicker() was called");
		try {
			// initialColor is the initially-selected color to be shown in the
			// rectangle on the left of the arrow.
			// for example, 0xff000000 is black, 0xff0000ff is blue. Please be
			// aware of the initial 0xff which is the alpha.
			
			// TODO: context as 'getContext()' might be problematic, if required I will change to getApplicationCon...			
			AmbilWarnaDialog am = new AmbilWarnaDialog(getContext(),
					initialColor, new OnAmbilWarnaListener() {

						@Override
						public void onOk(AmbilWarnaDialog arg0, int color) 
						{
							Log.d(LOG_TAG, "onOk() was invoked");
							selectedColor = color; // the selected color
							initialColor = color;
							persistInt(color);
							setSummary("color code is " + initialColor);		

						}

						@Override
						public void onCancel(AmbilWarnaDialog arg0) {
							Log.d(LOG_TAG, "onCancel() was invoked");
						}
					});

			am.show();
		} 
		catch (Exception e) 
		{
			Log.d(LOG_TAG, "launchColorPicker() thrown an exception:  ", e);
		}
		Log.d(LOG_TAG, "launchColorPicker() is about to exit method");
	}
		
	@Override
	protected void onBindView(View view) {
		Log.d(LOG_TAG, "onBindView() was invoked");
		
//		TextView tv_title = (TextView) view.findViewById(android.R.id.title);
//		if (!(tv_title==null))
//			tv_title.setTextColor(Color.GREEN);
		
		TextView tv_text1 = (TextView) view.findViewById(android.R.id.summary);
		if (!(tv_text1==null))
		{
			tv_text1.setTextColor(initialColor);
//			tv_text1.setBackgroundColor(Color.GRAY);
		}
		setSummary("color code is " + initialColor);		
				
		super.onBindView(view);
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) 
	{
	    if (restorePersistedValue) 
	    {
	        // Restore existing state
	    	initialColor = this.getPersistedInt(DEFAULT_VALUE);
	    } 
	    else 
	    {
	        // Set default state from the XML attribute
	    	initialColor = (Integer) defaultValue;
	        persistInt(initialColor);
	    }
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) 
	{
	    return a.getInteger(index, DEFAULT_VALUE);
	}

}
