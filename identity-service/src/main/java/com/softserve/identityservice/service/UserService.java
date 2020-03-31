package com.softserve.identityservice.service;

import com.softserve.identityservice.converter.AppUserDtoToEmailVerificationDtoConverter;
import com.softserve.identityservice.converter.SignUpToUserConverter;
import com.softserve.identityservice.converter.UserToUserInfo;
import com.softserve.identityservice.exception.AuthorizationException;
import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.EmailVerificationDto;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identityservice.model.UserInfoResponse;
import com.softserve.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SignUpToUserConverter userConverter;
    private final AppUserDtoToEmailVerificationDtoConverter emailVerificationDtoConverter;
    private final UserToUserInfo userToUserInfoConverter;
    private final TokenService tokenService;
    private final KafkaTemplate<UUID, EmailVerificationDto> emailVerificationDtoKafkaTemplate;

    @Transactional
    public AppUser signUp(SignUpDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthorizationException("User with a suchlike email already exist");
        } else {
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
        return tokenService.createToken(user.getId().toString(), user.getRole());
    }

    @Transactional
    public long blockUser(UUID id) {
        int resultOfUpdating = userRepository.updateBlockedStatus(id);
        if (resultOfUpdating == 0) {
            throw new UsernameNotFoundException("User with the id " + id + " doesn't exist");
        } else {
            return resultOfUpdating;
        }
    }

    @Transactional
    public UserInfoResponse userInfo(UUID id) {
        AppUser user = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist"));
        return userToUserInfoConverter.convert(user);
    }

    private void sendVerificationEmail(EmailVerificationDto emailVerificationDto) {
        String topic = "email-verification";
        emailVerificationDtoKafkaTemplate.send(topic, emailVerificationDto);
        log.info("sent verification to {}", topic);
    }
}
