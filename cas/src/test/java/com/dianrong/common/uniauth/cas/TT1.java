package com.dianrong.common.uniauth.cas;

import com.dianrong.common.uniauth.common.util.JsonUtil;

public class TT1 {

  private String aa;

  public TT1(String aa) {
    this.aa = aa;
  }

  public String getAa() {
    return aa;
  }

  public void setAa(String aa) {
    this.aa = aa;
  }

  public static void main(String[] args) {
    TT1 t = new TT1("wwf");
    String ss = JsonUtil.object2Jason(t);
    System.out.println(ss);
    System.out.println(JsonUtil.jsonToObject(ss, TT1.class));
  }
}
