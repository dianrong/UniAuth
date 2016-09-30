package com.dianrong.common.uniauth.client.custom;

import org.springframework.security.core.context.SecurityContextHolder;

import com.dianrong.common.uniauth.client.exp.UserNotLoginException;

/**
 * . 提供便利的方式供业务系统获取当前登陆的用户
 * 
 * @author wanglin
 */
public final class LoginUserInfoHolder {

	/**.
	 * 获取当前登陆用户的信息
	 * @return  当前登陆用户对象
	 * @throws UserNotLoginException
	 */
	public static UserExtInfo getLoginUserInfo() throws UserNotLoginException{
		UserExtInfo userExtInfo = (UserExtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userExtInfo == null) {
			throw new UserNotLoginException("there is not user is logined");
		}
		return userExtInfo;
	}

	/**.
	 * 获取当前登陆用户的租户id
	 * @return
	 */
	public static long getCurrentLoginUserTenancyId() {
		return getLoginUserInfo().getUserDto().getTenancyId();
	}
}
