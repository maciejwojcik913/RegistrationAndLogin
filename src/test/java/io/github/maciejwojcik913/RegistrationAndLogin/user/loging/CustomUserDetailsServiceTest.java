package io.github.maciejwojcik913.RegistrationAndLogin.user.loging;

import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.User;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CustomUserDetailsServiceTest {

    private CustomUserDetailsService SUT;
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = setUpPasswordEncoder();

    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        SUT = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturn_CustomUserDetails_withAppropriateDataIfUserExists() {
        //given
        var user = new User("test.user@testing.yxz", "user123", passwordEncoder.encode("secret"));
        user.setId(5L);
        given(userRepository.findByLoginOrEmail("test.user@testing.yxz")).willReturn(Optional.of(user));

        //when
        var userDetails = SUT.loadUserByUsername("test.user@testing.yxz");

        //then
        assertAll(
                () -> assertThat(userDetails.getId(), equalTo(5L)),
                () -> assertThat(userDetails.getEmail(), equalTo("test.user@testing.yxz")),
                () -> assertThat(userDetails.getUsername(), equalTo("user123")),
                () -> assertThat(userDetails.getPassword(), equalTo(passwordEncoder.encode("secret"))),
                () -> assertThat(userDetails.isEnabled(), is(true))
        );
    }

    @Test
    void loadUserByUsername_shouldThrow_UsernameNotFoundException_ifGivenArgumentDoesNotMatchToAnyUsernameOrEmail() {
        //given
        given(userRepository.findByLoginOrEmail(any(String.class))).willReturn( Optional.empty() );

        //when then
        assertThrows(UsernameNotFoundException.class,
                () -> SUT.loadUserByUsername("wrongArgument")
                );
    }

    /**
     * @return custom password encoder that imitates bean PasswordEncoder
     */
    private PasswordEncoder setUpPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                String[] encoded = {""};
                rawPassword.chars().forEach(ch -> encoded[0] += (char)(ch+13));
                return encoded[0];
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }
}