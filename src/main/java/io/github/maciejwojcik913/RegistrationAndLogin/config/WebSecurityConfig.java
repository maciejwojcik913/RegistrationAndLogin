package io.github.maciejwojcik913.RegistrationAndLogin.config;

import io.github.maciejwojcik913.RegistrationAndLogin.user.loging.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final Environment environment;

    @Autowired
    public WebSecurityConfig(Environment environment, CustomUserDetailsService userDetailsService) {
        this.environment = environment;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Encodes and decodes processing passwords.
     * @return instance of encoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Needed to set up UserDetailsService and PasswordEncoder.
     * @return bean important for AuthenticationManager
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Adds auth-provider to http security configuration.
     * @param http configuration object
     * @return AuthenticationManager bean
     * @throws AuthenticationException when auth fails
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    /**
     * Setup of users roles' hierarchy.
     * @return object used by DefaultWebSecurityExpressionHandler
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        var hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF and ROLE_STAFF > ROLE_USER");
        return hierarchy;
    }

    /**
     * Used by http authorized requests.
     * @return bean used in SecurityFilterChain's http setup.
     */
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        var webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return webSecurityExpressionHandler;
    }

    /**
     * Main configuration of http requests security.
     * @param http configuration object
     * @return configured filter chain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        if (profileIsActive("dev")) {
            http.csrf().disable();
            http.headers().frameOptions().disable();
        }

        http //TODO
                .authorizeRequests()
                .expressionHandler(webSecurityExpressionHandler())
                .antMatchers("/**").permitAll();

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Checks if environment accepts profile with given name.
     * @param profileName given profile name
     * @return true if environment accepts profile
     */
    private boolean profileIsActive(String profileName) {
        return this.environment.acceptsProfiles( Profiles.of(profileName) );
    }
}
