package  com.example.mobiledc.ui.login;

import androidx.annotation.Nullable;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String username;
    private String apiToken;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(String displayName, @Nullable String apiToken) {
        this.username = displayName;
        this.apiToken =apiToken;
    }

    public String getUsername() {
        return username;
    }
    public String getApiToken() {
        return apiToken;
    }
}