package com.dawson.document.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dawson.document.jwt.JWTAuthenticationEntryPoint;
import com.dawson.document.jwt.JWTRequestFilter;
import com.dawson.document.jwt.JWTUserDetailsService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
@SuppressWarnings(value = "deprecation")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private JWTUserDetailsService jwtUserDetailsService;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	        http.csrf(csrf -> csrf
	                .disable())
	                .authorizeRequests(requests -> {
						try {
							requests
							        .antMatchers(
							                "/auth/sign-up",
							                "/auth/login",
							                "/document/share/get",
							                "/ws/document/**"
							        )
							        .permitAll()
							        .anyRequest()
							        .authenticated()            
							        .and()
							        .exceptionHandling()
							        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
							        .and()
							        .sessionManagement()
							        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
						} catch (Exception e) {
							log.info("Error in config");
							throw new RuntimeException();
						}
					});
	        http.cors();
	        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
