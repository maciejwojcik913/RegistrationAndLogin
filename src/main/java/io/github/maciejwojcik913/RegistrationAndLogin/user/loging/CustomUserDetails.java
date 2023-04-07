package io.github.maciejwojcik913.RegistrationAndLogin.user.loging;

import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.User;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Used to distinguish User entity and User from org.springframework.security.core.userdetails
 * <BR> Used by CustomUserDetailsService
 */
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String login;
    private final String password;
    private final boolean enabled;
    private final Collection<GrantedAuthority> authorities;

    /**
     * Default value for not supported superclass' fields.
     */
    private final boolean defaultOtherOptions = true;


    public CustomUserDetails(@NotNull User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.authorities = mapRolesToAuthorities(user.getRoles());
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.defaultOtherOptions;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.defaultOtherOptions;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.defaultOtherOptions;
    }

    /**
     * Used to adjust User's roles to UserDetails's authorities.
     * @param roles collection of UserRole used by User entity.
     * @return suitable collection of authorities (roles)
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(Collection<UserRole> roles) {
        return roles.stream().map(
                r -> new SimpleGrantedAuthority(r.getName())
        ).collect(Collectors.toSet());
    }
}
