package net.proselyte.springbootdemo.details;

import net.proselyte.springbootdemo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
/**
 * Класс UserDetails определяет методы для получения информации о пользователе, который пытается получить доступ к данным, -
 * getAuthorities(): возвращает коллекцию объектов GrantedAuthority, представляющих разрешения, предоставленные пользователю;
 * getPassword(): возвращает пароль пользователя;
 * getUsername(): возвращает имя пользователя;
 * isAccountNonExpired(): проверяет, не истек ли срок действия учетной записи пользователя;
 * isAccountNonLocked(): проверяет, заблокирована ли учетная запись пользователя;
 * isCredentialsNonExpired(): проверяет, не истек ли срок действия учетных данных пользователя;
 * isEnabled(): проверяет, активен ли пользователь в системе.
 */
public class UserDetails implements  org.springframework.security.core.userdetails.UserDetails{
    private User user;
    public UserDetails(User user) { this.user=user;}
    public User getUser() {return user;}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword(){return getUser().getPassword();}

    @Override
    public String getUsername() {return  getUser().getLogin();}

    @Override
    public boolean isAccountNonExpired() {return false;}

    @Override
    public boolean isAccountNonLocked() {return false;}

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {return false;}
}
