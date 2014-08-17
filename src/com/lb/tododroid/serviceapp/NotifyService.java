package com.lb.tododroid.serviceapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.lb.tododroid.EditTodo;
import com.lb.tododroid.R;
import com.lb.tododroid.model.Todo;
import com.lb.tododroid.services.DatabaseHelper;
 

/**
 * This service is invoked when an Alarm has been raised by AlarmManager.
 * 
 * We pop a notification into the status bar for the user to tap on.
 * When the user taps on the notification a new EditTodo activity is launched
 * 
 * @author Lior.Basson
 */
public class NotifyService extends Service {
 
    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }
 
    // Name of an intent extra we can use to identify if this service was started to create a notification  
    public static final String INTENT_NOTIFY = "com.lb.tododroid.serviceapp.INTENT_NOTIFY";
    public static final String TODO_ID = "com.lb.tododroid.serviceapp.notifyservice.todoID";
    public static final String NOTIFICATION_SOUND_URI = "com.lb.tododroid.serviceapp.NOTIFICATIONSOUNDURI";    
    private int todoID;
    // The system notification manager
    private NotificationManager mNM;
 
    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
         
        // If this service was started by our AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
        {
        	todoID = intent.getIntExtra(TODO_ID, -1);
        	showNotification();        	
        }            
         
        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
 
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
 
    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    @SuppressWarnings("deprecation")
	private void showNotification() 
    {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	boolean isNotifEnabled = sp.getBoolean("pref_key_notification", false);    	
    	String notifPreferedTone =  sp.getString("pref_key_notif_selector", Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    	    
    	if (isNotifEnabled)
    	{
    		 DatabaseHelper db = new DatabaseHelper(getApplicationContext());
    	      int m_todoID = todoID;
    			Todo updatedTodo = db.getTodo(m_todoID);
    			db.closeDB();    
    			
    			// This is the 'title' of the notification    	
    	        CharSequence title = "TodoDroid Reminder!";    	
    	        // This is the icon to use on the notification
    	        int icon = R.drawable.ic_launcher;
    	        // This is the scrolling text of the notification
    	        CharSequence text = updatedTodo.getNote(); // "Tap to view Todo " + todoID;       
    	        // What time to show on the notification
    	        long time = System.currentTimeMillis();
    	         
    	        Notification notification = new Notification(icon, text, time);
    	        
    	        Uri notifSoundUri =  Uri.parse(notifPreferedTone);
    	        
    	        // TODO: for API 11 and higher. check later
    	       /* Notification noti = new Notification.Builder(getApplicationContext())
    												        .setContentTitle(title)
    												        .setContentText(text)
    												        .setSmallIcon(icon)
    												        //.setLargeIcon(aBitmap)
    												        .setSound(soundUri)
    												        .setWhen(time)
    												        .build();
    	*/
    	 
    	        // The PendingIntent to launch our activity if the user selects this notification
    	        
    	        Intent intentToInvoke = new Intent(this, EditTodo.class); // new Intent(this, MainActivity.class);
    	        intentToInvoke.putExtra("com.lb.tododroid.edittodo.todoID", todoID);
    	        intentToInvoke.putExtra("com.lb.tododroid.edittodo.sender", "NotifyService");
    	        
    	        PendingIntent contentIntent =  PendingIntent.getActivity(this, todoID, intentToInvoke, 0); // oneShot,Cancel, update - tried with no substantial diff. PendingIntent.getActivity(this, 0, intentToInvoke, 0);
    	 
    	        // Set the info for the views that show in the notification panel.
    	        notification.setLatestEventInfo(this, title, text, contentIntent);
    	        notification.sound = notifSoundUri;
    	 
    	        // Clear the notification when it is pressed
    	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
    	         
    	        // Send the notification to the system.
    	        mNM.notify(todoID, notification); // mNM.notify(NOTIFICATION, notification);
    		
    		
    	}     
                 
        // Stop the service when we are finished
        stopSelf();
    }
}
