package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@ApiModel("用户扩展属性操作请求参数")
public class UserExtendParam extends Operator{

    private static final long serialVersionUID = 1792555726955678797L;

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("用户扩展属性code")
    private String code;
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
	@Override
	public String toString() {
		return "UserExtendParam [id=" + id + ", code=" + code + ", description=" + description + "]";
	}
}

