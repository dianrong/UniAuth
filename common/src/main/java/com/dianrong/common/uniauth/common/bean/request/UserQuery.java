package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

/**
 * Created by Arc on 15/1/16.
 */
public class UserQuery extends PageParam {

    private Long userId;
    private List<Long> userIds;
    private String name;
    private String phone;
    private String email;
    private Byte status;
    private Integer tagId;

    public Integer getTagId() {
        return tagId;
    }

    public UserQuery setTagId(Integer tagId) {
        this.tagId = tagId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserQuery setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public UserQuery setUserIds(List<Long> userIds) {
        this.userIds = userIds;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserQuery setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserQuery setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserQuery setEmail(String email) {
        this.email = email;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public UserQuery setStatus(Byte status) {
        this.status = status;
        return this;
    }
}
