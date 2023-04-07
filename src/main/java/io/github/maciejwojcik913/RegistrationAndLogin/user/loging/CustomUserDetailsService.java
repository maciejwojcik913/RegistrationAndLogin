package io.github.maciejwojcik913.RegistrationAndLogin.user.loging;

import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.User;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * CustomUserDetailsService supports signing in by login or email.<br>
 * Contains one method loadUserByUsername from interface UserDetailsService which returns custom implementation of UserDetails.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLoginOrEmail(username);
        CustomUserDetails userDetails;
        if (user.isPresent()) {
            userDetails = new CustomUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User with given username or email does not exist: " + username);
        }
        return userDetails;
    }
}
