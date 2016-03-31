package com.dianrong.common.uniauth.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {
	
	private static final Pattern JASON_PARAM_WITH_PWD = Pattern.compile(".*\"(password|originPassword)\":\"(.*?)\".*");
	
	private RegExpUtil(){
		
	}
	
	public static String purgePassword(String jasonParam){
		if(jasonParam != null){
			Matcher m = JASON_PARAM_WITH_PWD.matcher(jasonParam);
			if(m.find()){
				String password = m.group(2);
				if(password != null && !"".equals(password.trim())){
					jasonParam = jasonParam.replace(password, "******");
				}
			}
		}
		return jasonParam;
	}
	
	public static void main(String[] args) {
		String s =  "{\"account\":\"shuanggui.fan@dianrong.com\",\"originPassword\":\"1234\",\"ip\":\"0:0:0:0:0:0:0:1\",\"password\":\"1234\"}";
		System.out.println(purgePassword(s));
	}
}
