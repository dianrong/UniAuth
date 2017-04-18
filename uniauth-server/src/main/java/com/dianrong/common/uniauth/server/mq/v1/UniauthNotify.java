package com.dianrong.common.uniauth.server.mq.v1;

/**
 * Uniauth的数据发生改变的通知
 * 
 * @author wanglin
 *
 */
public interface UniauthNotify {

    /**
     * 通知信息
     * @param notifyInfo 通知的信息对象,不能为空
     * @param NotifyFailedException 如果通知消息发送失败
     */
    void notify(NotifyInfo notifyInfo);
}
