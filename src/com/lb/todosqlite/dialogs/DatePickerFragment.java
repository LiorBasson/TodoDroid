package com.lb.todosqlite.dialogs;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

// TODO: why can't set as "static"? in http://developer.android.com/guide/topics/ui/controls/pickers.html example, the class declared as static. 
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
{
	OnDateSetListener ondateSet;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);;
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);		
		
		Bundle bd = this.getArguments();
		if (bd.containsKey("oldYear"))
			{
				year = bd.getInt("oldYear");
				monthOfYear = bd.getInt("oldMonth")-1;
				dayOfMonth = (bd.getInt("oldDay"));				
				Log.d("DatePickerFragment", "onCreateDialog() recived ");
			}
			else Log.d("DatePickerFragment", "couldn't find keys with old date, initiating dialog with current date instead");			
						
		return new DatePickerDialog(getActivity(), ondateSet, year, monthOfYear, dayOfMonth);		
	}

	//@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		Log.w("DatePickerFragment", "private onDateSet() invoked, and does nothing. you should set your own callback handler by setting setCallBack(OnDateSetListener onDateSet)");	
	}	
	
	public void setCallBack(OnDateSetListener onDateSet) 
	{
		ondateSet = onDateSet;
	}
	
}
