package io.github.maciejwojcik913.RegistrationAndLogin.user;

import io.github.maciejwojcik913.RegistrationAndLogin.validation.ManualConstraintValidator;
import io.github.maciejwojcik913.RegistrationAndLogin.validation.MatchingFields;
import io.github.maciejwojcik913.RegistrationAndLogin.validation.MatchingFieldsGroups;
import io.github.maciejwojcik913.RegistrationAndLogin.validation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Write model for registration of user.<br>
 * Provides javax validation of UserSignUpForm object.
 */

@MatchingFieldsGroups({
        @MatchingFields(fields = {"password", "pwdConf"}, name = "passwords")
})
public class UserSignUpForm {

    // TODO: extract messages to file

    private final String INVALID_EMAIL_MESSAGE = "Email is not valid";
    private final String EMAIL_REGEXP = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @NotNull
    @NotEmpty
    @Email(message = INVALID_EMAIL_MESSAGE, regexp = EMAIL_REGEXP)
    private String email;

    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @NotEmpty
    @Password(minLength = 8)
    private String password;

    @NotNull
    @NotEmpty
    private String pwdConf;

    public UserSignUpForm() {
    }

    public UserSignUpForm(String email, String login, String password, String pwdConf) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.pwdConf = pwdConf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwdConf() {
        return pwdConf;
    }

    public void setPwdConf(String pwdConf) {
        this.pwdConf = pwdConf;
    }

    /**
     * Validates this form using ManualConstraintValidator.
     * @return true if all annotated fields are valid.
     */
    public boolean isValid() {
        return ManualConstraintValidator.isValid(this);
    }
}
