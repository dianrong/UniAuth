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
  @ApiModelProperty("该扩展属性对应的值,可为空")
  private String value;

  public Long getId() {
    return id;
  }

  public AttributeExtendParam setId(Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public AttributeExtendParam setCode(String code) {
    this.code = code;
    return this;
  }

  public String getCategory() {
    return category;
  }

  public AttributeExtendParam setCategory(String category) {
    this.category = category;
    return this;
  }

  public String getSubcategory() {
    return subcategory;
  }

  public AttributeExtendParam setSubcategory(String subcategory) {
    this.subcategory = subcategory;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public AttributeExtendParam setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getValue() {
    return value;
  }

  public AttributeExtendParam setValue(String value) {
    this.value = value;
    return this;
  }
}

