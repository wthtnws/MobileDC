package com.example.mobiledc.ui.secondfactor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.mobiledc.databinding.ActivitySecondfactorBinding;
import com.example.mobiledc.ui.login.LoggedInUserView;
import com.example.mobiledc.ui.login.LoginFormState;
import com.example.mobiledc.ui.login.LoginResult;
import com.example.mobiledc.ui.menu.MenuActivity;

public class SecondFactorActivity extends AppCompatActivity {

    private SecondFactorViewModel secondFactorViewModel;
    private ActivitySecondfactorBinding secondfactorBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        secondfactorBinding = ActivitySecondfactorBinding.inflate(getLayoutInflater());
        setContentView(secondfactorBinding.getRoot());

        secondFactorViewModel = new SecondFactorViewModel();

        final EditText codeText = secondfactorBinding.code;
        final Button secFactorButton = secondfactorBinding.check2factor;
        final ProgressBar loadingProgressBar = secondfactorBinding.loading;

        secondFactorViewModel.getLogin2Result().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult login2Result) {
                if (login2Result == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (login2Result.getError() != null) {
                    showLoginFailed(login2Result.getError());
                }
                if (login2Result.getSuccess() != null) {
                    updateUiWithUser(login2Result.getSuccess());
                    Intent menu = new Intent(SecondFactorActivity.this, MenuActivity.class);
                    menu.putExtra("username",login2Result.getSuccess().getUsername());
                    menu.putExtra("apiToken", login2Result.getSuccess().getApiToken());
                    startActivity(menu);
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        secondFactorViewModel.getLogin2FormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState login2FormState) {
                if (login2FormState == null) {
                    return;
                }
                secFactorButton.setEnabled(login2FormState.isDataValid());
                if (login2FormState.getCodeError() != null) {
                    codeText.setError(getString(login2FormState.getCodeError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                secondFactorViewModel.loginDataChanged(codeText.getText().toString());
            }
        };
        codeText.addTextChangedListener(afterTextChangedListener);
        codeText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    secondFactorViewModel.secondFactor((String) getIntent().getSerializableExtra("username"),
                            codeText.getText().toString());
                }
                return false;
            }
        });


        secFactorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                secondFactorViewModel.secondFactor((String) getIntent().getSerializableExtra("username"),
                        codeText.getText().toString());
            }
        });
    }
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Authentication step 2 is successful! " + model.getUsername();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
