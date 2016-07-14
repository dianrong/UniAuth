package com.dianrong.common.uniauth.server.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import com.google.common.collect.Maps;

public class UniBundle {
	
	public static String getMsg(String key){
		return ResourceBundlueHolder.getResource().getString(key);
	}
	
    public static String getMsg(String key, Object... arguments) {
        String raw = ResourceBundlueHolder.getResource().getString(key);
        String result = MessageFormat.format(raw, arguments);
        return result;
    }
	
	public static void main(String[] args) {
		System.out.println(getMsg("common.parameter.empty", "姓名"));
	}
	
	
	
	private static class ResourceBundlueHolder{
		private static Map<Locale,ResourceBundle> RESOURCEBUNDLES = Maps.newConcurrentMap();
		
		
		public static ResourceBundle getResource(){
			Locale locale = UniauthLocaleInfoHolder.getLocale();
			ResourceBundle resourceBundle = RESOURCEBUNDLES.get(locale);
			if(resourceBundle == null){
				RESOURCEBUNDLES.putIfAbsent(locale, ResourceBundle.getBundle("UniauthResource", locale));
			}
			
			return RESOURCEBUNDLES.get(locale);
		}
	}
}
