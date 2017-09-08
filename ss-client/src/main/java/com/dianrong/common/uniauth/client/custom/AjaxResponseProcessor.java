package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.client.custom.redirect.UniauthRedirectFormat;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Ajax请求跳转处理.
 *
 * @author wanglin
 */
public interface AjaxResponseProcessor {

  /**
   * 返回Ajax的返回结果.
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param redirectFormat 返回结果的格式定义
   */
  void sendAjaxResponse(HttpServletRequest request, HttpServletResponse response,
      UniauthRedirectFormat redirectFormat) throws IOException;
}
