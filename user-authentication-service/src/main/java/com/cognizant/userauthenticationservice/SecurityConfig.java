package com.cognizant.userauthenticationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cognizant.userauthenticationservice.security.JwtAuthorizationFilter;
import com.cognizant.userauthenticationservice.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("Inside configure()");
		http.cors().and().csrf().disable().httpBasic()
		.and()
		.authorizeRequests()
		.antMatchers("/authenticate").permitAll()
		.antMatchers(HttpMethod.POST,"/users").anonymous()
		.antMatchers(HttpMethod.PUT, "/users").authenticated()
		.antMatchers(HttpMethod.GET, "/users/**").permitAll()
		.antMatchers(HttpMethod.POST, "/managers").hasAnyRole("ADMIN", "SUPER_USER")
		.antMatchers(HttpMethod.GET, "/managers/**").hasAnyRole("ADMIN", "SUPER_USER")
		.antMatchers(HttpMethod.DELETE, "/managers/**").hasAnyRole("ADMIN", "SUPER_USER")
		.antMatchers(HttpMethod.PUT, "/managers/**").hasAnyRole("SUPER_USER")
		.antMatchers("/admins").hasRole("SUPER_USER")
		.and()
		.addFilter(new JwtAuthorizationFilter(authenticationManager()))
		.logout().logoutUrl("/logout");
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
