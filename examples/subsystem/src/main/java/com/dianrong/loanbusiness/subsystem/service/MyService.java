package com.dianrong.loanbusiness.subsystem.service;

import com.dianrong.loanbusiness.subsystem.MyInterface;
import com.dianrong.loanbusiness.subsystem.model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

public class MyService {

  @SuppressWarnings("unused")
  @Autowired(required = false)
  private MyInterface myInterface;

  /**
   * 测试服务.
   */
  @PreAuthorize("hasRole('ROLE_USER')")
  public void testService() {
    for (int i = 0; i < 10; i++) {
      System.out.println(
          "--------------------------------------------------------------------------------------");
    }
  }

  @PreAuthorize("hasPermission(#tm, 'all')")
  public void testModel(TestModel tm) {

  }
}
