package com.iliadevy.electronicDiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/unsecured")
public class MainController {

    @GetMapping("/")
    public String unsecuredData() {
        return "Unsecured data";
    }
}


