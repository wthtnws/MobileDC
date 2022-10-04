package com.example.mobiledc.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobiledc.R;
import com.example.mobiledc.data.Requests;
import com.example.mobiledc.ui.menu.TaskItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class UpdatePreferenceBroadcastReceiver extends BroadcastReceiver {

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("UPDATE BROACAST","ENTER with " + intent.getAction());
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.APP_PREFERENCE),Context.MODE_PRIVATE);

        //TODO: sends the task update request to the server and updates (or not) pref
        Requests.Tasks tasksRequest = new Requests.Tasks(intent.getStringExtra("apiToken"));
        okHttpClient.newCall(tasksRequest.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("[-] UPDATE RQST", "Failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("[0] UPDATE RQST", "Received");

                String respStr = response.body().string();
                Log.i("[+++] UPDATE RESP", respStr);
                try {
                    JSONObject jsonObject = new JSONObject(respStr);
                    String status = jsonObject.getString("status");
                    if (response.isSuccessful()) {

                        Log.i("[+] UPDATE RESP SUCCESS", status);

                        //cancel notification alarms
                        AlarmHandler.cancelNotificationAll(context);
                        //clear local storage
                        TaskItem.clearTasksPref(sharedPreferences.edit());

                        if(jsonObject.has("tasks")) {

                            JSONArray tasks = jsonObject.getJSONArray("tasks");
                            String task = tasks.getString(0);

                            Log.i("[+] UPDATE RESP tasks", tasks.toString() + "\n length = " + tasks.length());
                            Log.i("[+] UPDATE RESP task", task);

                            List<TaskItem> taskItemArrayList = TaskItem.parseFromJSON(tasks);

                            //save tasks to local storage
                            TaskItem.saveTasksPref(sharedPreferences.edit(),taskItemArrayList);
                            //set the notification alarms
                            AlarmHandler.setNotificationAlarms(context);
                        }
                        else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(status,"status is" + status);
                            editor.commit();
                        }

                    } else {
                        Log.i("[-] UPDATE RESP NOT SUCCESS", status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
