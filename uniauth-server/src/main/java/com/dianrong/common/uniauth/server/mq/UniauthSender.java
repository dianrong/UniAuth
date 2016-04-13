package com.dianrong.common.uniauth.server.mq;

import com.dianrong.common.uniauth.server.data.entity.User;

/**
 * <pre>
 * uniauth中的消息发送接口
 * </pre>
 * @author cwl
 * @created Apr 11, 2016
 */
public interface UniauthSender{
    
    /**
     * 添加用户发送消息
     * @param user
     */
    public void sendUserAdd(User user);

}


