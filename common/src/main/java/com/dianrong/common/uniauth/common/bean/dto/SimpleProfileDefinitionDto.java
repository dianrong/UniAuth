package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

import lombok.ToString;

@ToString
@ApiModel("简单的Profile的结构定义Dto,为了表述父子关系")
public class SimpleProfileDefinitionDto extends TenancyBaseDto {

  private static final long serialVersionUID = -1765107973570952500L;

  @ApiModelProperty("主键Id.即ProfileId")
  private Long id;

  @ApiModelProperty("子Profile信息")
  private Set<SimpleProfileDefinitionDto> subProfiles;

  public Long getId() {
    return id;
  }

  public SimpleProfileDefinitionDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Set<SimpleProfileDefinitionDto> getSubProfiles() {
    return subProfiles;
  }

  public SimpleProfileDefinitionDto setSubProfiles(Set<SimpleProfileDefinitionDto> subProfiles) {
    this.subProfiles = subProfiles;
    return this;
  }
}
