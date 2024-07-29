package com.iliadevy.electronicDiary.controllers;

import com.iliadevy.electronicDiary.dtos.JwtRequest;
import com.iliadevy.electronicDiary.dtos.RegistrationUserDto;
import com.iliadevy.electronicDiary.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    //Post метод принимает JwtRequest, внутри которого username и password для аутентификации пользователя.
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthenticationToken(authRequest);
    }

    //Метод регистрации нового пользователя.
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
