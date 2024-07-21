package com.dawson.document.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.dawson.document.entities.JWTDetails;
import com.dawson.document.repositories.JWTRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    @Autowired
    private JWTRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<JWTDetails> user = userRepository.findById(username);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return new User(username, "", new ArrayList<>());
    }
}
