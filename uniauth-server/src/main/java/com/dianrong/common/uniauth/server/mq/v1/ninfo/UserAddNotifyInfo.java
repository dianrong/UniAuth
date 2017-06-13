package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;
import lombok.ToString;

@ToString
public class UserAddNotifyInfo extends BaseUserNotifyInfo {

  /**
   * 姓名.
   */
  private String name;

  /**
   * 邮箱.
   */
  private String email;

  /**
   * 电话.
   */
  private String phone;

  public UserAddNotifyInfo() {
    super.setType(NotifyInfoType.USER_ADD);
  }

  public String getName() {
    return name;
  }

  public UserAddNotifyInfo setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserAddNotifyInfo setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public UserAddNotifyInfo setPhone(String phone) {
    this.phone = phone;
    return this;
  }
}
