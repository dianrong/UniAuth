package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@ApiModel("用户扩展属性请求参数")
public class UserExtendPageParam extends PageParam {

    private static final long serialVersionUID = 1988526581461738102L;

    @ApiModelProperty("扩展属性对应的code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	@Override
	public String toString() {
		return "UserExtendPageParam [code=" + code + "]";
	}
}

