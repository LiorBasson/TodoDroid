package com.lb.tododroid.serviceapp;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Cancel an alarm for the given Todo ID passed into the constructor.
 * the Todo ID was initialized into the intent which was passed to AlarmManager with Set() command  
 * 
 * This will run on it's own thread.
 * 
 * @author Lior.Basson
 */


public class CancelAlarmTask implements Runnable
{
	// The android system alarm manager
    private final AlarmManager am;
    // The context to retrieve the alarm manager from
    private final Context context;
    // The todoID to send for NotifyService handling
    private final int todoID;
 
    public CancelAlarmTask(Context context, int todoID) 
    {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.todoID = todoID;
    }
     
    @Override
    public void run() 
    {   
    	Log.d("CancelAlarmTask","run() started");
    	
        Intent intent = new Intent(context, NotifyService.class);
        
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(NotifyService.TODO_ID, todoID);
        PendingIntent pendingIntent = PendingIntent.getService(context, todoID, intent, 0); // trying to set unique ReqCode to differentiate intent per todo reminder 
         
        // Cancel an alarm
        am.cancel(pendingIntent);
        
    	Log.d("CancelAlarmTask","run() finished");
    }
                
	

}