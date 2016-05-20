package com.dianrong.common.uniauth.cas.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**.
 * 国际化处理的bundle
 * @author wanglin
 */
public class UniBundle {
private static ResourceBundle rb =  ResourceBundle.getBundle("casResource", Locale.getDefault());
	
	public static String getMsg(String key){
		return rb.getString(key);
	}
	
    public static String getMsg(String key, Object... arguments) {
        String raw = rb.getString(key);
        String result = MessageFormat.format(raw, arguments);
        return result;
    }
}
