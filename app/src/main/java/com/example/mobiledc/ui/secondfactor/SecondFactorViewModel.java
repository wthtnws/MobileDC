package com.example.mobiledc.ui.secondfactor;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobiledc.R;
import com.example.mobiledc.data.Requests;
import com.example.mobiledc.ui.login.LoggedInUserView;
import com.example.mobiledc.ui.login.LoginFormState;
import com.example.mobiledc.ui.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class SecondFactorViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> login2FormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> login2Result = new MutableLiveData<>();

    LiveData<LoginFormState> getLogin2FormState() {
        return login2FormState;
    }

    LiveData<LoginResult> getLogin2Result() {
        return login2Result;
    }

    public void secondFactor(String username, String code)
    {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Requests.Login2 login2Request = new Requests.Login2(username, code);
        okHttpClient.newCall(login2Request.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                login2Result.setValue(new LoginResult(R.string.login_failed));
                Log.i("[-] Request2", "Failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("[0] Response2", "Received");
                String respStr = response.body().string();
                Log.i("[+++] Response2", respStr);
                try {
                    JSONObject jsonObject = new JSONObject(respStr);
                    String status = jsonObject.getString("status");

                    if (response.isSuccessful()){
                        String apiToken = jsonObject.getString("apiToken");
                        login2Result.postValue(new LoginResult(new LoggedInUserView(username,apiToken)));
                        Log.i("[+] Response2 status", status);
                        Log.i("[+] Response2 token", apiToken);
                    }
                    else
                    {
                        login2Result.postValue(new LoginResult(R.string.login_failed));
                        Log.i("[-] Response2", status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void loginDataChanged(String code) {
        if (!isCodeValid(code)) {
            login2FormState.setValue(new LoginFormState(null, null,R.string.invalid_code));
        } else {
            login2FormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isCodeValid(String code){
        return code != null && code.trim().length() == 6;
    }
}
