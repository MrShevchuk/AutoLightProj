package com.brainacad.security.controller;

import com.brainacad.security.controller.validation.UserValidator;
import com.brainacad.security.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {


    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;

    @GetMapping({"/registration"})
    public String registration (Model model) {
        model.addAttribute("userForm", new UserForm());
        return "registrstion";
    }

    @PostMapping(value = "/registrstion")
    public String registration (@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model)
        userValidator.validate (userForm, bindingResult);

    if (bindlingResult.hasErrors()) {
        return "registration";
    }

    User newUser = new User();
    newUser.setEmail(userForm.getEmail());
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    newUser.setPassword(encoder.encode(userForm.getPassword()));
    userService.save(newUser);

    securityService.autoLogin(userForm.getEmail(), userForm.getPassword());
    return "redirect:index";

    @GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error !=null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return  "login";
    }
}
