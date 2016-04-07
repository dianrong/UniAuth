package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;

/**
 * Created by Arc on 7/4/2016.
 */
public class TagDto {

    private Integer id;
    private String code;
    private Byte status;
    private String description;
    private Integer tagTypeId;
    private String tagTypeCode;
    private Date createDate;
    private Date lastUpdate;

    public Integer getId() {
        return id;
    }

    public TagDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TagDto setCode(String code) {
        this.code = code;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public TagDto setStatus(Byte status) {
        this.status = status;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public TagDto setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public TagDto setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TagDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getTagTypeId() {
        return tagTypeId;
    }

    public TagDto setTagTypeId(Integer tagTypeId) {
        this.tagTypeId = tagTypeId;
        return this;
    }

    public String getTagTypeCode() {
        return tagTypeCode;
    }

    public TagDto setTagTypeCode(String tagTypeCode) {
        this.tagTypeCode = tagTypeCode;
        return this;
    }
}
