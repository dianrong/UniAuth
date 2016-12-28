package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.enm.UserActionEnum;

public class UserParam extends Operator {

	private static final long serialVersionUID = 8550894865549572653L;
	private Long id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String originPassword;
    private Byte status;
    
    private Long tenancyId;
    
    private String tenancyCode;

    private UserActionEnum userActionEnum;
    
    private Integer domainId;
    
    private List<Integer> roleIds;
    
    private List<Integer> tagIds;
    
    private String groupCode;
    private Boolean includeSubGrp;
    private List<String> includeRoleNames;
    private String roleDomainCode;
    
	public List<Integer> getTagIds() {
		return tagIds;
	}

	public UserParam setTagIds(List<Integer> tagIds) {
		this.tagIds = tagIds;
		return this;
	}

	public Long getId() {
		return id;
	}

	public UserParam setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public UserParam setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public UserParam setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserParam setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserParam setPassword(String password) {
		this.password = password;
		return this;
	}

	public UserActionEnum getUserActionEnum() {
		return userActionEnum;
	}

	public UserParam setUserActionEnum(UserActionEnum userActionEnum) {
		this.userActionEnum = userActionEnum;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public UserParam setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public UserParam setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public UserParam setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public String getOriginPassword() {
		return originPassword;
	}

	public UserParam setOriginPassword(String originPassword) {
		this.originPassword = originPassword;
		return this;
	}

	public String getTenancyCode() {
		return tenancyCode;
	}

	public UserParam setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
		return this;
	}

    public Long getTenancyId() {
        return tenancyId;
    }

    public UserParam setTenancyId(Long tenancyId) {
        this.tenancyId = tenancyId;
        return this;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public UserParam setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public Boolean getIncludeSubGrp() {
        return includeSubGrp;
    }

    public UserParam setIncludeSubGrp(Boolean includeSubGrp) {
        this.includeSubGrp = includeSubGrp;
        return this;
    }

    public List<String> getIncludeRoleNames() {
        return includeRoleNames;
    }

    public UserParam setIncludeRoleNames(List<String> includeRoleNames) {
        this.includeRoleNames = includeRoleNames;
        return this;
    }

    public String getRoleDomainCode() {
        return roleDomainCode;
    }

    public UserParam setRoleDomainCode(String roleDomainCode) {
        this.roleDomainCode = roleDomainCode;
        return this;
    }

    @Override
    public String toString() {
        return "UserParam [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", password=" + password + ", originPassword=" + originPassword + ", status=" + status
                + ", tenancyId=" + tenancyId + ", tenancyCode=" + tenancyCode + ", userActionEnum=" + userActionEnum + ", domainId=" + domainId + ", roleIds=" + roleIds + ", tagIds=" + tagIds
                + ", groupCode=" + groupCode + ", includeSubGrp=" + includeSubGrp + ", includeRoleNames=" + includeRoleNames + ", roleDomainCode=" + roleDomainCode + "]";
    }
}
