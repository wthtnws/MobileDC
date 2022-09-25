package com.example.mobiledc.ui.menu;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskItem {
    private Long deadline;
    private Integer freq;
    private Long initTime;
    private String status;
    private String subj;
    private String taskID;
    private String teacher;
    private String text;
    private String title;

    public TaskItem(Long deadline, int freq, long init_time, String status, String subj, String taskID, String teacher, String text, String title) {
        this.deadline = deadline;
        this.freq = freq;
        this.initTime = init_time;
        this.status = status;
        this.subj = subj;
        this.taskID = taskID;
        this.teacher = teacher;
        this.text = text;
        this.title = title;
    }

    private TaskItem (@NonNull JSONObject jsonObject) throws JSONException {
        this(jsonObject.getLong("deadline"),
                jsonObject.getInt("freq"),
                jsonObject.getLong("init_time"),
                jsonObject.getString("status"),
                jsonObject.getString("subj"),
                jsonObject.getString("task_id"),
                jsonObject.getString("teacher"),
                jsonObject.getString("text"),
                jsonObject.getString("title"));
    }

    public static List<TaskItem> parseFromJSON(@NonNull JSONArray jsonArray)
    {
        List<TaskItem> taskItemList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                taskItemList.add(new TaskItem(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskItemList;
    }

    public static void saveTasksPref(SharedPreferences.Editor editor, List<TaskItem> taskItemList){
        int countAlarmed = 0;
        for (TaskItem item: taskItemList) {
            if (item.getStatus().equals("completed") || item.getStatus().equals("выполнено")){}
            else
                countAlarmed++;
        }
        editor.putInt("countAlarms",countAlarmed);
        editor.apply();
    }
    public static void clearTasksPref(SharedPreferences.Editor editor){
        editor.putInt("countAlarms",0);
        editor.apply();
    }

    public Long getDeadlineLong() {
        return deadline;
    }

    public LocalDateTime getDeadline() {
        LocalDateTime deadlineTime = LocalDateTime.ofEpochSecond(this.getDeadlineLong(),0, ZoneOffset.UTC);
        deadlineTime.
                format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).
                        withLocale(Locale.UK));
        return deadlineTime;
    }

    public Integer getFreq() {
        return freq;
    }

    public Long getInitTime() {
        return initTime;
    }

    public String getStatus() {
        return status;
    }

    public String getSubj() {
        return subj;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String[] getFull(){
        String[] arr = new String[9];
        arr[0] = deadline.toString();
        arr[1] = freq.toString();
        arr[2] = initTime.toString();
        arr[3] = status;
        arr[4] = subj;
        arr[5] = taskID;
        arr[6] = teacher;
        arr[7] = text;
        arr[8] = title;
        return arr;
    }
}
