package com.example.mobiledc.ui.menu.recycleradapter;

import com.example.mobiledc.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledc.ui.menu.TaskItem;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.TaskViewHolder> {

    private List<TaskItem> taskItemList;

    public void setTaskItemList(List<TaskItem> taskItemList) {
        this.taskItemList = taskItemList;
        notifyDataSetChanged();
    }

    public void clearItems() {
            taskItemList.clear();
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.taskitem, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView deadlineTextView;
        private TextView statusTextView;
        private TextView subjectTextView;
        private TextView teacherTextView;
        private TextView textTextView;
        private TextView titleTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            this.deadlineTextView = itemView.findViewById(R.id.deadline);
            this.statusTextView = itemView.findViewById(R.id.status);
            this.subjectTextView = itemView.findViewById(R.id.subject);
            this.teacherTextView = itemView.findViewById(R.id.teacher);
            this.textTextView = itemView.findViewById(R.id.someText);
            this.titleTextView = itemView.findViewById(R.id.taskTitle);
        }

        public void bind(TaskItem taskItem){
            deadlineTextView.setText(new Date(taskItem.getDeadline()).toString());
            statusTextView.setText(taskItem.getStatus());
            subjectTextView.setText(taskItem.getSubj());
            teacherTextView.setText(taskItem.getTeacher());
            textTextView.setText(taskItem.getText());
            titleTextView.setText(taskItem.getTitle());
        }
    }
}
