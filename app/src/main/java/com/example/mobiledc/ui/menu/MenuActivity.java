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
import com.example.mobiledc.data.Requests;
import com.example.mobiledc.data.Result;
import com.example.mobiledc.databinding.ActivityTaskmenuBinding;
import com.example.mobiledc.ui.login.LoggedInUserView;
import com.example.mobiledc.ui.login.LoginActivity;
import com.example.mobiledc.ui.login.LoginResult;
import com.example.mobiledc.ui.menu.recycleradapter.MenuAdapter;
import com.example.mobiledc.ui.secondfactor.SecondFactorActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ActivityTaskmenuBinding taskmenuBinding;
    private MenuViewModel menuViewModel;
    private RecyclerView tasksRecyclerView;
    private MenuAdapter menuAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ON_START", "Menu activity");
        menuViewModel.reloadTasks();
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
        Log.i("ON_DESTROY", "Menu activity");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ON_CREATE", "Menu activity");

        taskmenuBinding = ActivityTaskmenuBinding.inflate(getLayoutInflater());
        setContentView(taskmenuBinding.getRoot());

        menuViewModel = new MenuViewModel((String) getIntent().getSerializableExtra("apiToken"));

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.APP_PREFERENCE),Context.MODE_PRIVATE);

        final Button reload = taskmenuBinding.reloadtasks;
        final Button logout = taskmenuBinding.logout;
        final Button exit = taskmenuBinding.exit;
        final ProgressBar menuProgressBar = taskmenuBinding.menuProgressBar;
        final TextView noDeadlinesText = taskmenuBinding.noDeadlinesText;

        recyclerViewInit();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Reload button was pressed", Toast.LENGTH_SHORT).show();
                tasksRecyclerView.setVisibility(View.GONE);
                noDeadlinesText.setVisibility(View.GONE);
                menuProgressBar.setVisibility(View.VISIBLE);
                menuViewModel.reloadTasks();
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
                finish();
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
                if (tasksResult instanceof Result.Error) {
                    showLoadFailed(((Result.Error) tasksResult).getError());
                }
                if (tasksResult instanceof Result.Success) {
                    if (((Result.Success<?>) tasksResult).getData() instanceof String){
                        updateUiWithUser(((Result.Success<String>) tasksResult).getData());
                        noDeadlinesText.setVisibility(View.VISIBLE);
                        //TODO: if status "Unauthorized" start the login process again
                        // to refresh apitoken
                        if(((Result.Success<String>) tasksResult).getData().equals("Unathorized")) {
                            relogin();
                            finish();
                        }

                    }
                    else {
                        menuAdapter.setTaskItemList(TaskItem.parseFromJSON((
                                (Result.Success<JSONArray>) tasksResult).getData()));
                        //TODO:save tasks to local storage, start notification process
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
                    //TODO: end notification process (and clean all service data in local storages)

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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("log");
        editor.remove("pas");
        editor.apply();
        Intent loginActivity = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }

    private List<TaskItem> getTasks(){
        return new ArrayList<>(Arrays.asList(
                new TaskItem(1234567890, 600, 123456789, "in progress", "Kripta", "someID1", "Shenets", "texttext1", "title1"),
                new TaskItem(1234567890, 600, 123456789, "in progress", "OOP", "someID2", "Chernov", "texttext2", "title2"),
                new TaskItem(1234567890, 600, 123456789, "in progress", "SUBD", "someID3", "Poltavceva", "texttext3", "title3"),
                new TaskItem(1234567890, 600, 123456789, "in progress", "BIP", "someID4", "Dahnovich", "texttext4", "title4"),
                new TaskItem(1234567890, 600, 123456789, "in progress", "Kripta2", "someID5", "EB", "texttext5", "title5"),
                new TaskItem(1234567890, 600, 123456789, "in progress", "Mobilki", "someID6", "Ivanov", "texttext6", "title6"),
                new TaskItem(1234567890, 600, 123456789, "in progress", "Litrbol", "someID7", "Dmitriev", "Moskva p'et pivo", "Avaria p'et pivo")
                ));
    }

    private void updateUiWithUser(String data) {
        String body = "No deadlines? FOR SURE?? " + data;
        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
    }

    private void showLoadFailed(Exception e) {
        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
