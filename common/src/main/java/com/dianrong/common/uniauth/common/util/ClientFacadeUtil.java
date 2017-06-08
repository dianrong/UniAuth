package com.dianrong.common.uniauth.common.util;

import org.apache.cxf.jaxrs.client.WebClient;

/**
 * Created by Arc on 16/5/2016.
 */
public class ClientFacadeUtil {

  /**
   * 添加Api Key.
   */
  public static void addApiKey(String apiName, String apiKey, Object... objs) {
    if (objs != null && apiName != null && apiKey != null) {
      for (Object obj : objs) {
        WebClient.client(obj).header(apiName, apiKey);
      }
    }
  }
}
