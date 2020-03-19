package com.softserve.identityservice.service;

import com.softserve.identityservice.converter.SignUpToUserConverter;
import com.softserve.identityservice.exception.AuthorizationException;
import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.SignInDto;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpToUserConverter converter;

    @Transactional
    public AppUser signIn(SignInDto request){
        AppUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthorizationException("Email wrong or doesn't exist"));
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return user;
        }
        throw new AuthorizationException("Incorrect password");
    }

    @Transactional
    public AppUser signUp(SignUpDto request) throws AuthorizationException {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AuthorizationException("User with a suchlike email already exist");
        }else{
            return userRepository.save(converter.convert(request));
        }
    }
}
