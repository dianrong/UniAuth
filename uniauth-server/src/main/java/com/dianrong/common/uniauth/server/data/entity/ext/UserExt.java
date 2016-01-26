package com.dianrong.common.uniauth.server.data.entity.ext;


/**
 * Created by Arc on 26/1/16.
 */
public class UserExt {

    private Long id;
    private String email;
    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public UserExt setGroupId(Integer groupId) {
        this.groupId = groupId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserExt setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserExt setEmail(String email) {
        this.email = email;
        return this;
    }
}
