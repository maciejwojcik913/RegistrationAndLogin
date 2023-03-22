package io.github.maciejwojcik913.RegistrationAndLogin.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/register")
public class UserSignUpController {

    private static final Logger log = LoggerFactory.getLogger(UserSignUpController.class);

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userReg", new UserSignUpForm());
        return "register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("userReg") UserSignUpForm urf, BindingResult result, Model model) {
        if ( result.hasErrors() ) {
            return "register";
        }
        return "redirect:/api/login";
    }
}
