package com.dianrong.common.uniauth.common.bean.dto;

/**
 * Created by Arc on 15/1/16.
 */
public class GroupCodeDto {

    private Integer id;
    private String code;
    private String description;

    public Integer getId() {
        return id;
    }

    public GroupCodeDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public GroupCodeDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GroupCodeDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
