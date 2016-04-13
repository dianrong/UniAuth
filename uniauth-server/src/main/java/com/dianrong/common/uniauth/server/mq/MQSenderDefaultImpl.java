package com.dianrong.common.uniauth.server.mq;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * mqsender默认实现
 * </pre>
 * @author cwl
 * @created Apr 11, 2016
 */
public class MQSenderDefaultImpl implements MQSender {

    private final Logger log = LoggerFactory.getLogger(MQSenderDefaultImpl.class);

    private RabbitTemplate template;

    private static ObjectMapper objectMapper = new ObjectMapper();
    static{
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }
    
    protected MQSenderDefaultImpl(RabbitTemplate template) {
        this.template=template;
    }

    @Override
    public void send(String key, Object msgObj) {
        try {
            template.convertAndSend(key, toJsonString(msgObj));
        } catch (Exception e) {
            log.error("消息发送失败：key【"+key+'】',e);
        }
    }
    
    /**
     * 将对象转换为json串
     * null值不进行转换
     * @param obj
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
     public static String toJsonString(Object obj) throws JsonGenerationException, JsonMappingException, IOException{
         StringWriter sw = new StringWriter();
         objectMapper.writeValue(sw, obj);
         return sw.toString();
     }

}


