package com.example.mobiledc.ui.menu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobiledc.R;
import com.example.mobiledc.data.Requests;
import com.example.mobiledc.data.Result;
import com.example.mobiledc.ui.login.LoggedInUserView;
import com.example.mobiledc.ui.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MenuViewModel extends ViewModel {

    private OkHttpClient okHttpClient;
    private Requests.Tasks tasksRequest;
    private Requests.Logout logoutRequest;

    public MenuViewModel(String auth)
    {
        okHttpClient = new OkHttpClient().newBuilder().build();
        tasksRequest = new Requests.Tasks(auth);
        logoutRequest = new Requests.Logout(auth);
    }

    private MutableLiveData<Result<String>> tasksResult = new MutableLiveData<>();
    private MutableLiveData<Result<String>> logoutResult = new MutableLiveData<>();

    LiveData<Result<String>> getTasksResult() {
        return tasksResult;
    }
    LiveData<Result<String>> getLogoutResult() { return logoutResult; }

    public void reloadTasks(){
        okHttpClient.newCall(tasksRequest.getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tasksResult.setValue(new Result.Error(e));
                Log.i("[-] Request3", "Failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("[0] Response3", "Received");
                String respStr = response.body().string();
                Log.i("[+++] Response3", respStr);
                try {
                    JSONObject jsonObject = new JSONObject(respStr);
                    String status = jsonObject.getString("status");

                    if (response.isSuccessful()){
                        String tasks = jsonObject.getString("tasks");
                        //TODO: parse and handle the JSON body (maybe in Result.Success constructor)
                        tasksResult.postValue(new Result.Success<String>(tasks));
                        Log.i("[+] Response3 status", status);
                        Log.i("[+] Response3 token", "apiToken");
                    }
                    else
                    {
                        tasksResult.postValue(new Result.Error(new IOException()));
                        Log.i("[-] Response3", status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void logout(){
        okHttpClient.newCall(logoutRequest.postRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                logoutResult.postValue(new Result.Error(e));
                Log.i("[-] Request4", "Failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("[0] Response4", "Received");
                String respStr = response.body().string();
                Log.i("[+++] Response4", respStr);
                try {
                    JSONObject jsonObject = new JSONObject(respStr);
                    String status = jsonObject.getString("status");
                    if (response.isSuccessful()){
                        logoutResult.postValue(new Result.Success<String>(status));
                        Log.i("[+] Response4 status", status);
                    }
                    else
                    {
                        logoutResult.postValue(new Result.Error(new IOException()));
                        Log.i("[-] Response4", status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}