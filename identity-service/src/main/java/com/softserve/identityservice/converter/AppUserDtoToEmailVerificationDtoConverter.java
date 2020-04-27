package com.softserve.identityservice.converter;

import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.EmailVerificationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AppUserDtoToEmailVerificationDtoConverter implements Converter<AppUser, EmailVerificationDto> {
    private static final String PATH = "http://sslsecure.website/activation/";

    @Override
    public EmailVerificationDto convert(AppUser appUser) {
        EmailVerificationDto emailVerificationDto = new EmailVerificationDto();
        emailVerificationDto.setEmail(appUser.getEmail());
        emailVerificationDto.setVerificationPath(PATH + appUser.getVerifyToken());
        return emailVerificationDto;
    }
}
