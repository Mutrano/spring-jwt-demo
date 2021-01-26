package com.mutra.demo.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mutra.demo.entities.AuthenticationRequest;
import com.mutra.demo.entities.AuthenticationResponse;
import com.mutra.demo.services.UserDetailsServiceImpl;
import com.mutra.demo.services.util.JwtUtil;

@RestController
public class HelloResource {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@GetMapping(value="/hello")
	public String hello() {
		return "Hello world";
	}
	
	@PostMapping(value="/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest   ) throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch(BadCredentialsException expn) {
			throw new Exception("Incorred username or password", expn) ;
		}
		
		final UserDetails userDetails = userDetailsServiceImpl
				.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok().body(new AuthenticationResponse(jwt));
	}
}
