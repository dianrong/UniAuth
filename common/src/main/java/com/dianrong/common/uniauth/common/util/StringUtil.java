package com.dianrong.common.uniauth.common.util;

import java.util.Random;

/**.
 * some functions process String
 * @author R9GBP97
 *
 */
public class StringUtil {
	
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
}
