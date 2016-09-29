package com.dianrong.common.uniauth.cas.extend.filter;

public class TenancyCodeHolder {
	private static ThreadLocal<String> tenancyCodes = new ThreadLocal<String>();
	
	public static String get() {
		return tenancyCodes.get();
	}
	
	public static void set(String newTenancyCode) {
		tenancyCodes.set(newTenancyCode);
	}
}
