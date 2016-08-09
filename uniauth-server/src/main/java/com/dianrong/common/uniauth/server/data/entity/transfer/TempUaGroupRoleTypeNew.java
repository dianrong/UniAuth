package com.dianrong.common.uniauth.server.data.entity.transfer;

public class TempUaGroupRoleTypeNew {
    private Integer id;

    private String grtCode;

    private String name;

    private Integer trstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrtCode() {
        return grtCode;
    }

    public void setGrtCode(String grtCode) {
        this.grtCode = grtCode == null ? null : grtCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTrstatus() {
        return trstatus;
    }

    public void setTrstatus(Integer trstatus) {
        this.trstatus = trstatus;
    }
}