package com.iliadevy.electronicDiary.configs;


import com.iliadevy.electronicDiary.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter{

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,  @NonNull FilterChain filterChain) throws ServletException, IOException {
       String authHeader = request.getHeader("Authorization");
       String username = null;
       String token = null;

       if(authHeader != null && authHeader.startsWith("Bearer ")){
           token = authHeader.substring(7);
           try {
               username = jwtTokenUtils.getEmail(token);
           }catch (ExpiredJwtException e){
               log.debug("Время токена вышло");
           }catch (SignatureException e){
               log.debug("Подпись токена неверная");
           }
       }

       if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
           //Получаем токен аутентификации
           UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, jwtTokenUtils.getRoles(token).stream()
                   .map(SimpleGrantedAuthority::new).toList());
           //Добавляем токен в контекст.
           SecurityContextHolder.getContext().setAuthentication(authToken);
       }
       filterChain.doFilter(request, response);
    }
}

