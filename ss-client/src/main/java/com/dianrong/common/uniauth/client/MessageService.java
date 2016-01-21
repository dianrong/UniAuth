package com.dianrong.common.uniauth.client;

public class MessageService {
 
    public String getMessage() {
        return "Hello World!";
    }
    
    public static void main(String[] args) {
		System.setProperty("DR_CFG_ZOOKEEPER_ENV_URL", "127.0.0.1:2181");
	}
}
