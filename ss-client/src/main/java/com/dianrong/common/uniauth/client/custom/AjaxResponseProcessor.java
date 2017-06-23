package com.dianrong.common.uniauth.client.custom;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义接口用于处理所有的ajax的返回结果.
 *
 * @author wanglin
 */
public interface AjaxResponseProcessor {

  /**
   * 返回Ajax的返回结果.
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param customizedRedirectFormat 返回结果的格式定义
   */
  void sendAjaxResponse(HttpServletRequest request, HttpServletResponse response,
      CustomizedRedirectFormat customizedRedirectFormat) throws IOException;
}
