package com.iliadevy.electronicDiary.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret.key}")
    private static String secretKey;

    @Value("${jwt.lifetime}")
    private static Duration lifetime;

    //Метод для генерации токена из информации о пользователе.
    public static String generateToken(UserDetails userDetails) {

        //Формируем payload(полезная информация) для токена.
        Map<String, Object> claims = new HashMap<>();

        //Список ролей пользователя.
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roleList);
        //Можно добавить и другие поля, например, email, если принимать User, а не UserDetails.

        //Время создания токена.
        Date issuedDate = new Date();
        //Время создания + время жизни токена.
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                //Наполнили полезными данными
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                //Добавили подпись.
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


    }

    //Метод для получения username из токена.
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    //Метод для получения списка ролей из токена.
    public List<String> getRolesFromToken(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    //Общий метод для разбора приходящего токена на куски.
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }


}
