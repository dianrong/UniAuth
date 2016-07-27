package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.switches.Switch;

public class UniauthSwitchs {
	
	@Switch(desc="是否使用新的登陆sql",priority="P2")
	public static boolean useNewLoginSql = false;
	
	@Switch(desc="是否使用缓存（目前CommonService使用）",priority="P2")
	public static boolean useCache = false;

}
