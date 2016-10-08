package com.dianrong.common.uniauth.common.util;

import java.util.Random;

import org.apache.log4j.Logger;

/**.
 * some functions process String
 * @author R9GBP97
 *
 */
public class StringUtil {
	private static final Logger logger = Logger.getLogger(StringUtil.class);
	
	/**.
	 * judge str is null or empty
	 * @param str str
	 * @return boolean
	 */
	public static boolean strIsNullOrEmpty(final String str){
		if(str == null || "".equals(str)){
			return true;
		}
		
		return false;
	}
	
	/**.
	 * 获取对象的字符串表示方式
	 * @param obj 对象
	 * @return 字符串结果
	 */
	public static String getObjectStr(Object obj){
		if(obj == null){
			return "";
		}
		return obj.toString();
	}
	
	/**.
	 * 获取对象的字符串表示方式
	 * @param obj 对象
	 * @return 字符串结果
	 */
	public static Integer tryToTranslateStrToInt(String str){
		if(str == null){
			return null;
		}
		try {
		return Integer.parseInt(str);
		} catch(NumberFormatException e) {
			logger.warn(str + "is a invalid number format string", e);
		}
		return null;
	}
	
	/**.
	 * 生成目标长度的数字字符串
	 * @param length 
	 * @return
	 */
	public static String generateNumberStr(int length){
		if(length <= 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Random r = new Random(System.currentTimeMillis());
		for(int i = 0 ; i < length ; i++){
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}
	
	/**.
	 * 获取异常信息的第一行简单信息 
	 * @param detailMessage detailMessage
	 * @return 返回信息
	 */
	public static String getExceptionSimpleMessage(String detailMessage){
		if(strIsNullOrEmpty(detailMessage)){
			return "";
		}
		int firstIndex = detailMessage.indexOf('\r');
		int tfirstIndex = detailMessage.indexOf('\n');
		firstIndex = firstIndex < tfirstIndex ? (firstIndex > 0 ? firstIndex :tfirstIndex) :tfirstIndex;
		if(firstIndex < 0){
			return detailMessage;
		}
		return detailMessage.substring(0, firstIndex);
	}
}
