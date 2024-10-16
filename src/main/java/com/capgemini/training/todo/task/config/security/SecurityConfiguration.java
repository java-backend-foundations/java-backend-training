package com.capgemini.training.todo.task.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user", "/h2-console/**").anonymous()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(customizer -> customizer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Password is stored in memory and also can be obtained by deserialization. That way cannot be used in PROD.
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("adminPass")
                .roles(AccessControl.ROLE_ADMIN, AccessControl.ROLE_MAINTAINER)
                .build();
        UserDetails maintainer = User.withDefaultPasswordEncoder()
                .username("maintainer")
                .password("maintainerPass")
                .roles(AccessControl.ROLE_MAINTAINER)
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("userPass")
                .build();
        return new InMemoryUserDetailsManager(admin, maintainer, user);
    }

}
