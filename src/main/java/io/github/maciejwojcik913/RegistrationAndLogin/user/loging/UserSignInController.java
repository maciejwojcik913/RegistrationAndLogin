package io.github.maciejwojcik913.RegistrationAndLogin.user.loging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/login")
public class UserSignInController {

    private static final Logger log = LoggerFactory.getLogger(UserSignInController.class);

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userLogin", new UserSignInForm());
        return "login";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("userLogin") UserSignInForm ulf, BindingResult result, Model model) {
        if ( result.hasErrors() ) {
            return "login";
        } // TODO user auth here
        return "redirect:/api/welcome";
    }
}
