package com.screen.screen001.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/screen001")
public class UserController {

    @GetMapping("/menu")
    String menu(Model model) {
        return "menu";
    }

    @GetMapping("/trainingboard/{id}")
    String trainingBoard(Model model) {
        return "trainingBoard";
    }

    @GetMapping("/trainingboard/{id}/edit")
    String trainingBoardEdit(Model model) {
        return "trainingBoardEdit";
    }

    @GetMapping("/memberlist")
    String memberList(Model model) {
        return "memberList";
    }

    @GetMapping("/memberlist/{id}")
    String memberListId(Model model) {
        return "memberListId";
    }

    @GetMapping("/myprofile")
    String myprofile(Model model) {
        return "myprofile";
    }

    @GetMapping("/myprofile/edit")
    String editMyprofile(Model model) {
        return "editMyprofile";
    }

}
