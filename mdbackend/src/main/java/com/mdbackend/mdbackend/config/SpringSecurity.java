package com.mdbackend.mdbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.mdbackend.mdbackend.enums.RoleEnum;
import com.mdbackend.mdbackend.filter.JwtFilter;
import com.mdbackend.mdbackend.service.impl.UserDetailServiceImp;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImp userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/passenger/register", "/passenger/login", "/superAdmin/login", "/busAdmin/login", "/bus/login").permitAll()
                        .antMatchers("/passenger/**").hasAuthority(RoleEnum.PASSENGER.getRoleName())
                        .antMatchers("/superAdmin/**").hasAuthority(RoleEnum.SUPER_ADMIN.getRoleName())
                        .antMatchers("/busAdmin/**").hasAuthority(RoleEnum.BUS_ADMIN.getRoleName())
                        .antMatchers("/bus/**").hasAuthority(RoleEnum.BUS.getRoleName())
                        .anyRequest().authenticated())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); 
        config.addAllowedHeader("*"); 
        config.addAllowedMethod("*"); 
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
