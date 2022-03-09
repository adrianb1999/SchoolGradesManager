package com.adrian99.schoolGradesManager.security;

import com.adrian99.schoolGradesManager.jwt.JwtConfig;
import com.adrian99.schoolGradesManager.jwt.JwtTokenVerifier;
import com.adrian99.schoolGradesManager.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.adrian99.schoolGradesManager.service.implementation.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public SecurityConfig(ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManagerBean(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey, applicationUserService), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/teacher/**").hasAuthority("ROLE_TEACHER")
                .antMatchers("/api/student/**").hasAuthority("ROLE_STUDENT")
                .antMatchers("/","/index.html","/index","/login.html","/resetPassword.html","/passwordResetForm.html","/api/user/passwordReset").permitAll()
                .antMatchers("/createAdmin", "/api/user/resetPasswordLink").permitAll()
                .antMatchers("/css/style.css","/js/main.js").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowCredentials(true);

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        configuration.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
