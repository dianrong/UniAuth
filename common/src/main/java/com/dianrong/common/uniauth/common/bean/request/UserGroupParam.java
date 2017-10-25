package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import java.util.List;
import lombok.ToString;

@ToString
@ApiModel("用户组操作请求参数")
public class UserGroupParam extends PageParam {

  private static final long serialVersionUID = -4261321484001881493L;

  private List<Long> userIds;

  private String rootGrpCode;

  public List<Long> getUserIds() {
    return userIds;
  }

  public UserGroupParam setUserIds(List<Long> userIds) {
    this.userIds = userIds;
    return this;
  }

  public String getRootGrpCode() {
    return rootGrpCode;
  }

  public UserGroupParam setRootGrpCode(String rootGrpCode) {
    this.rootGrpCode = rootGrpCode;
    return this;
  }
}
