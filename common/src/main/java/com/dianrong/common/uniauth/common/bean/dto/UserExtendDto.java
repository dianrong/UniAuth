package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
public class UserExtendDto implements Serializable {
    
    private static final long serialVersionUID = -6617306737768606631L;
    
    private Long id;
    private String code;
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
		return "UserExtendDto [id=" + id + ", code=" + code + ", description=" + description + "]";
	}
}

