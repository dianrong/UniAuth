package com.dianrong.common.uniauth.client;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;

@Component
public class UniClientCommonService {

    public UserExtInfo getLoginUserInfo() {
        UserExtInfo userExtInfo = (UserExtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userExtInfo;
    }

}
