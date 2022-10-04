package com.example.mobiledc.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Requests {
    private static String URL = "https://192.168.1.105:8090/";
    private static MediaType mediaType = MediaType.parse("application/json");

    protected String uri;
    protected String method;
    protected RequestBody body;
    protected String token;

    private Requests() {}

    protected Request doRequest(String uri, String method, @Nullable RequestBody content, @NonNull String token){
        return new Request.Builder()
            .url(URL + uri)
            .method(method, content)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", token)
            .build();
    }

    public final static class Login1 extends Requests
    {
        public Login1 (String username, String password){
            uri = "login1";
            method = "POST";

            String content_body = new String("{\n" +
                    "    \"username\": \"" +
                    username +
                    "\",\n" +
                    "    \"password\": \"" +
                    password +
                    "\"\n"+
                    "}");
            body = RequestBody.create(mediaType, content_body);
            token = "null";
        }

        public Request postRequest() {
            return super.doRequest(uri,method,body,token);
        }
    }

    public final static class Login2 extends Requests
    {
        public Login2 (String username, String code){
            uri = "login2";
            method = "POST";

            String content_body = new String("{\n" +
                    "    \"username\": \"" +
                    username +
                    "\",\n" +
                    "    \"code\": \"" +
                    code +
                    "\",\n"+
                    "    \"is_mobile\": " +
                    "true" +
                    "\n"+
                    "}");
            body = RequestBody.create(mediaType, content_body);
            token = "";
        }

        public Request postRequest() {
            return super.doRequest(uri,method,body,token);
        }
    }

    public final static class Tasks extends Requests
    {
        public Tasks(String token){
            uri = "tasks";
            method = "GET";
            body = null;
            this.token = token;
        }

        public Request getRequest() {
            return super.doRequest(uri,method,body,token);
        }
    }

    public final static class Logout extends Requests
    {
        public Logout(String token) {
            uri = "logout";
            method = "POST";
            body = RequestBody.create(mediaType, "{}");;
            this.token = token;
        }
        public Request postRequest() { return super.doRequest(uri,method,body,token);}
    }

}
