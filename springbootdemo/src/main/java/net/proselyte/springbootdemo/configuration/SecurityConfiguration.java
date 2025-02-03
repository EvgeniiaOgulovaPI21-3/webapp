package net.proselyte.springbootdemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Конфигурационный класс содержит настройки для авторизации и аутентификации пользователей;
 * создает цепочку фильтров безопасности с настройками HTTP-запросов.
 */
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws  Exception {
        return httpSecurity
                .csrf().disable()
                .authorizeRequests().antMatchers("/","/author","/login","/registration").permitAll()
                .anyRequest().authenticated().and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/execute_login")
                .defaultSuccessUrl("/")
                .and()
                .logout().logoutSuccessUrl("/")
                .permitAll()
                .and().build();
    }

}
