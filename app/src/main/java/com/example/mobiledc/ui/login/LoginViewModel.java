package com.example.mobiledc.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.mobiledc.data.LoginRepository;
import com.example.mobiledc.data.Requests;
import com.example.mobiledc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
//        // can be launched in a separate asynchronous job
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Requests.Login1 login1Request = new Requests.Login1(username, password);
         okHttpClient.newCall(login1Request.postRequest()).enqueue(new Callback() {
             @Override
             public void onFailure(@NonNull Call call, @NonNull IOException e) {
                 loginResult.setValue(new LoginResult(R.string.login_failed));
                 Log.i("[-] Request", "Failed");
                 e.printStackTrace();
             }

             @Override
             public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                 Log.i("[0] Response", "Received");
                 String respStr = response.body().string();
                 Log.i("[+++] Response", respStr);
                 try {
                     JSONObject jsonObject = new JSONObject(respStr);
                     String jsonStr = jsonObject.getString("status");

                     if (response.isSuccessful()){
                         loginResult.postValue(new LoginResult(new LoggedInUserView(username,null)));
                         Log.i("[+] Response status", jsonStr);
                     }
                     else
                     {
                         loginResult.postValue(new LoginResult(R.string.login_failed));
                         Log.i("[-] Response", jsonStr);
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null,null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}