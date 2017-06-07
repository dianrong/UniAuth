package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel("租户信息")
@Data
public class TenancyBaseDto implements Serializable {

  private static final long serialVersionUID = 7612408066715418041L;

  // 租户id
  @ApiModelProperty("租户id")
  private Integer tenancyId;

  // 租户Code
  @ApiModelProperty("租户code")
  private String tenancyCode;

  public Integer getTenancyId() {
    return tenancyId;
  }

  public TenancyBaseDto setTenancyId(Integer tenancyId) {
    this.tenancyId = tenancyId;
    return this;
  }

  public String getTenancyCode() {
    return tenancyCode;
  }

  public TenancyBaseDto setTenancyCode(String tenancyCode) {
    this.tenancyCode = tenancyCode;
    return this;
  }
}
