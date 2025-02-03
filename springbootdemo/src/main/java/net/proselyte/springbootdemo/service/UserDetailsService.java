package net.proselyte.springbootdemo.service;

import net.proselyte.springbootdemo.details.UserDetails;
import net.proselyte.springbootdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Класс UserDetailsService представляет собой реализацию интерфейса проверки прав доступа пользователей в системе.
 * loadUserByUsername получает входной параметр - имя пользователя, который пытается зайти в систему,
 * и выполняет поиск этого пользователя в базе данных с помощью findByUsername(username)).
 * Затем найденный пользователь конвертируется в объект класса UserDetails, который содержит информацию о правах доступа,
 * включая список ролей, разрешений и другую информацию для проверки прав.
 * Далее возвращается объект UserDetails, который будет использоваться для проверки прав доступа пользователя в системе.
 */
@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        return new net.proselyte.springbootdemo.details.UserDetails(user);
    }
}
