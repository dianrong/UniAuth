package com.dianrong.common.uniauth.server.mq;

/**
 * <pre>
 * 定义消息发送接口
 * </pre>
 * @author cwl
 * @created Apr 11, 2016
 */
public interface MQSender {

    /**
     * 将msgObj对象包装成json字符串后发送
     * @param key
     * @param msgObj
     */
    void send(String key,Object msgObj);
}


