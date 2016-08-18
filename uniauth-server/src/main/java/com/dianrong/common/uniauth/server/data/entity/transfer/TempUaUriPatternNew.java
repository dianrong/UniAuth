package com.dianrong.common.uniauth.server.data.entity.transfer;

import java.util.Date;

public class TempUaUriPatternNew {
    private Integer id;

    private Integer domainId;

    private String name;

    private String description;

    private String method;

    private String pattern;

    private Date createDate;

    private Date lastUpdate;

    private Integer trstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern == null ? null : pattern.trim();
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