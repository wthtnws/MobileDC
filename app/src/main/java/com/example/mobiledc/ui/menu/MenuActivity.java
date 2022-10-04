package com.example.mobiledc.ui.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledc.R;
import com.example.mobiledc.data.Result;
import com.example.mobiledc.databinding.ActivityTaskmenuBinding;
import com.example.mobiledc.notification.AlarmHandler;
import com.example.mobiledc.ui.login.LoginActivity;
import com.example.mobiledc.ui.menu.recycleradapter.MenuAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private ActivityTaskmenuBinding taskmenuBinding;
    private MenuViewModel menuViewModel;
    private RecyclerView tasksRecyclerView;
    private MenuAdapter menuAdapter;
    private SharedPreferences sharedPreferences;

    private ProgressBar menuProgressBar;
    private TextView noDeadlinesText;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;


    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.i("LISTENER PREF", "PREFS CHANGED with " + s);
                tasksRecyclerView.setVisibility(View.GONE);
                noDeadlinesText.setVisibility(View.GONE);
                //menuProgressBar.setVisibility(View.VISIBLE);
                if(s == null){
                    return;
                }
                else if(s.equals("Unauthorized.")){
                    relogin();
                    finish();
                }
                else if((s.equals("OK"))) {
                    menuProgressBar.setVisibility(View.GONE);
                    noDeadlinesText.setVisibility(View.VISIBLE);
                }
                else{
                    menuProgressBar.setVisibility(View.GONE);
                    tasksRecyclerView.setVisibility(View.VISIBLE);
                    menuAdapter.setTaskItemList(TaskItem.getTasksPref(sharedPreferences));
                }
                setResult(Activity.RESULT_OK);
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        menuViewModel.reloadTasks();
        Log.i("ON_START", "Menu activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ON_RESUME", "Menu activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ON_PAUSE", "Menu activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        Log.i("ON_STOP", "Menu activity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("ON_RESTART", "Menu activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO:unregister listener to shared pref
        Log.i("ON_DESTROY", "Menu activity");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ON_CREATE", "Menu activity");

        taskmenuBinding = ActivityTaskmenuBinding.inflate(getLayoutInflater());
        setContentView(taskmenuBinding.getRoot());

        final Button reload = taskmenuBinding.reloadtasks;
        final Button logout = taskmenuBinding.logout;
        final Button exit = taskmenuBinding.exit;
        menuProgressBar = taskmenuBinding.menuProgressBar;
        noDeadlinesText = taskmenuBinding.noDeadlinesText;

        String apiToken = (String) getIntent().getSerializableExtra("apiToken");

        menuViewModel = new MenuViewModel(apiToken);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.APP_PREFERENCE),Context.MODE_PRIVATE);

        //Register listener to shared pref and if task list changed - update MenuAdapter

        recyclerViewInit();

        //set the updates alarms
        AlarmHandler.setUpdateAlarm(MenuActivity.this,apiToken);


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Reload button was pressed", Toast.LENGTH_SHORT).show();
//                tasksRecyclerView.setVisibility(View.GONE);
//                noDeadlinesText.setVisibility(View.GONE);
//                menuProgressBar.setVisibility(View.VISIBLE);
//                menuViewModel.reloadTasks();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuViewModel.logout();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });

        menuViewModel.getTasksResult().observe(this, new Observer<Result<String>>() {
            @Override
            public void onChanged(Result<String> tasksResult) {
                if (tasksResult == null) {
                    return;
                }
                menuProgressBar.setVisibility(View.GONE);
                tasksRecyclerView.setVisibility(View.VISIBLE);

                //TODO: cancel alarms
                AlarmHandler.cancelNotificationAll(MenuActivity.this);
                //TODO: clear local storage
                TaskItem.clearTasksPref(sharedPreferences.edit());

                if (tasksResult instanceof Result.Error) {
                    showLoadFailed(((Result.Error) tasksResult).getError());
                }
                if (tasksResult instanceof Result.Success) {
                    if (((Result.Success<?>) tasksResult).getData() instanceof String){
                        updateUiWithUser(((Result.Success<String>) tasksResult).getData());
                        noDeadlinesText.setVisibility(View.VISIBLE);
                        if(((Result.Success<String>) tasksResult).getData().equals("Unauthorized.")) {
                            relogin();
                            finish();
                        }
                    }
                    else {
                        ArrayList<TaskItem> taskItemArrayList = (ArrayList<TaskItem>) TaskItem.parseFromJSON((
                                (Result.Success<JSONArray>) tasksResult).getData());
                        //save tasks to local storage
                        TaskItem.saveTasksPref(sharedPreferences.edit(),taskItemArrayList);
                        //set the notification alarms
                        AlarmHandler.setNotificationAlarms(MenuActivity.this);
                    }

                }
                setResult(Activity.RESULT_OK);
            }
        });

        menuViewModel.getLogoutResult().observe(this, new Observer<Result<String>>() {
            @Override
            public void onChanged(Result<String> logoutResult) {
                if (logoutResult == null) {
                    return;
                }
                if (logoutResult instanceof Result.Error) {
                    showLoadFailed(((Result.Error) logoutResult).getError());
                }
                if (logoutResult instanceof Result.Success) {
                    //stop notification service
                    AlarmHandler.cancelNotificationAll(MenuActivity.this);
                    //stop update service
                    AlarmHandler.cancelUpdateAlarm(MenuActivity.this);
                    //TODO: clear local storage
                    TaskItem.clearTasksPref(sharedPreferences.edit());

                    relogin();
                    //setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void recyclerViewInit(){

            tasksRecyclerView = taskmenuBinding.taskrecycler;
            tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            tasksRecyclerView.setHasFixedSize(true);
            menuAdapter = new MenuAdapter();
            menuAdapter.setTaskItemList(new ArrayList<>());
            tasksRecyclerView.setAdapter(menuAdapter);
    }

    private void relogin(){
        SharedPreferences tokSharedPrefernces = getApplicationContext().getSharedPreferences(getString(R.string.TOK_PREFERENCE),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokSharedPrefernces.edit();
        editor.clear();
        editor.apply();
        Intent loginActivity = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }

    private void updateUiWithUser(String data) {
        String body = "No deadlines? FOR SURE?? " + data;
        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
    }

    private void showLoadFailed(Exception e) {
        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
