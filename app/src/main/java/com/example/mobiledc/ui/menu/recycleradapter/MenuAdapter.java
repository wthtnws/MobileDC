package com.example.mobiledc.ui.menu.recycleradapter;

import com.example.mobiledc.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledc.ui.menu.TaskItem;
import com.example.mobiledc.databinding.TaskitemBinding;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.TaskViewHolder> {

    private List<TaskItem> taskItemList;

    public MenuAdapter(List<TaskItem> taskItemList) {
        this.taskItemList = taskItemList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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

            deadlineTextView = itemView.findViewById(R.id.deadline);
            statusTextView = itemView.findViewById(R.id.status);
            subjectTextView = itemView.findViewById(R.id.subject);
            teacherTextView = itemView.findViewById(R.id.teacher);
            textTextView = itemView.findViewById(R.id.someText);
            titleTextView = itemView.findViewById(R.id.taskTitle);
        }
    }
}
