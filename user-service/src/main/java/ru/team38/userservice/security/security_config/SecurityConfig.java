package ru.team38.userservice.security.security_config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.team38.userservice.security.jwt.JwtAuthenticationEntryPoint;
import ru.team38.userservice.security.jwt.JwtAuthenticationFailureHandler;
import ru.team38.userservice.security.oauth.OAuth2AuthenticationSuccessHandler;
import ru.team38.userservice.security.security_services.RemoteUserDetailsService;
import ru.team38.userservice.security.jwt.JWTUtil;
import ru.team38.userservice.security.jwt.JwtAuthenticationFilter;
import ru.team38.userservice.security.oauth.OAuth2AuthenticationFailureHandler;
import ru.team38.userservice.security.security_services.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTUtil jwtTokenUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final UserDetailsService remoteUserDetailsService;

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JWTUtil jwtTokenUtil,
                          @Lazy CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                          JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler,
                          RemoteUserDetailsService remoteUserDetailsService) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.jwtAuthenticationFailureHandler = jwtAuthenticationFailureHandler;
        this.remoteUserDetailsService = remoteUserDetailsService;
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(remoteUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/admin/**").hasRole("ROLE_ADMIN")
                    .requestMatchers("/profile/**").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                    .requestMatchers("/**").permitAll()
                    .anyRequest().authenticated()
                .and().formLogin()
                    .loginPage("/signin").failureUrl("/signin")
                    .failureHandler(jwtAuthenticationFailureHandler)
                    .defaultSuccessUrl("/profile")
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/signin")
                    .deleteCookies("token")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .and().oauth2Login()
                    .loginPage("/signin")
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                  .and()
                    .redirectionEndpoint()
                    .baseUri("/oauth2/callback/*")
                  .and()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                  .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
                  .and().oauth2Client();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenUtil);
    }
}