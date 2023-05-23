package com.screen.screen001.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/screen001")

public class AdminController {

    @GetMapping("/adminmenu")
    // @PreAuthorize("hasRole('ADMIN')")
    String adminMenu(Model model) {
        return "admin";
    }

    @GetMapping("/trainingadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addTraining(Model model) {
        return "addTraining";
    }

    @GetMapping("/memberadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addMember(Model model) {
        return "addMember";
    }
}
