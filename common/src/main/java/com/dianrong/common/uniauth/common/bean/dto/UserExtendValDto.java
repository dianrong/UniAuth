package com.dianrong.common.uniauth.common.bean.dto;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
public class UserExtendValDto extends TenancyBaseDto {

    private static final long serialVersionUID = -4191944113318490773L;


    private Long id;

    private Long userId;

    private String value;

    private Byte status;

    private Long extendId;
    
    private String extendCode;
    
    private String extendDescription;
    
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

    public String getExtendCode() {
        return extendCode;
    }

    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }

    public String getExtendDescription() {
        return extendDescription;
    }

    public void setExtendDescription(String extendDescription) {
        this.extendDescription = extendDescription;
    }
}

