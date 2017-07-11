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
  
  /**
   * Profile的属性Key.
   */
  public static final String ATTRIBUTES = "attributes";
  
  /**
   * Profile的子Profile的key.
   */
  public static final String SUB_PROFILE = "subProfile";

  @ApiModelProperty("主键Id.即ProfileId")
  private Long id;

  @ApiModelProperty("Profile的名称,可为中文")
  private String name;

  @ApiModelProperty("所有Profile区分开来的编码")
  private String code;

  @ApiModelProperty("Profile的描述信息")
  private String description;

  @ApiModelProperty("Profile对应的扩展属性列表(扩展属性Code和描述)")
  private Map<String, AttributeExtendDto> attributes;

  @ApiModelProperty("所有子ProfileId的集合,用于在添加和更新的时候指定profile的下一层的profile id集合")
  private Set<Long> descendantProfileIds;

  @ApiModelProperty("用于在返回结果的时候嵌套返回子ProfileId的关系")
  private Set<SimpleProfileDefinitionDto> subProfiles;

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

  public Map<String, AttributeExtendDto> getAttributes() {
    return attributes;
  }

  public ProfileDefinitionDto setAttributes(Map<String, AttributeExtendDto> attributes) {
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

  public Set<SimpleProfileDefinitionDto> getSubProfiles() {
    return subProfiles;
  }

  public ProfileDefinitionDto setSubProfiles(Set<SimpleProfileDefinitionDto> subProfiles) {
    this.subProfiles = subProfiles;
    return this;
  }
}
