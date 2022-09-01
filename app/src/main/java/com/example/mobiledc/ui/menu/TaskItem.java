package com.example.mobiledc.ui.menu;

public class TaskItem {
    private double deadline;
    private int freq;
    private double initTime;
    private String status;
    private String subj;
    private String taskID;
    private String teacher;
    private String text;
    private String title;

    public TaskItem(double deadline, int freq, double init_time, String status, String subj, String taskID, String teacher, String text, String title) {
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

    public double getDeadline() {
        return deadline;
    }

    public int getFreq() {
        return freq;
    }

    public double getInitTime() {
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
