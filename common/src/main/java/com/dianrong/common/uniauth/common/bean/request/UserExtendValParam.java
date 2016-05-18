package com.dianrong.common.uniauth.common.bean.request;


/**
 * @author wenlongchen
 * @since May 16, 2016
 */
public class UserExtendValParam extends PageParam {

    private static final long serialVersionUID = 5991602165228109411L;

    private Long id;

    private Long userId;

    private Long extendId;

    private String value;

    private Byte status;
    
    private String extendCode;
    
    /**.
     * 是否只查询被用户使用了的属性
     */
    private boolean queryOnlyUsed;
    
    public String getExtendCode() {
        return extendCode;
    }

    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }

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

    public Long getExtendId() {
        return extendId;
    }

    public void setExtendId(Long extendId) {
        this.extendId = extendId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	public boolean isQueryOnlyUsed() {
		return queryOnlyUsed;
	}

	public void setQueryOnlyUsed(boolean queryOnlyUsed) {
		this.queryOnlyUsed = queryOnlyUsed;
	}
}

