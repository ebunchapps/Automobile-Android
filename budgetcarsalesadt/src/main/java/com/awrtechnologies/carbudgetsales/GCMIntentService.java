package com.awrtechnologies.carbudgetsales;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;
import com.google.android.gcm.GCMBaseIntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class GCMIntentService extends GCMBaseIntentService {

    public static int KEYERROR = 10;
    public static int KEYREGISTERED = 11;
    public static int KEYUNREGISTRED = 12;

    private static final String TAG = "com.awrtechnologies.carbudgetsales.GCMIntentService";
    //	 static final String SENDER_ID = "915076089072";
    static final String SENDER_ID = "207375680117";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i("Rakhi", "Device registered: regId = " + registrationId);

        Toast.makeText(getApplicationContext(),"tokenis" +registrationId,Toast.LENGTH_SHORT).show();
        PreferencesManager.setPreferenceIntByKey(context,
                PreferencesManager.KEYGCMSTATUS, KEYREGISTERED);
        PreferencesManager.setPreferenceByKey(context,
                PreferencesManager.KEYREGID, registrationId);

    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        PreferencesManager.setPreferenceIntByKey(context,
                PreferencesManager.KEYGCMSTATUS, KEYUNREGISTRED);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        try {
            String message = intent.getExtras().getString("message").toString();
            String time = intent.getExtras().getString("time");
            Log.i(TAG, "MESSAGE = " + message);
            System.out.println("MESSAGE" + message);
            System.out.println("TIME SERVICE" + time);
            Date d = new Date();
            CharSequence s = DateFormat.format("HH:mm:ss ", d.getTime());
            PreferencesManager.setPreferenceByKey(context, "NOTIFICATIONTIME", s.toString());


            System.out.println("TIME=======" + PreferencesManager.getPreferenceByKey(context, "NOTIFICATIONTIME"));
            generateNotification(context, "Service : " + message, time);
        } catch (Exception e) {

        }

        // notifies user
        // try {
        //
        // JSONObject obj = new JSONObject(message);
        // if (obj.getInt("type") == 2) {
        // String pushMessage = obj.getJSONObject("com.awrtechnologies.carbudgetsales.data").getString(
        // "reminder_description");
        // int type = obj.getInt("type");
        // String lead_id = obj.getJSONObject("com.awrtechnologies.carbudgetsales.data").getString("lead_id");
        // String id = obj.getJSONObject("com.awrtechnologies.carbudgetsales.data").getString("id");
        // generateNotification(context, "Reminder : " + pushMessage,
        // type, lead_id,id);
        // } else if (obj.getInt("type") == 1) {
        // String pushMessage = obj.getJSONObject("com.awrtechnologies.carbudgetsales.data").getString(
        // "appointment_name");
        // int type = obj.getInt("type");
        // String lead_id = obj.getJSONObject("com.awrtechnologies.carbudgetsales.data").getString("lead_id");
        // String id = obj.getJSONObject("com.awrtechnologies.carbudgetsales.data").getString("id");
        // generateNotification(context, "Appointment : " + pushMessage,
        // type, lead_id,id);
        // }
        // } catch (Exception e) {
        //
        // }

    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        // Log.i(TAG, "Received deleted messages notification");
        //
        // // notifies user
        // generateNotification(context, "");
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        PreferencesManager.setPreferenceIntByKey(context,
                PreferencesManager.KEYGCMSTATUS, KEYERROR);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        PreferencesManager.setPreferenceIntByKey(context,
                PreferencesManager.KEYGCMSTATUS, KEYERROR);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */

    private static void generateNotification(Context context, String message,
                                             String time) {
        int icon = R.drawable.transparentlogo;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, FirstActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("FROMNOTIFICATION", true);
        bundle.putString("TIME", time);
        notificationIntent.putExtras(bundle);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int notificationindex = getNotificationId(context);
        PendingIntent intent = PendingIntent.getActivity(context,
                notificationindex, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification(icon, message, when);
            notification.setLatestEventInfo(context, title, message, intent);
        } else {
            notification = new Notification.Builder(context)
                    .setContentIntent(intent).setSmallIcon(icon)
                    .setTicker(title).setWhen(when).setAutoCancel(true)
                    .setContentTitle(title).setContentText(message).build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(notificationindex, notification);

    }

    public static int getNotificationId(Context context) {
        if (PreferencesManager.getPreferenceIntByKey(context, "UNID") == Integer.MIN_VALUE) {
            PreferencesManager.setPreferenceIntByKey(context, "UNID", 1);
            return 1;
        } else {
            if (PreferencesManager.getPreferenceIntByKey(context, "UNID") == Integer.MAX_VALUE - 1) {
                PreferencesManager.setPreferenceIntByKey(context, "UNID", 1);
            }
            int id = PreferencesManager.getPreferenceIntByKey(context, "UNID");
            PreferencesManager.setPreferenceIntByKey(context, "UNID", id + 1);
            return id + 1;
        }
    }

}
