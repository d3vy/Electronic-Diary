package com.iliadevy.electronicDiary.services;

import com.iliadevy.electronicDiary.dtos.RegistrationUserDto;
import com.iliadevy.electronicDiary.models.User;
import com.iliadevy.electronicDiary.repositories.RoleRepository;
import com.iliadevy.electronicDiary.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Setter
//Создает конструктор для final полей.
public class UserService implements UserDetailsService {


    private UserRepository userRepository;
    private RoleService roleService;
    private BCryptPasswordEncoder passwordEncoder;


    //Поиск пользователя по username.
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Получение пользователя по username.
    @Override
    //Если что-либо вызовет ошибку, то действия всего метода откатятся.
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("No user found with username '%s'.", username)
                ));

        //Возвращаем пользователя в виде класса User, который поймет Spring.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(
                                role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    //Нужно сделать проверку на корректность введенных данных.
    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));

        //Добавить ошибку, если нет роли.
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

}
