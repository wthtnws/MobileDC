package com.example.mobiledc.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
public class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer codeError;
    private boolean isDataValid;

    public LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer codeError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.codeError = codeError;
        this.isDataValid = false;
    }

    public LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.codeError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getCodeError() {
        return codeError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}