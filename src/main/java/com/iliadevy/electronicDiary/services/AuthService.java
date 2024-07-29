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


    //Метод принимает JwtRequest, внутри которого username и password для аутентификации пользователя.

    public ResponseEntity<?> createAuthenticationToken(JwtRequest authRequest) {

        //В идеале весь этот код переместить в service.
        //Ошибки обработать через глобальный перехват исключений.
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException e) {
            //Выдаем ошибку если данных нет.
            return new ResponseEntity<>(new ApplicationError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }

        //Генерация токена по username.
        UserDetails userDetails = userService.loadUserByUsername(authRequest.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    //Метод регистрации нового пользователя.
    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.password().equals(registrationUserDto.confirmPassword())) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Passwords do not match"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.username()).isPresent()) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Username already exists"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new CustomerDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstname(), user.getLastname()));
    }
}
