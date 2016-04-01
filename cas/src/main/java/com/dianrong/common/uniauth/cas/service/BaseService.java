package com.dianrong.common.uniauth.cas.service;

import java.util.List;

import com.dianrong.common.uniauth.cas.exp.ResetPasswordException;
import com.dianrong.common.uniauth.common.bean.Info;

/**.
 * 基础类,提供远程调用的公用方法
 * @author wanglin
 */
public abstract class BaseService {
	/**.
	 * 坚持异常
	 * @param infoList infoList
	 * @throws ResetPasswordException 异常
	 */
	protected void checkInfoList(List<Info> infoList) throws ResetPasswordException {
		if (infoList != null && !infoList.isEmpty()) {
			for (Info info : infoList) {
				String errorMsg = info.getMsg();
				throw new ResetPasswordException(errorMsg);
			}
		}
	}
}
