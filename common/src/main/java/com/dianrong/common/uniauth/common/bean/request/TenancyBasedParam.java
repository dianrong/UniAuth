package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel("调用Api涉及到的租户标识信息")
public class TenancyBasedParam implements Serializable {

  private static final long serialVersionUID = 7612408066715418041L;

  // 租户id
  @ApiModelProperty("租户id。某些区别租户的操作与tenancyCode，二传一")
  private Long tenancyId;

  // 租户Code
  @ApiModelProperty("租户code。某些区别租户的操作与tenancyId，二传一")
  private String tenancyCode;

  public Long getTenancyId() {
    return tenancyId;
  }

  public TenancyBasedParam setTenancyId(Long tenancyId) {
    this.tenancyId = tenancyId;
    return this;
  }

  public String getTenancyCode() {
    return tenancyCode;
  }

  public TenancyBasedParam setTenancyCode(String tenancyCode) {
    this.tenancyCode = tenancyCode;
    return this;
  }
}
