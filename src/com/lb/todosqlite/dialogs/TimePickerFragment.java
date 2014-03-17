package com.lb.todosqlite.dialogs;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener 
{
	OnTimeSetListener onTimeSet;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{			
		final Calendar calendar = Calendar.getInstance();	
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		boolean is24HourView =  DateFormat.is24HourFormat(getActivity());	
		
		Bundle bd = this.getArguments();
		if (bd.containsKey("oldHour"))
			{
				hourOfDay = bd.getInt("oldHour");
				minute = bd.getInt("oldMinuets");
				Log.d("TimePickerFragment", "onCreateDialog() recived ");
			}		
		
		return new TimePickerDialog(getActivity(), onTimeSet, hourOfDay, minute, is24HourView);
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	{
		Log.w("TimePickerFragment", "private onTimeSet() invoked, and does nothing. you shold set you on callback handler by setting setCallBack(OnTimeSetListener onTimeSet)");
	}
	
	public void setCallBack(OnTimeSetListener onTimeSet)
	{
		this.onTimeSet = onTimeSet;
	}

}
