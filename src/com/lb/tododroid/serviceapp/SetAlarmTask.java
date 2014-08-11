package com.lb.tododroid.serviceapp;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 
 
/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * 
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * 
 * This will run on it's own thread.
 * 
 * @author Lior.Basson
 */
public class SetAlarmTask implements Runnable{
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // The context to retrieve the alarm manager from
    private final Context context;
    // The todoID to send for NotifyService handling
    private final int todoID;
 
    public SetAlarmTask(Context context, Calendar date, int todoID) 
    {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.todoID = todoID;
    }
     
    @Override
    public void run() {
        // Request to start a service when the alarm required date comes
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
    	Log.d("SetAlarmTask","run() started");
    	    	
        Intent intent = new Intent(context, NotifyService.class);
        // intent.setType("TodoID=" + todoID);  // used this workaround to differentiate intent per todo reminder  
        
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(NotifyService.TODO_ID, todoID);
        PendingIntent pendingIntent = PendingIntent.getService(context, todoID, intent, 0);  // trying to set unique ReqCode to differentiate intent per todo reminder 
         
        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent); 
        
        Log.d("SetAlarmTask","run() finished");
    }
}