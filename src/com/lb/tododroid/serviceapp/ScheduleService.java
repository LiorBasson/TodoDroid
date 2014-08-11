package com.lb.tododroid.serviceapp;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
 
 
public class ScheduleService extends Service {
 
    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        ScheduleService getService() {
            return ScheduleService.this;
        }
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService", "Received start id " + startId + ": " + intent);
         
        return START_STICKY;
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
 
    // This is the object that receives interactions from clients. See
    private final IBinder mBinder = new ServiceBinder();
 
    /**
     * Show an alarm for a certain date when the alarm is called it will pop up a notification 
     */
    public void setAlarm(Calendar c, int todoID)   
    {            	
		Thread setAlarmThread = new  Thread(new SetAlarmTask(this, c, todoID), "TodoDroidAT");
		setAlarmThread.start();    	 
    }
    
    public void cancelAlarm(int todoID)
    {
    	Thread cancelAlarmThread = new Thread(new CancelAlarmTask(this, todoID));
    	cancelAlarmThread.start();
    }
    
    public void updateAlarm(Calendar c, int todoID) 
    {    	   	    	 
    	ExecutorService exe = Executors.newFixedThreadPool(1);
    	Future cancelAlarmFuture = exe.submit(new CancelAlarmTask(this, todoID));    
    	Future setAlarmFuture = exe.submit(new SetAlarmTask(this, c, todoID));
    	exe.shutdown();    	
    }  	
		
	
}
