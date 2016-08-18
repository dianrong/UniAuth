package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Arc on 7/4/2016.
 */
public class TagDto implements Serializable {

	private static final long serialVersionUID = -6998161636801183621L;
	private Integer id;
    private String code;
    private Byte status;
    private String description;
    private Integer tagTypeId;
    private String tagTypeCode;
    private Date createDate;
    private Date lastUpdate;
    
	//whether this tag directly connected with a user
	private Boolean tagUserChecked;
	
	//whether this tag directly connected with a group
	private Boolean tagGrouprChecked;

    public Boolean getTagUserChecked() {
		return tagUserChecked;
	}

	public TagDto setTagUserChecked(Boolean tagUserChecked) {
		this.tagUserChecked = tagUserChecked;
		return this;
	}

	public Boolean getTagGrouprChecked() {
		return tagGrouprChecked;
	}

	public TagDto setTagGrouprChecked(Boolean tagGrouprChecked) {
		this.tagGrouprChecked = tagGrouprChecked;
		return this;
	}

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

	@Override
	public String toString() {
		return "TagDto [id=" + id + ", code=" + code + ", status=" + status + ", description=" + description
				+ ", tagTypeId=" + tagTypeId + ", tagTypeCode=" + tagTypeCode + ", createDate=" + createDate
				+ ", lastUpdate=" + lastUpdate + ", tagUserChecked=" + tagUserChecked + ", tagGrouprChecked="
				+ tagGrouprChecked + "]";
	}
}
