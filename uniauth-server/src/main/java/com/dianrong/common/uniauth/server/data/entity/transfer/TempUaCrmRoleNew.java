package com.dianrong.common.uniauth.server.data.entity.transfer;

import java.util.Date;

public class TempUaCrmRoleNew {
    private Integer id;

    private String roleCode;

    private String name;

    private String description;

    private Byte status;

    private Date createDate;

    private Date lastUpdate;

    private Integer trstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getTrstatus() {
        return trstatus;
    }

    public void setTrstatus(Integer trstatus) {
        this.trstatus = trstatus;
    }
}