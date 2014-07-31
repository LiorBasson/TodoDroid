package com.lb.tododroid.serviceapp;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
 
//import com.blundell.tut.service.task.AlarmTask;
 
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
         
        // We want this service to continue running until it is explicitly stopped, so return sticky.
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
        // This starts a new thread to set the alarm
    	
    	/*try 
	    	{
				Thread.sleep(3000);
			} 
	       catch (InterruptedException e) 
			{
		       	Log.e("TD_SCHEDULE_SERVICE", "InterruptedException was thrown during sleep", e);
		       	e.printStackTrace();
			}*/
    	
		Thread setAlarmThread = new  Thread(new SetAlarmTask(this, c, todoID), "TodoDroidAT");
		setAlarmThread.start();    	 
    	// new AlarmTask(this, c).run(); // original and the only line in this method       
    }
    
    public void cancelAlarm(int todoID)
    {
    	Thread cancelAlarmThread = new Thread(new CancelAlarmTask(this, todoID));
    	cancelAlarmThread.start();
    }
    
    public void updateAlarm(Calendar c, int todoID) 
    {
    	
    	/*final Runnable r1 = new Runnable() {
    	    public void run() 
    	    {
    	    	Log.d("SchdS","t1 started");
    	    	
    	    	try 
    	    	{
					Thread.sleep(2000);
				} 
    	    	catch (InterruptedException e) 
    	    	{
					Log.d("SchdS","t1  wait interupted", e);
				}
    	    	
    	    	Log.d("SchdS","t1 finished");
    	    }
    	};
    	    
    	final Runnable r2 = new Runnable() {
     	    public void run() 
     	    {
     	    	Log.d("SchdS","t2 started");
     	    	
     	    	try 
    	    	{
					Thread.sleep(1000);
				} 
    	    	catch (InterruptedException e) 
    	    	{
					Log.d("SchdS","t2 wait interupted", e);
				}
     	    	
    	    	Log.d("SchdS","t2 finished");
     	    }
    	 };*/
    	 
    	ExecutorService exe = Executors.newFixedThreadPool(1);
    	Future cancelAlarmFuture = exe.submit(new CancelAlarmTask(this, todoID));    
    	Future setAlarmFuture = exe.submit(new SetAlarmTask(this, c, todoID));
    	exe.shutdown();    	
    }  	
		
	
}
