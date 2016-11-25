package com.dianrong.common.uniauth.server.mq;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * TODO
 * </pre>
 * 
 * @author cwl
 * @created Apr 14, 2016
 */
@Service("mqSenderFactory")
public class MQSenderFactory {
	private static final Logger logger = Logger.getLogger(MQSenderFactory.class);

	@Value("#{uniauthConfig['rabbit.switch']}")
	private String mq_switch;
	
    @Autowired
    @Qualifier("uniauth_mq_template")
    private RabbitTemplate template;
    
    @Autowired
    @Qualifier("uniauth_admin")
    private RabbitAdmin admin;
    
    @Value("#{uniauthConfig['rabbit.exchange']}")
    private String exchangeName;

	/**
	 * 根据template获取消息发送服务
	 * 
	 * @param template
	 * @return
	 */
	public MQSender getSender() {
		if (isOn()) {
			return MQSenderDefaultImpl.build(template, admin, exchangeName);
		}
		logger.warn("rabbit_mq服务并没有开启，hit：#{uniauthConfig['rabbit.switch']}==on.......");
		return MQSenderNotWorkImpl.getInstance();
	}

	/**.
	 * 判断是否开启了rabbitmq 发送消息的功能
	 * @return true or false
	 */
	private boolean isOn() {
		if ("on".equalsIgnoreCase(mq_switch)) {
			return true;
		}
		return false;
	}
}