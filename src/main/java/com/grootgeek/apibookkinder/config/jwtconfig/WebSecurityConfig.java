package com.grootgeek.apibookkinder.config.jwtconfig;

import com.grootgeek.apibookkinder.service.PasswordEncoderService;
import com.grootgeek.apibookkinder.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {


    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, PasswordEncoderService pwEncoderService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.pwEncoderService = pwEncoderService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UsuarioDetailsService usuarioDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(usuarioDetailsService)
                .passwordEncoder(pwEncoderService.passwordEncoder())
                .and()
                .build();
    }
    private final JwtRequestFilter jwtRequestFilter;

    private final PasswordEncoderService pwEncoderService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/publico/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .anyRequest().authenticated()
                        .and())
                .cors()
                .and()
                .httpBasic(withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}

