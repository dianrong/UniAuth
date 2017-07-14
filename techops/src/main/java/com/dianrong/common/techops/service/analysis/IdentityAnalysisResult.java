package com.dianrong.common.techops.service.analysis;

import com.dianrong.common.uniauth.common.bean.request.UserParam;

import java.util.List;

/**
 * 解析输入得到的带用户身份识别的对象.
 * 
 * @author wanglin
 *
 */
public class IdentityAnalysisResult {

  private UserParam userParam;
  /**
   * 按顺序存放额外结果.
   */
  private List<String> appendInfo;

  public List<String> getAppendInfo() {
    return appendInfo;
  }

  public IdentityAnalysisResult setAppendInfo(List<String> appendInfo) {
    this.appendInfo = appendInfo;
    return this;
  }

  public UserParam getUserParam() {
    return userParam;
  }

  public IdentityAnalysisResult setUserParam(UserParam userParam) {
    this.userParam = userParam;
    return this;
  }

  @Override
  public String toString() {
    return "IdentityAnalysisResult [userParam=" + userParam + ", appendInfo=" + appendInfo + "]";
  }
}
