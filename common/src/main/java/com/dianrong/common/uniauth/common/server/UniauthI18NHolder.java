package com.dianrong.common.uniauth.common.server;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UniauthI18NHolder implements ApplicationContextAware, InitializingBean {

  private static UniauthResourceService techOpsResource;

  private ApplicationContext context;


  /**
   * 获取国际化文案（此方法只适用于spring web环境中调用）.<br>
   * 如果不在spring web环境中请自行取到Request，然后调用{@link #getProperties(HttpServletRequest, String)}
   */
  public static String getProperties(String key) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    return getProperties(request, key);
  }


  /**
   * 获取支持的国际化语言.
   */
  public static String getProperties(HttpServletRequest request, String key) {
    if (request == null) {
      return null;
    }
    Locale lang = (Locale) request.getSession()
        .getAttribute(UniauthLocaleChangeInterceptor.SESSION_NAME);
    if (lang == null) {
      lang = Locale.getDefault();
    }
    Map<String, String> properties = techOpsResource.getProperties(lang);
    return properties == null ? null : properties.get(key);
  }

  public void setTechOpsResource(UniauthResourceService techOpsResource) {
    UniauthI18NHolder.techOpsResource = techOpsResource;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (techOpsResource == null) {
      UniauthI18NHolder.techOpsResource = context.getBean(UniauthResourceService.class);
    }
  }

}
