package com.mutra.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mutra.demo.filters.JwtRequestFilter;
import com.mutra.demo.services.UserDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				//permits all types of calls to the /authenticate endpoint
				.authorizeRequests().antMatchers("/authenticate").permitAll()
				// any request should be authenticated
				.anyRequest().authenticated()
				//tells spring security to not store Sessions
				.and().sessionManagement() 
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//adds filter before the default(?) chain to check if the header has a jwt
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
					
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	// it actually doesnt encrypt passwords
	@Bean
	public PasswordEncoder passwordEncoder() {
		return  NoOpPasswordEncoder.getInstance();
	}
	
}
