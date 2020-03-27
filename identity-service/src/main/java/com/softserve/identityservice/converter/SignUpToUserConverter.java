package com.softserve.identityservice.converter;

import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identitystarter.model.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Component
@RequiredArgsConstructor
public class SignUpToUserConverter implements Converter<SignUpDto, AppUser> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser convert(SignUpDto dto) {
        AppUser user = new AppUser();
        Set<Role> role = new HashSet<>();
        role.add(Role.USER);
        user.setId(UUID.randomUUID());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setVerifyToken(UUID.randomUUID());
        user.setRole(role);
        return user;
    }
}
