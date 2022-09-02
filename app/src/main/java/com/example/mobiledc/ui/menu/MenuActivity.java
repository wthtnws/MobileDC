package com.example.mobiledc.ui.menu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobiledc.data.Requests;
import com.example.mobiledc.data.Result;
import com.example.mobiledc.databinding.ActivityTaskmenuBinding;
import com.example.mobiledc.ui.login.LoggedInUserView;
import com.example.mobiledc.ui.login.LoginResult;
import com.example.mobiledc.ui.secondfactor.SecondFactorActivity;

public class MenuActivity extends AppCompatActivity {

    private ActivityTaskmenuBinding taskmenuBinding;
    private MenuViewModel menuViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskmenuBinding = ActivityTaskmenuBinding.inflate(getLayoutInflater());
        setContentView(taskmenuBinding.getRoot());

        menuViewModel = new MenuViewModel((String) getIntent().getSerializableExtra("apiToken"));

        final Button reload = taskmenuBinding.reloadtasks;
        final Button logout = taskmenuBinding.logout;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Reload button was pressed", Toast.LENGTH_SHORT).show();
                //menuViewModel.reloadTasks();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuViewModel.logout();
            }
        });

        menuViewModel.getTasksResult().observe(this, new Observer<Result<String>>() {
            @Override
            public void onChanged(Result<String> tasksResult) {
                if (tasksResult == null) {
                    return;
                }
                //loadingProgressBar.setVisibility(View.GONE);
                if (tasksResult instanceof Result.Error) {
                    showLoadFailed(((Result.Error) tasksResult).getError());
                }
                if (tasksResult instanceof Result.Success) {
                    updateUiWithUser(((Result.Success<String>) tasksResult).getData());

                    //TODO: delegate data to RecyclerView Handler class
                    //
                    //TODO: start notification process
                    //Intent menu = new Intent(SecondFactorActivity.this, MenuActivity.class);
                    //menu.putExtra("username",tasksResult.getSuccess().getUsername());
                    //menu.putExtra("apiToken", tasksResult.getSuccess().getApiToken());
                    //startActivity(menu);
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
                    //TODO: end notification process (and clean all service data if exist)
                    //setResult(Activity.RESULT_OK);
                    //finish();
                }
            }
        });
    }

    private void updateUiWithUser(String jsonBody) {
        String body = "Load tasks is successful! " + jsonBody;
        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
    }

    private void showLoadFailed(Exception e) {
        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
