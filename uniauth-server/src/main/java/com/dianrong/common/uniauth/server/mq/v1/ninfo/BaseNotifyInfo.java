package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import com.dianrong.common.uniauth.server.mq.v1.NotifyInfo;
import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanglin
 */
@ToString
@Slf4j
public class BaseNotifyInfo implements NotifyInfo {
    /**
     * 异常类型
     */
    protected NotifyInfoType notifyInfoType;

    @Override
    public NotifyInfoType getType() {
        if (this.notifyInfoType == null) {
            log.error("current NotifyInfo's notifyInfoType is null");
            throw new IllegalArgumentException("current NotifyInfo's notifyInfoType is null");
        }
        return this.notifyInfoType;
    }

    public BaseNotifyInfo setNotifyInfoType(NotifyInfoType notifyInfoType) {
        this.notifyInfoType = notifyInfoType;
        return this;
    }

    public NotifyInfoType getNotifyInfoType() {
        return notifyInfoType;
    }
}
