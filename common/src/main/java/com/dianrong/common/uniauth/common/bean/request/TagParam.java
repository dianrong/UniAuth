package com.dianrong.common.uniauth.common.bean.request;

/**
 * Created by Arc on 12/4/2016.
 */
public class TagParam extends Operator {

    private Integer id;
    private String code;
    private Byte status;
    private Integer tagTypeId;
    private Integer domainId;
    private String description;

    public Integer getId() {
        return id;
    }

    public TagParam setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TagParam setCode(String code) {
        this.code = code;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public TagParam setStatus(Byte status) {
        this.status = status;
        return this;
    }

    public Integer getTagTypeId() {
        return tagTypeId;
    }

    public TagParam setTagTypeId(Integer tagTypeId) {
        this.tagTypeId = tagTypeId;
        return this;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public TagParam setDomainId(Integer domainId) {
        this.domainId = domainId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TagParam setDescription(String description) {
        this.description = description;
        return this;
    }
}
