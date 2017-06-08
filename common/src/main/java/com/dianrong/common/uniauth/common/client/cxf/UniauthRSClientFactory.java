package com.dianrong.common.uniauth.common.client.cxf;

import java.util.Arrays;
import java.util.List;
import org.apache.cxf.common.util.ProxyHelper;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

/**
 * 用于创建接口的代理对象.
 *
 * @author wanglin
 */
public final class UniauthRSClientFactory {

  /**
   * API  request filter.
   */
  private static final ApiCtlRequestFilter API_CTL_REQ_FILTER = new ApiCtlRequestFilter();

  /**
   * API response filter.
   */
  private static final ApiCtlResponseFilter API_CTL_RES_FILTER = new ApiCtlResponseFilter();

  /**
   * Create代理对象.
   */
  @SuppressWarnings("unchecked")
  public static <T> T create(String baseAddress, Class<T> cls, List<?> providers) {
    // default
    Object[] providersArray = {};
    if (providers != null) {
      providersArray = providers.toArray();
    }

    // append two provider
    int tempArrayLength = providersArray.length;
    providersArray = Arrays.copyOf(providersArray, tempArrayLength + 2);
    providersArray[tempArrayLength] = API_CTL_REQ_FILTER;
    providersArray[tempArrayLength + 1] = API_CTL_RES_FILTER;
    List<?> cxfProviders = Arrays.asList(providersArray);
    T proxy = JAXRSClientFactory.create(baseAddress, cls, cxfProviders);
    ClassLoader loader = UniauthRSClientFactory.class.getClassLoader();
    ApiControlInvocationHandler handler = new ApiControlInvocationHandler(proxy);
    return (T) ProxyHelper.getProxy(loader, new Class<?>[]{cls, Client.class}, handler);
  }
}
