package com.awrtechnologies.carbudgetsales;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class MyAlarmService extends Service {

	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				FirstActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("FROMNOTIFICATIONTIME", true);
//		bundle.putString("TIME", "Done !!");
		intent1.putExtras(bundle);
		Notification notification = new Notification(R.drawable.transparentlogo,
				"Your Receipt has been Upload", System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent1.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_CANCEL_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				"Service", "Your Receipt has been Upload",
				pendingNotificationIntent);
		
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		// Play default notification sound
//		notification.defaults |= Notification.DEFAULT_SOUND;
//		// Vibrate if vibrate is enabled
//		notification.defaults |= Notification.DEFAULT_VIBRATE;

		mManager.notify(0, notification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	


}
