package com.awrtechnologies.carbudgetsales;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		try {
			PreferencesManager.setPreferenceByKey(context,
					"SERVICETIME", "");
			PreferencesManager.setPreferenceByKey(context,
					"NOTIFICATIONTIME", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent service1 = new Intent(context, MyAlarmService.class);
		context.startService(service1);
		

	}

}
