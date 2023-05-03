package com.screen.screen001.controller;

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
