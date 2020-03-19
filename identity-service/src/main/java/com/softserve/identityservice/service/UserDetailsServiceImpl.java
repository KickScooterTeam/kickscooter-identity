package com.softserve.identityservice.service;

import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User with email " + email + " doesn't exists"));
        return new User(user.getEmail(), user.getPassword(), user.getRole());
    }
}
