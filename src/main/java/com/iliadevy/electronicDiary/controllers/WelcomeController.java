package com.iliadevy.electronicDiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/unsecured")
public class WelcomeController {

    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "WelcomePage";
    }
}


