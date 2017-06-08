package com.dianrong.common.uniauth.common.apicontrol.client;

import com.dianrong.common.uniauth.common.apicontrol.ResponseVerifiedType;

/**
 * 调用结果处理.
 *
 * @author wanglin
 */
public interface InvokeResultHandler {

  void handle(ResponseVerifiedType type, String content);

}
