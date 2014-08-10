package com.lb.tododroid.serviceapp;

// import com.lb.tododroid.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnRestartAlarmRegistrar extends BroadcastReceiver  
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d("On8828RestartAlarmRegistrar", "onReceive() started 8828 [BOOT_COMPLETED BroadcastReceiver]");
		
		// TODO: cleanup once dev complete
		/*Intent i = new Intent(context, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		context.startActivity(i);*/
		
		Thread alarmRegistrar = new Thread(new RegAlarmsTask(context));
		alarmRegistrar.start();
		
		Log.d("On8828RestartAlarmRegistrar", "onReceive() finished 8828 [BOOT_COMPLETED BroadcastReceiver]");	
		
	}

}
