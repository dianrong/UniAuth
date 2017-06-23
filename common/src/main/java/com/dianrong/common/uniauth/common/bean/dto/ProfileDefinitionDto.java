package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;
import java.util.Set;

import lombok.ToString;

@ToString
@ApiModel("Profile的结构定义Dto")
public class ProfileDefinitionDto extends TenancyBaseDto {

  private static final long serialVersionUID = -1765107973570952500L;

  @ApiModelProperty("主键Id.即ProfileId")
  private Long id;
  
  @ApiModelProperty("Profile的名称,可为中文")
  private String name;
  
  @ApiModelProperty("所有Profile区分开来的编码")
  private String code;
  
  @ApiModelProperty("Profile的描述信息")
  private String description;
  
  @ApiModelProperty("Profile对应的扩展属性列表(扩展属性Code和描述)")
  private Map<String, String> attributes;
  
  @ApiModelProperty("所有子ProfileId的集合")
  private Set<Long> descendantProfileIds;

  public String getName() {
    return name;
  }

  public ProfileDefinitionDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getCode() {
    return code;
  }

  public ProfileDefinitionDto setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public ProfileDefinitionDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public Long getId() {
    return id;
  }

  public ProfileDefinitionDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public ProfileDefinitionDto setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Set<Long> getDescendantProfileIds() {
    return descendantProfileIds;
  }

  public ProfileDefinitionDto setDescendantProfileIds(Set<Long> descendantProfileIds) {
    this.descendantProfileIds = descendantProfileIds;
    return this;
  }
}
