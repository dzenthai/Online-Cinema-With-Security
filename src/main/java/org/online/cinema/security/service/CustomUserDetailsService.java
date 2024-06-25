package org.online.cinema.security.service;

import org.online.cinema.security.domain.CustomUserDetails;
import org.online.cinema.store.entity.User;
import org.online.cinema.store.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }
}
