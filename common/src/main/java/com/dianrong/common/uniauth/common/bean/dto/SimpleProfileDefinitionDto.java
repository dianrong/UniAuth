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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((subProfiles == null) ? 0 : subProfiles.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SimpleProfileDefinitionDto other = (SimpleProfileDefinitionDto) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (subProfiles == null) {
      if (other.subProfiles != null) {
        return false;
      }
    } else if (!subProfiles.equals(other.subProfiles)) {
      return false;
    }
    return true;
  }
}
