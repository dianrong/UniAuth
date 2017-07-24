package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.ToString;

/**
 * @author wenlongchen.
 * @since May 16, 2016
 */
@ToString
@ApiModel("扩展属性")
public class AttributeExtendDto extends TenancyBaseDto {

  private static final long serialVersionUID = -6617306737768606631L;

  @ApiModelProperty(value = "主键id", required = true)
  private Long id;
  @ApiModelProperty(value = "扩展属性code", required = true)
  private String code;
  @ApiModelProperty(value = "扩展属性类别")
  private String category;
  @ApiModelProperty(value = "扩展属性子类别")
  private String subcategory;
  @ApiModelProperty("属性描述信息")
  private String description;
  @ApiModelProperty("对应属性的扩展值,可能为空")
  private String value;

  public Long getId() {
    return id;
  }

  public AttributeExtendDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public AttributeExtendDto setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public AttributeExtendDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getCategory() {
    return category;
  }

  public AttributeExtendDto setCategory(String category) {
    this.category = category;
    return this;
  }

  public String getSubcategory() {
    return subcategory;
  }

  public AttributeExtendDto setSubcategory(String subcategory) {
    this.subcategory = subcategory;
    return this;
  }

  public String getValue() {
    return value;
  }

  public AttributeExtendDto setValue(String value) {
    this.value = value;
    return this;
  }
}

