package com.dianrong.common.uniauth.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {
	
	private static final Pattern JASON_PARAM_WITH_PWD = Pattern.compile(".*\"password\":\"(.*?)\".*");
	
	private RegExpUtil(){
		
	}
	
	public static String purgePassword(String jasonParam){
		if(jasonParam != null){
			Matcher m = JASON_PARAM_WITH_PWD.matcher(jasonParam);
			if(m.find()){
				String password = m.group(1);
				jasonParam = jasonParam.replace(password, "******");
			}
		}
		return jasonParam;
	}
}
