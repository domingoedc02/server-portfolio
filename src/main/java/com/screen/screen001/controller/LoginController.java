package com.screen.screen001.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/screen001")
public class LoginController {

    @GetMapping("/login")
    String login(Model model) {
        return "login";
    }

    @GetMapping("/changepassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String changePassword(Model model) {
        return "changePassword";
    }

    @GetMapping("/forgotpassword")
    String forgotPassword(Model model) {
        return "forgotPassword";
    }

    @GetMapping("/resetpassword")
    String resetPassword(Model model) {
        return "resetPassword";
    }

}
