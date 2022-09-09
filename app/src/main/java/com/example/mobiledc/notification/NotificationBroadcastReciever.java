package com.example.mobiledc.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.mobiledc.R;
import com.example.mobiledc.ui.menu.TaskItem;

public class NotificationBroadcastReciever extends BroadcastReceiver {
    private String LOG_TAG = "NOTIFICATION BROADCAST RECEIVER";
    private static NotificationChannel inProgressChannel = new NotificationChannel("IN_PROGRESS_CHANNEL",
            "MobiledDC notification channel",NotificationManager.IMPORTANCE_LOW);
    private static NotificationChannel deadlineChannel = new NotificationChannel("DEADLINE_CHANNEL",
            "MobiledDC notification channel", NotificationManager.IMPORTANCE_HIGH);
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(LOG_TAG, "onReceive");
        Log.d(LOG_TAG, "action = " + intent.getAction());
        Log.d(LOG_TAG, "title = " + intent.getStringArrayExtra("task")[8]);

        String[] taskArr = intent.getStringArrayExtra("task").clone();
        TaskItem taskItem = new TaskItem(
                Long.valueOf(taskArr[0]),
                Integer.valueOf(taskArr[1]),
                Long.valueOf(taskArr[2]),
                taskArr[3],
                taskArr[4],
                taskArr[5],
                taskArr[6],
                taskArr[7],
                taskArr[8]);

        NotificationChannel thisChannel = inProgressChannel;
        String thisChannelId = "IN_PROGRESS_CHANNEL";
        int thisIcon = R.drawable.ic_stat_name;
        //TODO: check the deadline, if it has been passed, then stop alarm and don't notify
        if(taskItem.getStatus().equals(context.getString(R.string.deadline))){
            thisChannel = deadlineChannel;
            thisChannelId = "DEADLINE_CHANNEL";
        }

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(thisIcon)
                .setAutoCancel(true)
                .setContentTitle(taskItem.getSubj() + " "+ taskItem.getTitle())
                .setContentText(taskItem.getDeadline().toString())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(taskItem.getText()))
                .setChannelId(thisChannelId);
        notificationManager.createNotificationChannel(thisChannel);
        notificationManager.notify(intent.getStringArrayExtra("task")[5].hashCode(), mNotifyBuilder.build());
    }
}