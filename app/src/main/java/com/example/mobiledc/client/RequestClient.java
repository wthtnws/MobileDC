package com.example.mobiledc.client;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.mobiledc.data.Requests;

public class RequestClient {
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

    private String doRequest(Request request){
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("[-] Request", "Failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("[0] Response", "Received");
                if (response.isSuccessful()){
                    Log.i("[+] Response", "Successful");
                }
                else
                {
                    Log.i("[-] Response", "Unsuccessful");
                }
                Log.i("[+++] Response", response.body().string());
            }
        });
        return null;
    }

    public void postLogin1 (String username, String password){
        Requests.Login1 login1Request = new Requests.Login1(username, password);
        doRequest(login1Request.getRequest());
    }
}
