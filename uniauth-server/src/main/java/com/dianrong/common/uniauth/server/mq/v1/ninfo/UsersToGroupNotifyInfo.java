package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.ToString;

@ToString
public class UsersToGroupNotifyInfo extends BaseNotifyInfo {
    /**
     * 关联关系中的用户id
     */
    protected List<Long> userIds;
    
    /**
     * 关联关系中的组id
     */
    protected Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public UsersToGroupNotifyInfo setGroupId(Integer groupId) {
        this.groupId = groupId;
        return this;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public UsersToGroupNotifyInfo setUserId(Long userId) {
        if (this.userIds == null) {
            this.userIds = Lists.newArrayList();
        }
        this.userIds.add(userId);
        return this;
    }
    
    public UsersToGroupNotifyInfo setUserIds(List<Long> userIds) {
        this.userIds = userIds;
        return this;
    }
}
