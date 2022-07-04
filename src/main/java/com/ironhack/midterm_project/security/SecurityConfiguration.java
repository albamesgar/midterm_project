package com.ironhack.midterm_project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/my-accounts/**").hasRole("ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.GET, "/my-user").hasAnyRole("ADMIN", "ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.POST, "/new/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/delete/**").hasRole("ADMIN")
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
