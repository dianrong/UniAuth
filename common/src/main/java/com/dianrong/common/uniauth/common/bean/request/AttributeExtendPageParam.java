package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
@ApiModel("扩展属性请求参数")
public class AttributeExtendPageParam extends PageParam {

  private static final long serialVersionUID = 1988526581461738102L;

  @ApiModelProperty("扩展属性对应的code")
  private String code;

  @ApiModelProperty("扩展属性的类别")
  private String category;

  @ApiModelProperty("扩展属性的子类别")
  private String subcategory;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

