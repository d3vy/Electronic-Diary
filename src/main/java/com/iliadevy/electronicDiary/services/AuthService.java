package com.iliadevy.electronicDiary.services;

import com.iliadevy.electronicDiary.dtos.JwtRequest;
import com.iliadevy.electronicDiary.dtos.JwtResponse;
import com.iliadevy.electronicDiary.dtos.RegistrationUserDto;
import com.iliadevy.electronicDiary.dtos.CustomerDto;
import com.iliadevy.electronicDiary.exceptions.ApplicationError;
import com.iliadevy.electronicDiary.models.User;

import com.iliadevy.electronicDiary.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;


    //Метод аутентификации пользователя. Принимает JwtRequest, внутри которого username и password.
    //
    public String createAuthenticationToken(JwtRequest authRequest) {

        //В идеале весь этот код переместить в service.
        //Ошибки обработать через глобальный перехват исключений.
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException e) {
            //Выдаем ошибку если данных нет.
            return "redirect:/auth/login";
        }

        //Генерация токена по username.
        UserDetails userDetails = userService.loadUserByUsername(authRequest.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        return "redirect:/secured/main";
    }

    //Метод регистрации нового пользователя.
    public String createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.password().equals(registrationUserDto.confirmPassword())) {
            return "redirect:/auth/registration";
        }
        if (userService.findByUsername(registrationUserDto.username()).isPresent()) {
            return "redirect:/auth/registration";
        }
        User user = userService.createNewUser(registrationUserDto);
        return "redirect:/auth/login";
    }
}
