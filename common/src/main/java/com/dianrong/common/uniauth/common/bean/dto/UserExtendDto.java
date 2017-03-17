package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@ApiModel("用户扩展属性")
public class UserExtendDto extends TenancyBaseDto {

    private static final long serialVersionUID = -6617306737768606631L;

    @ApiModelProperty(value = "主键id", required = true)
    private Long id;
    @ApiModelProperty(value = "扩展属性code", required = true)
    private String code;
    @ApiModelProperty("属性描述信息")
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
}

