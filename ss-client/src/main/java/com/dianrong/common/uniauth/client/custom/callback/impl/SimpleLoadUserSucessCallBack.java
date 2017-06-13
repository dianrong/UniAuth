package com.dianrong.common.uniauth.client.custom.callback.impl;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.custom.callback.LoadUserSuccessCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * LoadUserSuccessCallBack的简单实现.
 *
 * @author wanglin
 */
@Component
@Slf4j
public class SimpleLoadUserSucessCallBack implements LoadUserSuccessCallBack {

  @Override
  public void loadUserSuccess(UserExtInfo userDetail) {
    log.info("email:{}, tenancyId:{} load user info success!", userDetail.getUserDto().getEmail(),
        userDetail.getUserDto().getTenancyId());
  }
}
