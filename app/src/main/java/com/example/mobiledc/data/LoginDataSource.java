package com.example.mobiledc.data;

import android.util.Log;
import android.widget.Toast;

import com.example.mobiledc.client.RequestClient;
import com.example.mobiledc.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertStoreException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static RequestClient client = new RequestClient();

    public Result<LoggedInUser> login(String username, String password) {
        //сюда пихать GET/POST запросы на логин
        //+ понять как правильно ожидать ответа

        String resp = client.postLogin1(username, password);
        Log.i("[000] Response from postLogin1",resp);

        String message = null;
        try {
            JSONObject jsonObject = new JSONObject(resp);
            message = jsonObject.getString("status");
            Log.i("[000] Response from json",message);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if (message.equals("OK"))
        {
            LoggedInUser loggedInUser = new LoggedInUser("userID",username);
            return new Result.Success<>(loggedInUser);
        }
        else
            return new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}