package com.iliadevy.electronicDiary.controllers;

import com.iliadevy.electronicDiary.dtos.JwtRequest;
import com.iliadevy.electronicDiary.dtos.RegistrationUserDto;
import com.iliadevy.electronicDiary.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    //Метод, который отвечает за вывод страницы авторизации.
    @GetMapping("/login")
    public String showLoginPage(){
        return "LoginPage";
    }

    //Метод, который отвечает за вывод страницы регистрации.
    @GetMapping("/registration")
    public String showRegistrationPage(){
        return "RegistrationPage";
    }

    //Post метод принимает JwtRequest, внутри которого username и password для аутентификации пользователя.
    @PostMapping("/login")
    public String createAuthenticationToken(@ModelAttribute JwtRequest authRequest) {
        return authService.createAuthenticationToken(authRequest);
    }

    //Метод регистрации нового пользователя.
    @PostMapping("/registration")
    public String createNewUser(@ModelAttribute RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
