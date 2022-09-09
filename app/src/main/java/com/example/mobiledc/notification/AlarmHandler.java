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
    private static String INTENT_ACTION = "com.example.mobiledc.notification.Notify";
    private AlarmHandler(){

    }

    public static void setAlarms(Context context, List<TaskItem> taskItems){
        Log.i("ALARM SET", "ENTER");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int id = 1;
        for (TaskItem item: taskItems) {
            if (item.getStatus().equals(context.getString(R.string.completed)) || item.getStatus().equals("выполнено"))
                return;
            else {
                Intent intent = new Intent(context, NotificationBroadcastReciever.class);
                intent.setAction(INTENT_ACTION);
                intent.putExtra("task", item.getFull());

                PendingIntent pendingNotificationIntent = PendingIntent.getBroadcast(context, id++, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 30000, item.getFreq() * 100, pendingNotificationIntent);
                Log.i("ALARM SET", "freq: " + item.getFreq() + " title: " + item.getTitle());
            }
        }
    }

    public static void cancelAlarm(Context context, int requestCode){
        Log.i("ALARM CANCEL", "ENTER");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent cancelFooIntent = new Intent(context, NotificationBroadcastReciever.class);
        cancelFooIntent.setAction(INTENT_ACTION);

        PendingIntent fooPendingNotificationIntent = PendingIntent.getBroadcast(context,requestCode,cancelFooIntent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(fooPendingNotificationIntent);
        if(alarmManager.getNextAlarmClock() == null)
            Log.i("ALARM CANCEL", "NO ALARMS");
        else
            Log.i("ALARM CANCEL", alarmManager.getNextAlarmClock().toString());
    }

    public static void cancelAll(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.APP_PREFERENCE),Context.MODE_PRIVATE);
        int countAlarms = sharedPreferences.getInt("countAlarms",0);
        for (int i = 0; i < countAlarms; i++) {
            cancelAlarm(context,i+1);
        }
    }

}
