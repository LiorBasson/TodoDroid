package com.lb.todosqlite.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class DateTimeServices 
{	
	private static final String logTag = "DateTimeServices";
	private static final String dateFormat = "DD-MM-YYYY";		//private static final String dateFormat = "YYYY-MM-DD";	
	private static final String timeFormat = "HH:mm";
	private static final String dateTimeFormat = "DD-MM-YYYY HH:mm";	//private static final String dateTimeFormat = "YYYY-MM-DD HH:mm";	
	private static boolean isDebugNewImp = false; // var which enables debug of new implementation with SimpleDateFormat and Calendar
	
	// the next 3 methods (getYearOfDateFormat, getMonthOfDateFormat, getDayOfDateFormat) 
	// are basically handles current DateTime format (YYYY-MM-DD HH:mm) and Date format (YYYY-MM_DD) 
	public static int getYearOfDateFormat(String todoDueDate)
	{
		int year = 1900;
		if (dateFormat.equals("DD-MM-YYYY"))
		{	
			int startIndex = 6;
			int endIndex = 10;				
			try 
			{	year = Integer.parseInt(todoDueDate.substring(startIndex, endIndex));			} 
			catch (NumberFormatException e) 			{
				Log.e(logTag, "getYearOfDateFormat() caught an exception when attempted to parse date from string to int"); 
			}
			catch (IndexOutOfBoundsException e)			{
				Log.e(logTag, "getYearOfDateFormat() caught an exception when attempted to trim a substring"); 
			}			
		}
		
		return year;
	}
	
	public static int getMonthOfDateFormat(String todoDueDate)
	{
		int month = 0;
		if (dateFormat.equals("DD-MM-YYYY"))
		{			
			int startIndex = 3;
			int endIndex = 5;				
			try 
			{	month = Integer.parseInt(todoDueDate.substring(startIndex, endIndex));			} 
			catch (NumberFormatException e) 			{
				Log.e(logTag, "getYearOfDateFormat() caught an exception when attempted to parse date from string to int"); 
			}
			catch (IndexOutOfBoundsException e)			{
				Log.e(logTag, "getYearOfDateFormat() caught an exception when attempted to trim a substring"); 
			}			
		}
		
		return month;
	}
	
	public static int getDayOfDateFormat(String todoDueDate)
	{
		int day = 1;
		if (dateFormat.equals("DD-MM-YYYY"))
		{	
			int startIndex = 0;
			int endIndex = 2;				
			try 
			{	day = Integer.parseInt(todoDueDate.substring(startIndex, endIndex));			} 
			catch (NumberFormatException e) 			{
				Log.e(logTag, "getYearOfDateFormat() caught an exception when attempted to parse date from string to int"); 
			}
			catch (IndexOutOfBoundsException e)			{
				Log.e(logTag, "getYearOfDateFormat() caught an exception when attempted to trim a substring"); 
			}			
		}
		
		return day;
	}
	
	
	// the next 2 methods are basically handles only Time format (HH:mm) when using old implementation 
	public static int getHourOfTimeFormat(String timeString)
	{
		int hour;
		if (isDebugNewImp)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault()); 
			Calendar c = Calendar.getInstance();			
			try 
			{
				c.setTime(sdf.parse(timeString));
			} 
			catch (ParseException e) 
			{
				Log.e(logTag, "getHourOfTimeFormat() thrown an ParseException. check timeString =   "
						+ timeString.toString() + " exception = ", e);
			}
			hour = c.get(Calendar.HOUR_OF_DAY);		
		}
		else hour = Integer.parseInt(timeString.substring(0, 2));		
		
		return hour;
	}
	
	public static int getMinuteOfTimeFormat(String timeString)
	{
		int minuets;
		if (isDebugNewImp)
		{		
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault()); 
			Calendar c = Calendar.getInstance();
			try 
			{
				c.setTime(sdf.parse(timeString));
			} 
			catch (ParseException e) 
			{
				Log.e(logTag, "getMinuteOfTimeFormat() thrown an ParseException. check timeString =   "
						+ timeString.toString() + " exception = ", e);
			}
			minuets = c.get(Calendar.MINUTE);	
		}		
		else minuets = Integer.parseInt(timeString.substring(3, timeString.length()));
		
		return minuets;
	}
	
	
	public static String getDateOfDateTimeFormat(String todoDueDate)
	{
		int startIndex = 0;
		int endIndex = (todoDueDate.indexOf(":") -2);
		return todoDueDate.substring(startIndex, endIndex);
	}
	
	public static String getTimeOfDateTimeFormat(String todoDueDate)
	{
		int startIndex = (todoDueDate.indexOf(":") -2) ;
		int endIndex = todoDueDate.length();		
		return todoDueDate.substring(startIndex, endIndex);
	}
	
	public static String getFormattedDateOfYMD(int year, int monthOfYear, int dayOfMonth)
	{
		String formattedDate;
		
		String yearString = String.valueOf(year);
		
		String month="01";
		int month_Int = monthOfYear+1;
		if (month_Int>9)
			month = String.valueOf(month_Int);
		else month = "0" + String.valueOf(month_Int);
		
		String day = "01";
		if (dayOfMonth>9)
			day = String.valueOf(dayOfMonth);
		else day = "0" + String.valueOf(dayOfMonth);
		
		formattedDate = day + "-" + month + "-" + yearString;
		return formattedDate;
	}
	
	public static String getFormattedTimeOfHM(int hourOfDay, int minute)
	{
		String formattedTime;
		
		String hour="01";		
		if (hourOfDay>9)
			hour = String.valueOf(hourOfDay);
		else hour = "0" + String.valueOf(hourOfDay);
		
		String min = "01";
		if (minute>9)
			min = String.valueOf(minute);
		else min = "0" + String.valueOf(minute);
		
		formattedTime = hour + ":" + min;
		return formattedTime;		
	}	
	
	public static String getDate() 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	    		   "dd-MM-yyyy", Locale.getDefault()); 
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	public static String getTime() 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	    		   "HH:mm", Locale.getDefault()); 
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	public static String getDateTime() 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	    		   "dd-MM-yyyy HH:mm:ss", Locale.getDefault()); 
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	public static String getSupportedDateFormat()
	{
		return dateFormat;
	}
	
	public static String getSupportedTimeFormat()
	{
		return timeFormat;
	}
	
	public static String getSupportedDateTimeFormat()
	{
		return dateTimeFormat;
	}
}
