package com.iliadevy.electronicDiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/secured")
public class MainController {

    public String showMainPage() {
        return "MainPage";
    }
}
