package io.github.maciejwojcik913.RegistrationAndLogin.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        SUT = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturn_CustomUserDetails_withAppropriateDataIfUserExists() {
        //given
        var user = new User("test.user@testing.yxz", "user123", "secret");
        user.setId(5L);
        given(userRepository.findByLoginOrEmail("test.user@testing.yxz")).willReturn(Optional.of(user));

        //when
        var userDetails = SUT.loadUserByUsername("test.user@testing.yxz");

        //then
        assertAll(
                () -> assertThat(userDetails.getId(), equalTo(5L)),
                () -> assertThat(userDetails.getEmail(), equalTo("test.user@testing.yxz")),
                () -> assertThat(userDetails.getUsername(), equalTo("user123")),
                () -> assertThat(userDetails.getPassword(), equalTo("")),
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
}