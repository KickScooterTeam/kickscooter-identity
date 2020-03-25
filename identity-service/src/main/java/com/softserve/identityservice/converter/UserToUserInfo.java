package com.softserve.identityservice.converter;

import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.UserInfoResponse;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserToUserInfo implements Converter<AppUser, UserInfoResponse> {
    @Override
    public UserInfoResponse convert(AppUser user) {
        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setEmail(user.getEmail());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        return userInfo;
    }
}
