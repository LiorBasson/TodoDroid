package com.lb.tododroid.serviceapp;

import java.util.Calendar;
 
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
 
/**
 * This is our service client, it is the 'middle-man' between the
 * service and any activity that wants to connect to the service
 * 
 * @author Lior.Basson
 */
public class ScheduleClient {
 
    // The hook into our service
    private ScheduleService mBoundService;
    // The context to start the service in
    private Context mContext;
    // A flag if we are connected to the service or not
    private boolean mIsBound;
 
    public ScheduleClient(Context context) {
        mContext = context;
    }
     
    /**
     * Call this to connect your activity to your service
     */
    public void doBindService() {
    	Intent scheduleServiceIntent = new Intent(mContext, ScheduleService.class);
    	ComponentName cm = mContext.startService(scheduleServiceIntent); //(new Intent(mContext, ScheduleService.class));
        // Establish a connection with our service
    	mIsBound = mContext.bindService(scheduleServiceIntent, mConnection, Context.BIND_AUTO_CREATE); // (new Intent(mContext, ScheduleService.class), mConnection, Context.BIND_AUTO_CREATE);
    }
     
    /**
     * When you attempt to connect to the service, this connection will be called with the result.
     * If we have successfully connected we instantiate our service object so that we can call methods on it.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with our service has been established, 
            // giving us the service object we can use to interact with our service.
            mBoundService = ((ScheduleService.ServiceBinder) service).getService();
        }
 
        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };
 
    /**
     * Tell our service to set an alarm for the given date
     * @param c a date to set the notification for
     * @param todoID the todo ID number to associate with this notification (this would also be the ID in AlarmManager)
     */
    public void setAlarmForNotification(Calendar c, int todoID)
    {    	
    	mBoundService.setAlarm(c, todoID);
    }
    
    /**
     * Tell our service to set an alarm for the given date
     * @param c a date to set the notification for
     */
    public void cancelAlarmForNotification(int todoID) 
    {
		mBoundService.cancelAlarm(todoID);
	}
    
    public void updateAlarmForNotification(Calendar c, int todoID) 
    {
    	mBoundService.updateAlarm(c, todoID);
    }
     
    /**
     * When you have finished with the service call this method to stop it 
     * releasing your connection and resources
     */
    public void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }
}
