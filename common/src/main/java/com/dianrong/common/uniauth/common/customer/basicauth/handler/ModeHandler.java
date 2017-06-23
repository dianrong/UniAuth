package com.dianrong.common.uniauth.common.customer.basicauth.handler;

import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import java.util.ArrayList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Created by denghb on 6/21/17.
 */
public interface ModeHandler {

  ArrayList<SimpleGrantedAuthority> handle(UserDetailDto userDetailDto, String domainDefine,
      String permissionType);
}
