package com.lb.tododroid.serviceapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnRestartAlarmRegistrar extends BroadcastReceiver  
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d("OnRestartAlarmRegistrar", "onReceive() started [BOOT_COMPLETED BroadcastReceiver]");
				
		Thread alarmRegistrar = new Thread(new RegAlarmsTask(context));
		alarmRegistrar.start();
		
		Log.d("OnRestartAlarmRegistrar", "onReceive() finished [BOOT_COMPLETED BroadcastReceiver]");		
	}

}
