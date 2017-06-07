package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.ToString;

@ToString
@ApiModel("扩展属性操作请求参数")
public class AttributeExtendParam extends Operator {

  private static final long serialVersionUID = 1792555726955678797L;

  @ApiModelProperty("主键id")
  private Long id;
  @ApiModelProperty("用户扩展属性code")
  private String code;
  @ApiModelProperty("扩展属性的类别")
  private String category;
  @ApiModelProperty("扩展属性的子类别")
  private String subcategory;
  @ApiModelProperty("用户扩展属性描述信息")
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getSubcategory() {
    return subcategory;
  }

  public void setSubcategory(String subcategory) {
    this.subcategory = subcategory;
  }
}

