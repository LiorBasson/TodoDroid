package com.lb.tododroid.serviceapp;

import java.util.Calendar;
import java.util.List;

import com.lb.tododroid.model.Todo;
import com.lb.tododroid.services.DatabaseHelper;
import com.lb.tododroid.services.DateTimeServices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RegAlarmsTask implements Runnable
{
	// The android system alarm manager
    private final AlarmManager am;
	// The context to retrieve the alarm manager and DB from
    private final Context context;
    
    
	public RegAlarmsTask(Context context)
	{
		Log.d("RegAlarmsTask","RegAlarmsTask constructor invoked");

		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Log.d("RegAlarmsTask","RegAlarmsTask constructor finished");
	}

	@Override
	public void run() 
	{
		Log.d("RegAlarmsTask","run() started");
				
		// gets from DB all todo records with reminder enabled and then iterate foreach for Alarm manager to setAlarm
		DatabaseHelper db = new DatabaseHelper(context);
		List<Todo> reminderEnabledTodos = db.getAllToDosWithNotification();
		db.closeDB();
		
		int remindersCnt = reminderEnabledTodos.size();
				
		Log.d("RegAlarmsTask","There seem to be " + remindersCnt + " Todos to register for a reminder");
		
		for (Todo todo : reminderEnabledTodos) 
		{
			int todoID = (int) todo.getId();
			
			String reqDueDateTime = DateTimeServices.getTimeOfDateTimeFormat(todo.getDueDate());			
			int year = DateTimeServices.getYearOfDateFormat(todo.getDueDate())
					, month = (DateTimeServices.getMonthOfDateFormat(todo.getDueDate()))-1 // 0 based 
					, day = DateTimeServices.getDayOfDateFormat(todo.getDueDate()) 
					, hourOfDay = DateTimeServices.getHourOfTimeFormat(reqDueDateTime)
					, minute = DateTimeServices.getMinuteOfTimeFormat(reqDueDateTime);
				
			Calendar c = Calendar.getInstance();
			c.set(year, month, day, hourOfDay, minute);
			
			Intent intent = new Intent(context, NotifyService.class);
	                
	        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
	        intent.putExtra(NotifyService.TODO_ID, todoID); 
	        PendingIntent pendingIntent = PendingIntent.getService(context, todoID, intent, 0);   
	         
	        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
	        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);			
		}	
        
		Log.d("RegAlarmsTask","run() finished");
	}

}
