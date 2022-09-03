package com.example.mobiledc.ui.menu;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class TaskItem {
    private long deadline;
    private int freq;
    private long initTime;
    private String status;
    private String subj;
    private String taskID;
    private String teacher;
    private String text;
    private String title;

    public TaskItem(long deadline, int freq, long init_time, String status, String subj, String taskID, String teacher, String text, String title) {
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

    private TaskItem (@NonNull JSONObject jsonObject){
        try {
            this.deadline = jsonObject.getLong("deadline");
            this.freq = jsonObject.getInt("freq");
            this.initTime = jsonObject.getLong("init_time");
            this.status = jsonObject.getString("status");
            this.subj = jsonObject.getString("subj");
            this.taskID = jsonObject.getString("task_id");
            this.teacher = jsonObject.getString("teacher");
            this.text = jsonObject.getString("text");
            this.title = jsonObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public long getDeadline() {
        return deadline;
    }

    public int getFreq() {
        return freq;
    }

    public long getInitTime() {
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
}
