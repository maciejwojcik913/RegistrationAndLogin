package io.github.maciejwojcik913.RegistrationAndLogin.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class WelcomeController {

    @GetMapping(path = "/welcome")
    public String getWelcomePage() {
        return "welcome";
    }

    @GetMapping(path = "/logout")
    public String logout() {
        return "welcome";
    }

    @GetMapping(path = "/user")
    public String goToUserPanel() {
        return "user-panel";
    }

    @GetMapping(path = "/admin")
    public String goToAdminPanel() {
        return "admin-panel";
    }

    @GetMapping(path = "/staff")
    public String goToStaffPanel() {
        return "staff-panel";
    }
}
