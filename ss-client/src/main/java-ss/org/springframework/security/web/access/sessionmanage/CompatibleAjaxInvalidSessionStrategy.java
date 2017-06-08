package org.springframework.security.web.access.sessionmanage;

import com.dianrong.common.uniauth.client.custom.AjaxResponseProcessor;
import com.dianrong.common.uniauth.client.custom.CustomizedRedirectFormat;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.InvalidSessionStrategy;

/**
 * 兼容Ajax请求的InvalidSessionStrategey实现.
 *
 * @author wanglin
 */
public class CompatibleAjaxInvalidSessionStrategy implements InvalidSessionStrategy {

  /**
   * 原始InvalidSessionStrategy.
   */
  private InvalidSessionStrategy originalInvalidSessionStrategy;

  /**
   * 希望在Ajax请求的时候被该InvalidSessionStrategy忽略的InvalidSessionStrategy.
   */
  private Set<Class<? extends InvalidSessionStrategy>> ignoreWhenAjaxRequest;

  private CustomizedRedirectFormat customizedRedirectFormat;

  public CompatibleAjaxInvalidSessionStrategy setCustomizedRedirectFormat(
      CustomizedRedirectFormat customizedRedirectFormat) {
    this.customizedRedirectFormat = customizedRedirectFormat;
    return this;
  }

  public void setAjaxResponseProcessor(AjaxResponseProcessor ajaxResponseProcessor) {
    Assert.notNull(ajaxResponseProcessor);
    this.ajaxResponseProcessor = ajaxResponseProcessor;
  }

  /**
   * 当Ajax请求的时候处理其返回值.
   */
  private AjaxResponseProcessor ajaxResponseProcessor;

  /**
   * 构造函数.
   */
  @SafeVarargs
  public CompatibleAjaxInvalidSessionStrategy(InvalidSessionStrategy originalInvalidSessionStrategy,
      AjaxResponseProcessor ajaxResponseProcessor,
      Class<? extends InvalidSessionStrategy>... ignoreWhenAjaxRequest) {
    Assert.notNull(ajaxResponseProcessor);
    this.originalInvalidSessionStrategy = originalInvalidSessionStrategy;
    this.ajaxResponseProcessor = ajaxResponseProcessor;
    this.ignoreWhenAjaxRequest = new HashSet<Class<? extends InvalidSessionStrategy>>(
        Arrays.asList(ignoreWhenAjaxRequest));
  }

  @Override
  public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // ajax 请求
    if (HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCorsRequest(request)) {
      this.ajaxResponseProcessor.sendAjaxResponse(request, response, customizedRedirectFormat);
      if (this.originalInvalidSessionStrategy != null) {
        // 忽略某些不需要处理的InvalidSessionStrategy
        if (!ignoreWhenAjaxRequest.contains(this.originalInvalidSessionStrategy.getClass())) {
          this.originalInvalidSessionStrategy.onInvalidSessionDetected(request, response);
        }
      }
    } else {
      if (this.originalInvalidSessionStrategy != null) {
        this.originalInvalidSessionStrategy.onInvalidSessionDetected(request, response);
      }
    }
  }
}
