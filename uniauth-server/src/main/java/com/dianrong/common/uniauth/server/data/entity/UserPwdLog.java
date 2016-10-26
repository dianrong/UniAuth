package com.dianrong.common.uniauth.server.data.entity;

import java.util.Date;

/**.
 * 用户密码记录表
 * @author wanglin
 */
public class UserPwdLog {
	
	private Long id;
	
	private Long userId;
	
    private String password;

    private String passwordSalt;
    
    private Date createDate;
    
    private Long tenancyId;
    // query condtion
    private Date createDateBegin;
    
    private Date createDateEnd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDateBegin() {
		return createDateBegin;
	}

	public void setCreateDateBegin(Date createDateBegin) {
		this.createDateBegin = createDateBegin;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public Long getTenancyId() {
		return tenancyId;
	}

	public void setTenancyId(Long tenancyId) {
		this.tenancyId = tenancyId;
	}
}
