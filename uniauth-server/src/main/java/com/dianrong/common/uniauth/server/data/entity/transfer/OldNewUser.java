package com.dianrong.common.uniauth.server.data.entity.transfer;

public class OldNewUser {
    private Long oldUserId;

    private Long newUserId;

    private String email;

    public Long getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(Long oldUserId) {
        this.oldUserId = oldUserId;
    }

    public Long getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(Long newUserId) {
        this.newUserId = newUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
}