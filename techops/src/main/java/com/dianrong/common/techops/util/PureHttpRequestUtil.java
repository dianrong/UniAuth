package com.dianrong.common.techops.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Arc on 31/8/2017.
 */
public class PureHttpRequestUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(PureHttpRequestUtil.class);

  public static String sendGet(String url, String params, Map<String, String> requestHeaders) {
    String result = "";
    BufferedReader in = null;
    try {
      String urlName = url + "?" + params;
      URL realUrl;
      realUrl = new URL(urlName);
      URLConnection conn = realUrl.openConnection();
      conn.setConnectTimeout(1000);
      conn.setReadTimeout(50 * 1000);

      if (requestHeaders != null && !requestHeaders.isEmpty()) {
        Set<String> keys = requestHeaders.keySet();
        for (String key : keys) {
          conn.setRequestProperty(key, requestHeaders.get(key));
        }
      }

//      conn.setRequestProperty("connection", "Keep-Alive");
//      conn.setRequestProperty("accept", "*/*");
//      conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible;MSIE)");

      conn.connect();

      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += "\n" + line;
      }
    } catch (Exception e) {
      LOGGER.error("Send get exception...", e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          LOGGER.error("IOException...", e);
        }
      }
    }
    return result;
  }

  public static String sendPost(String url, String params, Map<String, String> requestHeaders) {
    PrintWriter out = null;
    BufferedReader br = null;
    String result = "";
    try {
      URL realURL = new URL(url);
      URLConnection conn = realURL.openConnection();
      conn.setConnectTimeout(1000);
      conn.setReadTimeout(50 * 1000);

      if (requestHeaders != null && !requestHeaders.isEmpty()) {
        Set<String> keys = requestHeaders.keySet();
        for (String key : keys) {
          conn.setRequestProperty(key, requestHeaders.get(key));
        }
      }

//      conn.setRequestProperty("connection", "Keep-Alive");
//      conn.setRequestProperty("accept", "*/*");
//      conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible;MSIE)");
//      conn.setRequestProperty("content-type", "application/json");

      conn.setDoOutput(true);
      conn.setDoInput(true);

      //获取URLConnection对象对应的输入流
      out = new PrintWriter(conn.getOutputStream());
      //发送请求参数
      out.print(params);
      out.flush();
      br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = br.readLine()) != null)
      {
        result += "\n" + line;
      }
    } catch (Exception e) {
      LOGGER.error("Send post exception...", e);
    }
    finally{
      try {
        if (br != null) {
          br.close();
        }
        if ( out != null) {
          out.close();
        }
      } catch (IOException e) {
        LOGGER.error("IOException...", e);
      }
    }
    return result;

  }

}
