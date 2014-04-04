package com.lb.todosqlite.dialogs;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String>
{
	private int colorCodeForTableBG = -1; // white
	private int colorCodeForTableText = -16777216; // black
	int dbgCounter = 0;

	public MyArrayAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
	}

	public MyArrayAdapter(Context context, int textViewResourceId) 
	{
		super(context, textViewResourceId);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = super.getView(position, convertView, parent);
		try
		{
			TextView tvForSpinner = (TextView) v;
			tvForSpinner.setTextColor(colorCodeForTableText);
			tvForSpinner.setBackgroundColor(colorCodeForTableBG);
			v = tvForSpinner;			
		}
		catch (Exception e)
		{
			Log.d("MyArrayAdapter", "getView() caught the next exception: ", e);
		}
		
		return v; 
	}
	
	
	
//	@Override
//	public View getDropDownView(int position, View convertView, ViewGroup parent) 
//	{
//		dbgCounter++;
//		Log.d("MyArrayAdapter", "getDropDownView() invoke counter =" + dbgCounter);
//		View v = super.getDropDownView(position, convertView, parent);
//		try 
//		{
//			TextView tvForSpinner = (TextView) v;
//			tvForSpinner.setTextColor(colorCodeForTableText);
//			tvForSpinner.setBackgroundColor(colorCodeForTableBG);
//		} catch (Exception e) 
//		{
//			Log.d("MyArrayAdapter", "getDropDownView() caught the next exception: ", e);
//		}
//		return v;
//	}
	
	
	public void setTextColor_mine(int color)
	{
		colorCodeForTableText = color;
	}
	
	public void setBackgroundColor_mine(int color)
	{
		colorCodeForTableBG = color;
	}

}
