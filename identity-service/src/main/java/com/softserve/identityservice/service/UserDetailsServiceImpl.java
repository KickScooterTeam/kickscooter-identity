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
        if(!user.isVerified()){
            return new User(user.getId().toString(), user.getPassword(), false, true,
                    true, true, user.getRole());
        }else if(user.isBlocked()){
            return new User(user.getId().toString(), user.getPassword(),false, false,
                    false,false, user.getRole());
        }
        return new User(user.getId().toString(), user.getPassword(), user.getRole());
    }
}
