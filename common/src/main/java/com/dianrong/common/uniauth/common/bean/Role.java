package com.dianrong.common.uniauth.common.bean;

/**
 * Created by Arc on 14/1/16.
 */
public class Role {

    private Integer id;
    private String code;

    public Integer getId() {
        return id;
    }

    public Role setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Role setCode(String code) {
        this.code = code;
        return this;
    }
}
