package net.proselyte.springbootdemo.security;

import net.proselyte.springbootdemo.model.User;
import net.proselyte.springbootdemo.details.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import net.proselyte.springbootdemo.service.UserDetailsService;
import org.springframework.stereotype.Component;
/**
 *  Класс UserProvider обеспечивает логику аутентификации для пользователя, пытающегося войти в систему со своими учетными данными.
 *  Он использует интерфейс UserDetailsService для получения сведений о пользователе на основе его имени.
 *  Затем он получает объект пользователя из экземпляра UserDetails и сравнивает введенный пароль с сохраненным.
 *  Если учетные данные совпадают, он создает новый объект Authentication, используя класс UsernamePasswordAuthenticationToken,
 *  и возвращает его. Если введенные учетные данные неверны, создается исключение BadCredentialsException.
 *  Если пользователь с введенным именем пользователя не существует, он выдает другой экземпляр того же исключения.
 */
@Component
public class UserProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        if (credentials == null) throw new BadCredentialsException("Не удалось войти в аккаунт");
        String password = credentials.toString();

        UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
        User user = userDetails.getUser();

        if (user == null) throw new BadCredentialsException("Пользователь с таким логином не найден");
        if (!user.getPassword().equals(password)) throw new BadCredentialsException("Пароль введён неверно");

        return new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword(), userDetails.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {return true;}
}

