package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;
import com.dianrong.common.uniauth.server.test.BaseTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserExtendValServiceTest extends BaseTest {

  @Autowired
  private UserExtendValInnerService userExtendValInnerService;
  
  @Test
  public void addOrUpdateTest() {
    CxfHeaderHolder.TENANCYCODE.set("DIANRONG");
    Long userId = 300000019L;
    Long extendId =1L;
    String value = "test";
    userExtendValInnerService.addNew(userId, extendId, value);
  }
}
