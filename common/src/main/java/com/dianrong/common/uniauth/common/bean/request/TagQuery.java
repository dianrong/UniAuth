package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

/**
 * Created by Arc on 7/4/2016.
 */
public class TagQuery extends PageParam {

    private Integer id;
    private List<Integer> tagIds;
    private String code;
    private Byte status;
    private Integer domainId;
    private Integer userId;
    private Integer grpId;

    public Integer getId() {
        return id;
    }

    public TagQuery setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public TagQuery setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TagQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public TagQuery setStatus(Byte status) {
        this.status = status;
        return this;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public TagQuery setDomainId(Integer domainId) {
        this.domainId = domainId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public TagQuery setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getGrpId() {
        return grpId;
    }

    public TagQuery setGrpId(Integer grpId) {
        this.grpId = grpId;
        return this;
    }
}
