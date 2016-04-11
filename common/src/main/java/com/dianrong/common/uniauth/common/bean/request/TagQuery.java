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
    private Integer tagTypeId;
    private Integer domainId;
    private List<Integer> domainIds;
    private Long userId;
    private Integer groupId;

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

    public Long getUserId() {
        return userId;
    }

    public TagQuery setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Integer getTagTypeId() {
        return tagTypeId;
    }

    public TagQuery setTagTypeId(Integer tagTypeId) {
        this.tagTypeId = tagTypeId;
        return this;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public TagQuery setDomainId(Integer domainId) {
        this.domainId = domainId;
        return this;
    }

    public List<Integer> getDomainIds() {
        return domainIds;
    }

    public TagQuery setDomainIds(List<Integer> domainIds) {
        this.domainIds = domainIds;
        return this;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public TagQuery setGroupId(Integer groupId) {
        this.groupId = groupId;
        return this;
    }
}
