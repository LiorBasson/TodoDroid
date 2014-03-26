package com.lb.todosqlite.preferences;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

public class ColorPickerPreference extends Preference 
{
	final String LOG_TAG = "ColorPickerPreference";
	int initialColor = 0;
	int selectedColor = 0;

	public ColorPickerPreference(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
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
						public void onOk(AmbilWarnaDialog arg0, int color) {
							Log.d(LOG_TAG, "onOk() was invoked");
							selectedColor = color; // the selected color
							initialColor = color;
//							TextView vColorCode = (TextView) findViewById(R.id.sandbox_colorcode);
//							vColorCode.setText(String.valueOf(selectedColor));
//							TextView vColorex = (TextView) findViewById(R.id.editText_SelColor);
//							vColorex.setBackgroundColor(selectedColor);
						}

						@Override
						public void onCancel(AmbilWarnaDialog arg0) {
							Log.d(LOG_TAG, "onCancel() was invoked");
						}
					});

			am.show();
		} catch (Exception e) {
			Log.d(LOG_TAG, "launchColorPicker() thrown an exception:  ", e);
		}
		Log.d(LOG_TAG, "launchColorPicker() is about to exit method");
	}
	

}
