package com.iliadevy.electronicDiary.services;

import com.iliadevy.electronicDiary.dtos.JwtRequest;
import com.iliadevy.electronicDiary.dtos.JwtResponse;
import com.iliadevy.electronicDiary.dtos.RegistrationUserDto;
import com.iliadevy.electronicDiary.dtos.UserDto;
import com.iliadevy.electronicDiary.exceptions.ApplicationError;
import com.iliadevy.electronicDiary.models.User;

import com.iliadevy.electronicDiary.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    //Метод принимает JwtRequest, внутри которого username и password для аутентификации пользователя.

    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authRequest) {

        //В идеале весь этот код переместить в service.
        //Ошибки обработать через глобальный перехват исключений.
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            //Выдаем ошибку если данных нет.
            return new ResponseEntity<>(new ApplicationError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }

        //Генерация токена по username.
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = JwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    //Метод регистрации нового пользователя.
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Passwords do not match"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Username already exists"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}
