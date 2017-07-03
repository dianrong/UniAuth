package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;
import com.dianrong.common.uniauth.server.test.BaseTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserExtendValServiceTest extends BaseTest {

  @Autowired
  private UserExtendValInnerService userExtendValInnerService;
  
  @Test
  public void addOrUpdateTest() {
    Long userId = 1L;
    Long extendId =2L;
    String value = "test";
    userExtendValInnerService.addOrUpdate(userId, extendId, value);
  }
}
