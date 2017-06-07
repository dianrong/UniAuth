package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Arc on 14/1/16.
 */
@ApiModel("调用api人的信息")
public class Operator extends TenancyBasedParam {

  private static final long serialVersionUID = 2506508045350825576L;

  @ApiModelProperty(value = "操作人的id", required = false)
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  protected Long opUserId;

  @ApiModelProperty(value = "操作对应的域id", required = false)
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  protected Integer opDomainId;

  public Operator() {
    opUserId = ReflectionUtils.getOpUserId();
    opDomainId = (Integer) ReflectionUtils.getStaticField(DomainDefine.class.getName(), "domainId");
  }

  public Long getOpUserId() {
    return opUserId;
  }

  public Integer getOpDomainId() {
    return opDomainId;
  }
}
