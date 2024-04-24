package com.backend.demo.security;

import com.backend.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${api.version.controller.path}")
    private String backendVersionController;

    @Value("${api.userAuth.path}")
    private String authController;

    @Value("${api.gateway.actuator.path}")
    private String gatewayActuator;

    @Value("${api.users.actuator.path}")
    private String usersActuator;

    @Value("${api.users.notification.path}")
    private String userNotifications;

    @Value("${api.version.control.path}")
    private String versionController;

    @Value("${api.property.path}")
    private String propertyController;

    @Value("${api.data.migrate.controller.path}")
    private String dataMigrateController;

    @Value("${api.admin.path}")
    private String adminController;

    @Value("${api.endorsement.path}")
    private String endorsementController;

    private final JwtRequestFilter jwtRequestFilter;
    @Autowired
    public WebSecurity(Environment environment, UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder, JwtRequestFilter jwtRequestFilter) {
        this.environment = environment;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> {
            CorsConfiguration cors = new CorsConfiguration();
            cors.setAllowedOrigins(Collections.singletonList("*"));
            cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(Collections.singletonList("*"));
            return cors;
        });
        http.authorizeRequests().
//                antMatchers("/swagger-resources/**").permitAll().
//                antMatchers("/swagger-ui/**").permitAll().
//                antMatchers("/v3/api-docs").permitAll().
                antMatchers(adminController).permitAll().
                antMatchers(endorsementController).permitAll().
                antMatchers(dataMigrateController).permitAll().
                antMatchers(backendVersionController).permitAll().
                antMatchers(propertyController).permitAll().
                antMatchers(authController).permitAll().
                antMatchers(gatewayActuator).permitAll().
                antMatchers(usersActuator).permitAll().
                antMatchers(userNotifications).permitAll().
                antMatchers(versionController).permitAll().
                anyRequest().authenticated().and().sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
