package com.dianrong.common.uniauth.common.bean;

/**
 * 第三方账号的类型.
 */
public enum ThirdAccountType {
  IPA((byte)1), OA((byte)2);
  
  private ThirdAccountType(byte type) {
    this.type = type;
  }
  
  private byte type;

  public byte getType() {
    return type;
  }
  
  /**
   * 便利方法. 用于通过byte type获取对应的Type枚举. 如果不能找到,则返回null.
   */
  public static ThirdAccountType getType(byte type) {
    ThirdAccountType[] values = ThirdAccountType.values();
    for (ThirdAccountType atp: values) {
      if (atp.getType() == type) {
        return atp;
      }
    }
    return null;
  }
}
