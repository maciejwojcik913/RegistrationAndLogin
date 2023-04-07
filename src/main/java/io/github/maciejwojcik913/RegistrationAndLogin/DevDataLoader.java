package io.github.maciejwojcik913.RegistrationAndLogin;

import io.github.maciejwojcik913.RegistrationAndLogin.user.UserService;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.RoleRepository;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.User;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRepository;
import io.github.maciejwojcik913.RegistrationAndLogin.user.dao.UserRole;
import io.github.maciejwojcik913.RegistrationAndLogin.user.registration.UserSignUpForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Profile("dev")
@Component
final class DevDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    private final Logger log = LoggerFactory.getLogger(DevDataLoader.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() { //TODO log

        // INITIAL DATA: ROLES
        var rUser = roleRepository.save( new UserRole("ROLE_USER") );
        log.info("Saved role: " + rUser.getName());

        var rAdmin = roleRepository.save( new UserRole("ROLE_ADMIN") );
        log.info("Saved role: " + rAdmin.getName());

        var rStaff = roleRepository.save( new UserRole("ROLE_STAFF") );
        log.info("Saved role: " + rStaff.getName());

        // INITIAL DATA: USERS
        var uUser = new User("someuser@anything.xyz", "user1234", passwordEncoder.encode("user1234"));
        uUser.setRoles(Set.of(rUser));
        var savedU = userRepository.save(uUser);
        log.info("Saved user {email: " + savedU.getEmail() + ", login: " + savedU.getLogin() + ", role: " + savedU.getRoles().stream().findFirst().get().getName() + "}");

        var uAdmin = new User("someadmin@anything.xyz", "admin1234", passwordEncoder.encode("admin1234"));
        uAdmin.setRoles(Set.of(rAdmin));
        var savedA = userRepository.save(uAdmin);
        log.info("Saved user {email: " + savedA.getEmail() + ", login: " + savedA.getLogin() + ", role: " + savedA.getRoles().stream().findFirst().get().getName() + "}");


        var uStaff = new User("somestaff@anything.xyz", "staff1234", passwordEncoder.encode("staff1234"));
        uStaff.setRoles(Set.of(rStaff));
        var savedS = userRepository.save(uStaff);
        log.info("Saved user {email: " + savedS.getEmail() + ", login: " + savedS.getLogin() + ", role: " + savedS.getRoles().stream().findFirst().get().getName() + "}");

        // TEST REGISTRATION: USER
        var regForm = new UserSignUpForm("newUser@iamnew.asd", "newUser123", "myPass11", "myPass11");
        UserSignUpForm usuf = userService.registerNewUser(regForm);
        log.info("Registered successfully user with email: " + usuf.getEmail() + " and login: " + usuf.getLogin());
    }
}
