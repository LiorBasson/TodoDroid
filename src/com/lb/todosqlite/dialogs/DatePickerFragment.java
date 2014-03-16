package com.lb.todosqlite.dialogs;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

// TODO: why can't set as "static"? in http://developer.android.com/guide/topics/ui/controls/pickers.html example, the class declared as static. 
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
{
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);		
	}

	//@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		Log.w("DatePickerFragment", "onDateSet() in this class does nothing. you can override it" +
				" in your class and do whatever you'd like with its inputs: yyyy=" + year + " mm=" + monthOfYear + " dd=" + dayOfMonth);
	}	
	
	
}
