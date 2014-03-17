package com.lb.todosqlite.dialogs;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;

// TODO: why can't set as "static"? in http://developer.android.com/guide/topics/ui/controls/pickers.html example, the class declared as static. 
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
{
	OnDateSetListener ondateSet;
	 public void setCallBack(OnDateSetListener onDateSet) {
	  ondateSet = onDateSet;
	 }
	
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		int year;
		int monthOfYear;
		int dayOfMonth;		
		final Calendar calendar = Calendar.getInstance();	
		
		Bundle bd = this.getArguments();
		if (bd.containsKey("oldYear"))
			{
				year = bd.getInt("oldYear");
				monthOfYear = bd.getInt("oldMonth")-1;
				dayOfMonth = (bd.getInt("oldDay"));				
				Log.d("DatePickerFragment", "onCreateDialog() recived ");
			}
			else
				{
					year = calendar.get(Calendar.YEAR);
					monthOfYear = calendar.get(Calendar.MONTH);
					dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
					Log.d("DatePickerFragment", "couldn't find keys with old date, initiating dialog with current date instead");			
				}
		
		return new DatePickerDialog(getActivity(), ondateSet, year, monthOfYear, dayOfMonth);		
	}

	//@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		Log.w("DatePickerFragment", "onDateSet() sends date elements to parent fragment as a bundle, setArguments(Bundle args). inputs: yyyy=" + year + " mm=" + monthOfYear + " dd=" + dayOfMonth);
		
//		Bundle userSelectedDate = new Bundle();
//		userSelectedDate.putInt("yearToSet", year);
//		userSelectedDate.putInt("monthToSet", monthOfYear);
//		userSelectedDate.putInt("dayToSet", dayOfMonth);		
		
	}	
	
}
