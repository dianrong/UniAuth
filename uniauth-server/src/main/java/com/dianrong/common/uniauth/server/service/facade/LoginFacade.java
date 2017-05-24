package com.dianrong.common.uniauth.server.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.util.InnerStringUtil;
import com.dianrong.common.uniauth.server.service.IPAService;
import com.dianrong.common.uniauth.server.service.UserService;

/**
 * 用于处理用户登陆相关API,兼容IPA和MySQL
 * 
 * @author wanglin
 */

@Service
public class LoginFacade {

    @Autowired
    private UserService userService;
    
    @Autowired
    private IPAService ipaService;
    
    public UserDto login(LoginParam loginParam) {
        if (InnerStringUtil.isIPAAccount(loginParam.getAccount())) {
            return ipaService.login(loginParam);
        }
        return userService.login(loginParam);
    }

    public UserDetailDto getUserDetailInfo(LoginParam loginParam) {
        if (InnerStringUtil.isIPAAccount(loginParam.getAccount())) {
            return ipaService.getUserDetailInfo(loginParam);
        }
        return userService.getUserDetailInfo(loginParam);
    }
    
    public UserDto getUserByEmailOrPhone(LoginParam loginParam) {
        if (InnerStringUtil.isIPAAccount(loginParam.getAccount())) {
            return ipaService.getUserByEmailOrPhone(loginParam);
        }
        return userService.getUserByEmailOrPhone(loginParam);
    }
}
