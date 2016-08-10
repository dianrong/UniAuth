package com.dianrong.common.uniauth.common.switches;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class SwitchInit implements InitializingBean {
	
	private static final Log log = LogFactory.getLog(SwitchInit.class);
	
	private String appName;
	
	private List<String> switchs;

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setSwitchs(List<String> switchs) {
		this.switchs = switchs;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(switchs!=null && switchs.size() >0){
			SwitchRegistry.init();
			for(String s : switchs){
				try{
					SwitchRegistry.register(appName, Class.forName(s));
				}catch(Exception e){
					log.error("init "+s+"eror", e);
				}
			}
		}
	}

}
