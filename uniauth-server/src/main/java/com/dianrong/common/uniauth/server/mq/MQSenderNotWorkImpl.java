package com.dianrong.common.uniauth.server.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 简单实现MQSender接口，没有实际发送mq消息
 * </pre>
 * @author cwl
 * @created Apr 14, 2016
 */
public class MQSenderNotWorkImpl implements MQSender{

    Logger logger=LoggerFactory.getLogger(MQSenderNotWorkImpl.class);
    
    private MQSenderNotWorkImpl() {}
    
    private static final MQSenderNotWorkImpl instance=new MQSenderNotWorkImpl();
    
    @Override
    public void send(String key, Object msgObj) {
        logger.info("没有发送消息，消息key：【"+key+"】，请检查是否启用mq...");
    }
    
    /**
     * singletype
     * @return
     */
    public static final MQSenderNotWorkImpl getInstance(){
        return instance;
    }
}


