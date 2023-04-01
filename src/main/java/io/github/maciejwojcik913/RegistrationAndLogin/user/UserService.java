package io.github.maciejwojcik913.RegistrationAndLogin.user;

import io.github.maciejwojcik913.RegistrationAndLogin.exception.ConfirmationDoNotMatchToDefinedPasswordException;
import io.github.maciejwojcik913.RegistrationAndLogin.exception.EmailAlreadyExistsException;
import io.github.maciejwojcik913.RegistrationAndLogin.exception.UsernameAlreadyExistsException;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.RoleRepository;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.User;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRepository;
import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.CustomUserDetails;
import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.CustomUserDetailsService;
import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.UserSignInForm;
import io.github.maciejwojcik913.RegistrationAndLogin.user.registration.UserSignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registration of new user.<br>
     * Checks if username, email are free and if confirmation matches to password.
     * @param registrationForm to register
     * @return UserSignUpForm of saved user.
     */
    public UserSignUpForm registerNewUser(final UserSignUpForm registrationForm) {

        if (userRepository.findByEmail(registrationForm.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User with given email already exists.");
        }
        if (userRepository.findByLogin(registrationForm.getLogin()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with given username already exists.");
        }
        if (!registrationForm.getPassword().equals(registrationForm.getPwdConf())) {
            throw new ConfirmationDoNotMatchToDefinedPasswordException("Password and its confirmation are different.");
        }

        var user = new User(
                registrationForm.getEmail(),
                registrationForm.getLogin(),
                encodePassword(registrationForm.getPassword()));
        user.setRoles(Set.of(roleRepository.getDefaultRole()));
        var saved = userRepository.save(user);

        return new UserSignUpForm(saved.getEmail(), saved.getLogin(), "", "");
    }


    public CustomUserDetails loginUser(final UserSignInForm loginForm) {
        return userDetailsService.loadUserByUsername(loginForm.getEmailOrLogin());
    }

    /**
     * Encodes password by defined bean PasswordEncoder
     * @param password to encode
     * @return encoded password
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
