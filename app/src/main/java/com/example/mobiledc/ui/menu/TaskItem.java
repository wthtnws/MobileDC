package com.example.mobiledc.ui.menu;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    private TaskItem() {

    }

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
            //TODO:handle the status
            if (item.getStatus().equals("completed") || item.getStatus().equals("выполнено")){}
            else {
                Set<String> taskSet = new HashSet<>(Arrays.asList(item.getFullWithFieldNames()));
                Log.i("SAVE TO PREFS",taskSet.toString());
                editor.putStringSet("10000"+ countAlarmed, taskSet);
                countAlarmed++;
            }
        }
        editor.putInt("countAlarms",countAlarmed);
        editor.apply();
    }

    public static List<TaskItem> getTasksPref(SharedPreferences sharedPreferences){
        Map<String,?> tasksMap =  sharedPreferences.getAll();
        ArrayList<TaskItem> taskItemArrayList = new ArrayList<>();

        for (Map.Entry<String,?> entry: tasksMap.entrySet()) {
            if(entry.getValue() instanceof Set<?>){

                Set<String> valueStringSet = (Set<String>) entry.getValue();
                Iterator<String> stringSetIterator = valueStringSet.iterator();

                String[] itemDataArray = new String[valueStringSet.size()];
                while (stringSetIterator.hasNext()){
                    String taskField = stringSetIterator.next();
                    int index = (int) taskField.charAt(0) - 48;
                    String fieldData = taskField.substring(2);
                    itemDataArray[index] = fieldData;
                }

//                Log.i("GET FROM PREFS",
//                        itemDataArray[0] + " " +
//                        itemDataArray[1] + " " +
//                        itemDataArray[2] + " " +
//                        itemDataArray[3] + " " +
//                        itemDataArray[4] + " " +
//                        itemDataArray[5] + " " +
//                        itemDataArray[6] + " " +
//                        itemDataArray[7] + " " +
//                        itemDataArray[8]
//                );
                TaskItem taskItem = new TaskItem
                        (Long.valueOf(itemDataArray[0]),
                        Integer.valueOf(itemDataArray[1]),
                        Long.valueOf(itemDataArray[2]),
                        itemDataArray[3],
                        itemDataArray[4],
                        itemDataArray[5],
                        itemDataArray[6],
                        itemDataArray[7],
                        itemDataArray[8]
                );
                taskItemArrayList.add(taskItem);
            }
        }
        return taskItemArrayList;
    }
    public static void clearTasksPref(SharedPreferences.Editor editor){
        Log.i("CLEAR TASK PREF", "ENTER");
        editor.clear();
        //editor.putInt("countAlarms",0);
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

    private void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    private void setFreq(Integer freq) {
        this.freq = freq;
    }

    private void setInitTime(Long initTime) {
        this.initTime = initTime;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    private void setSubj(String subj) {
        this.subj = subj;
    }

    private void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    private void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    private void setText(String text) {
        this.text = text;
    }

    private void setTitle(String title) {
        this.title = title;
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

    private String[] getFullWithFieldNames(){
        String[] arr = new String[9];
        arr[0] = "0:" + deadline.toString();
        arr[1] = "1:" + freq.toString();
        arr[2] = "2:" + initTime.toString();
        arr[3] = "3:" + status;
        arr[4] = "4:" + subj;
        arr[5] = "5:" + taskID;
        arr[6] = "6:" + teacher;
        arr[7] = "7:" + text;
        arr[8] = "8:" + title;
        return arr;
    }
}
