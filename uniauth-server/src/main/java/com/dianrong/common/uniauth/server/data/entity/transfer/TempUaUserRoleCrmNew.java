package com.dianrong.common.uniauth.server.data.entity.transfer;

public class TempUaUserRoleCrmNew {
    private Long userId;

    private Integer roleId;

    private Integer trstatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getTrstatus() {
        return trstatus;
    }

    public void setTrstatus(Integer trstatus) {
        this.trstatus = trstatus;
    }
}