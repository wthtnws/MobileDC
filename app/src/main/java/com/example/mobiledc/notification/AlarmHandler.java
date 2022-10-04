package com.example.mobiledc.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.example.mobiledc.R;
import com.example.mobiledc.ui.menu.TaskItem;

import java.util.List;

public class AlarmHandler {
    private static String INTENT_NOTIFY_ACTION = "com.example.mobiledc.notification.Notify";
    private static String INTENT_UPDATE_ACTION = "com.example.mobiledc.notification.Update";

    private AlarmHandler(){

    }

    public static void setNotificationAlarms(Context context){
        Log.i("ALARM NOTIFICATION SET", "ENTER");
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.APP_PREFERENCE),Context.MODE_PRIVATE);
        //generate list of tasks from sharedpref file
        List<TaskItem> taskItems = TaskItem.getTasksPref(sharedPreferences);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int id = 1;
        for (TaskItem item: taskItems) {
            //TODO:handle status
            if (item.getStatus().equals(context.getString(R.string.completed)) || item.getStatus().equals("выполнено"))
                return;
            else {
                Intent intent = new Intent(context, NotificationBroadcastReciever.class);
                intent.setAction(INTENT_NOTIFY_ACTION);
                intent.putExtra("task", item.getFull());
                Log.i("ALARM NOTIFICATION SET", intent.getStringArrayExtra("task")[8]);
                PendingIntent pendingNotificationIntent = PendingIntent.getBroadcast(context, id++, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), item.getFreq() * 100, pendingNotificationIntent);
                Log.i("ALARM NOTIFICATION SET", "freq: " + item.getFreq() + " title: " + item.getTitle());
            }
        }
    }

    public static void cancelNotificationAlarm(Context context, int requestCode){
        Log.i("ALARM NOTIFICATION CANCEL", "ENTER");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent cancelFooIntent = new Intent(context, NotificationBroadcastReciever.class);
        cancelFooIntent.setAction(INTENT_NOTIFY_ACTION);

        PendingIntent fooPendingNotificationIntent = PendingIntent.getBroadcast(context,requestCode,cancelFooIntent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(fooPendingNotificationIntent);
        if(alarmManager.getNextAlarmClock() == null)
            Log.i("ALARM NOTIFICATION CANCEL", "NO ALARMS");
        else
            Log.i("ALARM NOTIFICATION CANCEL", alarmManager.getNextAlarmClock().toString());
    }

    public static void cancelNotificationAll(Context context){
        Log.i("ALARM NOTIFICATION CANCEL ALL", "ENTER");
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.APP_PREFERENCE),Context.MODE_PRIVATE);
        int countAlarms = sharedPreferences.getInt("countAlarms",0);
        for (int i = 0; i < countAlarms; i++) {
            cancelNotificationAlarm(context,i+1);
        }
    }

    public static void setUpdateAlarm(Context context, String apiToken) {
        Log.i("ALARM UPDATE SET", "ENTER");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UpdatePreferenceBroadcastReceiver.class);
        intent.setAction(INTENT_UPDATE_ACTION);
        intent.putExtra("apiToken", apiToken);
        Log.i("ALARM UPDATE SET", intent.getStringExtra("apiToken"));
        PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1*60_000, pendingUpdateIntent);
        Log.i("ALARM UPDATE SET", intent.getAction());
    }

    public static void cancelUpdateAlarm(Context context){
        Log.i("ALARM UPDATE CANCEL", "ENTER");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent cancelFooIntent = new Intent(context, UpdatePreferenceBroadcastReceiver.class);
        cancelFooIntent.setAction(INTENT_UPDATE_ACTION);

        PendingIntent fooPendingUpdateIntent = PendingIntent.getBroadcast(context,0,cancelFooIntent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(fooPendingUpdateIntent);
    }

}
