package com.dianrong.common.uniauth.server.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class UniBundle {
	private static ResourceBundle rb =  ResourceBundle.getBundle("UniauthResource", Locale.getDefault());
	
	public static String getMsg(String key){
		return rb.getString(key);
	}
	
    public static String getMsg(String key, Object... arguments) {
        String raw = rb.getString(key);
        String result = MessageFormat.format(raw, arguments);
        return result;
    }
	
	public static void main(String[] args) {
		System.out.println(getMsg("common.parameter.empty", "姓名"));
	}
}
