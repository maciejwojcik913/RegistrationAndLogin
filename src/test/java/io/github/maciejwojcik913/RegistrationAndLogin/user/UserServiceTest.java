package io.github.maciejwojcik913.RegistrationAndLogin.user;

import io.github.maciejwojcik913.RegistrationAndLogin.exception.ConfirmationDoNotMatchToDefinedPasswordException;
import io.github.maciejwojcik913.RegistrationAndLogin.exception.EmailAlreadyExistsException;
import io.github.maciejwojcik913.RegistrationAndLogin.exception.UsernameAlreadyExistsException;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.RoleRepository;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.User;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRepository;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRole;
import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.CustomUserDetails;
import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.CustomUserDetailsService;
import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.UserSignInForm;
import io.github.maciejwojcik913.RegistrationAndLogin.user.registration.UserSignUpForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserServiceTest {

    private UserService SUT;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    @Mock private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    @Mock private CustomUserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);

    @BeforeEach
    void setupE() {
        userRepository = Mockito.mock(UserRepository.class);
        SUT = new UserService(this.userRepository, this.passwordEncoder, roleRepository, userDetailsService);
    }


    @Test
    void registerNewUser_shouldThrow_EmailAlreadyExistsException_ifGivenEmailAlreadyExists() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(new User()));
        var regForm = new UserSignUpForm("email", null, null, null);

        // when then
        assertThrows(EmailAlreadyExistsException.class, () -> SUT.registerNewUser(regForm));
    }

    @Test
    void registerNewUser_shouldThrow_UsernameAlreadyExistsException_ifGivenLoginAlreadyExists() {
        // given
        given(userRepository.findByLogin(any(String.class))).willReturn(Optional.of(new User()));
        var regForm = new UserSignUpForm(null, "login", null, null);

        // when then
        assertThrows(UsernameAlreadyExistsException.class, () -> SUT.registerNewUser(regForm));
    }

    @Test
    void registerNewUser_shouldThrow_ConfirmationDoNotMatchToDefinedPasswordException_ifRegFormHasDifferentPasswordAndItsConfirmation() {
        // given
        var regForm = new UserSignUpForm(null, null, "Password", "Wrong Confirm");

        // when then
        assertThrows(ConfirmationDoNotMatchToDefinedPasswordException.class, () -> SUT.registerNewUser(regForm));
    }

    @Test
    void registerNewUser_shouldNotThrow_and_shouldReturnRegFormWithEmailAndLoginAndEmptyPassword() {
        // given
        var regForm = new UserSignUpForm("email", "login", "secret", "secret");
        var defaultRole = new UserRole("ROLE_USER");
        var encodedPassword = "********";
        var user = new User("email", "login", encodedPassword);
        user.setRoles(Set.of(defaultRole));

        given(passwordEncoder.encode(any(String.class))).willReturn("********");
        given(roleRepository.getDefaultRole()).willReturn(defaultRole);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        var registered = SUT.registerNewUser(regForm);

        // then
        assertAll(
                () -> assertDoesNotThrow( () -> SUT.registerNewUser(regForm) ),
                () -> assertThat(registered.getEmail(), equalTo(regForm.getEmail())),
                () -> assertThat(registered.getLogin(), equalTo(regForm.getLogin())),
                () -> assertThat(registered.getPassword().length(), equalTo(0)),
                () -> assertThat(registered.getPwdConf().length(), equalTo(0))
        );
    }

    @Test
    void loginUser_shouldRethrowExceptionFrom_UserDetailsService() {
        // given
        given(userDetailsService.loadUserByUsername("wrongUsername")).willThrow(UsernameNotFoundException.class);
        var loginForm = new UserSignInForm("wrongUsername", "pwd");

        // when then
        assertThrows(UsernameNotFoundException.class, () -> SUT.loginUser(loginForm));
    }

    @Test
    void loginUser_shouldReturnUserDetailsIfFormIsCorrect() {
        // given
        var user = new User("email", "login", "password");
        user.setRoles( Set.of( new UserRole("ROLE_USER") ) );
        given(userDetailsService.loadUserByUsername(any())).willReturn(new CustomUserDetails(user));
        var loginForm = new UserSignInForm( user.getLogin(), user.getPassword() );

        // when
        var userDetails = SUT.loginUser(loginForm);

        // then
        assertAll(
                () -> assertThat(userDetails.getUsername(), equalTo(user.getLogin())),
                () -> assertThat(userDetails.getEmail(), equalTo(user.getEmail())),
                () -> assertThat(userDetails.getPassword().length(), equalTo(0)),
                () -> assertTrue(userDetails.isEnabled()),
                () -> assertTrue(userDetails.getAuthorities().stream().anyMatch(
                        a -> a.getAuthority().equals("ROLE_USER")))
        );
    }
}