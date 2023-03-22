package io.github.maciejwojcik913.RegistrationAndLogin.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class UserSignInForm {

    // TODO: extract messages to file
    private final String FIELDS_EMPTY = "Form contains empty fields.";

    @NotNull
    @NotEmpty(message = FIELDS_EMPTY)
    private String emailOrLogin;

    @NotNull
    @NotEmpty(message = FIELDS_EMPTY)
    private String password;

    public UserSignInForm() {
    }

    public UserSignInForm(String emailOrLogin, String password) {
        this.emailOrLogin = emailOrLogin;
        this.password = password;
    }

    public String getEmailOrLogin() {
        return emailOrLogin;
    }

    public void setEmailOrLogin(String emailOrLogin) {
        this.emailOrLogin = emailOrLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
