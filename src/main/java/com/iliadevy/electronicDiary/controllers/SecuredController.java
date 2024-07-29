package com.iliadevy.electronicDiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
@RequiredArgsConstructor
public class SecuredController {

    @GetMapping("/")
    public String securedData() {
        return "Unsecured data";
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
