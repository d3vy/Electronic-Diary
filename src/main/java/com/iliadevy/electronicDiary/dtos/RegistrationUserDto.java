package com.iliadevy.electronicDiary.dtos;


public record RegistrationUserDto(
        String username,
        String password,
        String confirmPassword,
        String email,
        String firstname,
        String lastname
) {

}

