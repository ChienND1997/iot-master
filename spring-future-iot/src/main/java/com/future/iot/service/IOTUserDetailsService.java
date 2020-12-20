package com.future.iot.service;

import com.future.iot.model.Employee;
import com.future.iot.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("userDetailsService")
public class IOTUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository empRepo;

    private static final String[] ROLE_ADMIN = {"ROLE_ADMIN", "ROLE_USER"};
    private static final String[] ROLE_USER = { "ROLE_USER"};


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = empRepo.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not authorized.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (user.getRole()) {
            case "ROLE_ADMIN": Arrays.asList(ROLE_ADMIN).forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
            case "ROLE_USER" : Arrays.asList(ROLE_USER).forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getHashPass(), true, true, true, true, authorities);
    }

}
