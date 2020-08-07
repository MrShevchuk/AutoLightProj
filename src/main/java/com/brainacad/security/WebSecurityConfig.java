package com.brainacad.security;
import com.brainacad.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Настройка службы для поиска пользователя в базе данных.
        // и установка PasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // не требуют авторизациии
        http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();

        // Страница /userInfo требует входа в систему как ROLE_USER или ROLE_ADMIN.
        // Если нет логина, будет перенаправлен на страницу /login.
        http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

        // ТОлько для роли админ
        http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");

        // если нет прав (роли) для доступа к странице
        // будет брошено AccessDeniedException.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Конфиг для формы входа (логин страница)
        http.authorizeRequests().and().formLogin()
            .loginProcessingUrl("/j_spring_security_check") // Submit URL
            .loginPage("/login")//
            .defaultSuccessUrl("/userInfo")//
            .failureUrl("/login?error=true")//
            .usernameParameter("username")//
            .passwordParameter("password")
            // Конфиг для выхода
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");
    }

}
