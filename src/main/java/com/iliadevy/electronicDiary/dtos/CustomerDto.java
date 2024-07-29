package com.iliadevy.electronicDiary.dtos;

//Record - функция, используемая для упрощения кода.
//Чаще всего используется для создания DTO, где данные не изменяются,
// т.к. после создания объекта нельзя изменять данные.
public record CustomerDto(
        Long id,
        String username,
        String email,
        String firstname,
        String lastname
) {

}
