package com.dianrong.common.uniauth.server.data.entity.ext;


/**
 * Created by Arc on 26/1/16.
 */
public class UserExt {

    private Long id;
    private String email;
    private String name;
    private Integer groupId;
    private Byte userGroupType;

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
    public Byte getUserGroupType() {
        return userGroupType;
    }

    public UserExt setUserGroupType(Byte userGroupType) {
        this.userGroupType = userGroupType;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserExt setName(String name) {
        this.name = name;
        return this;
    }
}
