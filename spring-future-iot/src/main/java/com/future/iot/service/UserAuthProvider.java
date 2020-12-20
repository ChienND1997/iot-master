package com.future.iot.service;

import com.future.iot.model.Employee;
import com.future.iot.repo.EmployeeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("userAuthPro")
public class UserAuthProvider implements AuthenticationProvider{
	private static final Logger   LOG = Logger.getLogger(UserAuthProvider.class);
	private static final String[] ROLE_ADMIN = {"ROLE_ADMIN", "ROLE_USER"};
	private static final String[] ROLE_USER = { "ROLE_USER"};

	@Autowired
	private EmployeeRepository empRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		Employee user = empRepo.findByName(username);
		if(user == null) return null;
		LOG.info("--------------------- > Found " + user + " by " +username +"<-----------------------");
		if(!user.getHashPass().equals(authentication.getCredentials())) return null;
		return successful(username, authentication.getCredentials().toString(), user.getRole());
	}

	private Authentication successful(String username, String password, String role) {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		switch (role) {
			case "ROLE_ADMIN": Arrays.asList(ROLE_ADMIN).forEach(r -> grantedAuths.add(new SimpleGrantedAuthority(r)));
			case "ROLE_USER" : Arrays.asList(ROLE_USER).forEach(r -> grantedAuths.add(new SimpleGrantedAuthority(r)));
		}
		return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
