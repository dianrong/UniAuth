package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

/**
 * Created by Arc on 7/4/2016.
 */
public class TagQuery extends PageParam {

	private static final long serialVersionUID = -7447070647269360141L;
	private Integer id;
    private List<Integer> tagIds;
    private String code;
    private String fuzzyCode;
    private Byte status;
    private Integer tagTypeId;
    private Integer domainId;
    private String domainCode;
    private List<Integer> domainIds;
    private Long userId;
    private Integer groupId;

    public String getDomainCode() {
        return domainCode;
    }

    public TagQuery setDomainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    public String getFuzzyCode() {
        return fuzzyCode;
    }

    public TagQuery setFuzzyCode(String fuzzyCode) {
        this.fuzzyCode = fuzzyCode;
        return this;
    }

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

	@Override
	public String toString() {
		return "TagQuery [id=" + id + ", tagIds=" + tagIds + ", code=" + code + ", fuzzyCode=" + fuzzyCode + ", status="
				+ status + ", tagTypeId=" + tagTypeId + ", domainId=" + domainId + ", domainCode=" + domainCode
				+ ", domainIds=" + domainIds + ", userId=" + userId + ", groupId=" + groupId + "]";
	}
}
