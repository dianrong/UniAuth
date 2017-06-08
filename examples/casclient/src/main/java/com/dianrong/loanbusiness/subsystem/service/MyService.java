package com.dianrong.loanbusiness.subsystem.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {

  /**
   * 测试方法.
   */
  public void testService() {
    for (int i = 0; i < 10; i++) {
      System.out.println(
          "-------------------------------------------------------------------------------------");
    }
  }
}
