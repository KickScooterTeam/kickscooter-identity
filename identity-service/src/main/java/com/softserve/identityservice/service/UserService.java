package com.softserve.identityservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.identityservice.converter.AppUserDtoToEmailVerificationDtoConverter;
import com.softserve.identityservice.converter.SignUpToUserConverter;
import com.softserve.identityservice.exception.AuthorizationException;
import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.EmailVerificationDto;
import com.softserve.identityservice.model.SignInDto;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpToUserConverter userConverter;
    private final AppUserDtoToEmailVerificationDtoConverter emailVerificationDtoConverter;
    private final TokenService tokenService;
    private final KafkaTemplate<UUID, EmailVerificationDto> emailVerificationDtoKafkaTemplate;

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
    public AppUser signUp(SignUpDto request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AuthorizationException("User with a suchlike email already exist");
        }else{
            AppUser appUser = userConverter.convert(request);
            sendVerificationEmail(emailVerificationDtoConverter.convert(appUser));
            return userRepository.save(appUser);
        }
    }

    @Transactional
    public String activateAccount(UUID token) throws ServletException {
        AppUser user = userRepository.findByVerifyToken(token).orElseThrow(() ->
                new UsernameNotFoundException("Incorrect token"));
        user.setVerified(true);
        return tokenService.createToken(user.getEmail(), user.getRole());
    }

    @Transactional
    public long blockUser(UUID id){
        int resultOfUpdating = userRepository.updateBlockedStatus(id);
        if(resultOfUpdating == 0){
            throw new UsernameNotFoundException("User with the id " + id + " doesn't exist");
        }else{
            return resultOfUpdating;
        }
    }

    private void sendVerificationEmail(EmailVerificationDto emailVerificationDto){
        emailVerificationDtoKafkaTemplate.send("email.verification", emailVerificationDto);
    }
}
