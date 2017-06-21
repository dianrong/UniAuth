package com.dianrong.common.uniauth.common.server;

import com.dianrong.common.uniauth.common.bean.LangDto;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * 处理Uniauth的国际化信息.
 * 
 * @author dreamlee
 */
@Slf4j
public class UniauthResourceService implements InitializingBean {

  private static final String BASE_PATH = "META-INF/resources/";

  private static final ResourceBundle.Control CONTROL = new UniauthResourceControl();

  private List<LangDto> menuCache = Lists.newArrayList();

  /**
   * 应用名称，以语言包形式加载时必须设置（此配置与path互斥）.
   */
  private String appName;

  /**
   * 资源文件路径，普通加载时必须设置（此配置与appName互斥）.
   */
  private String path;


  /**
   * 菜单配置文件的路径(语言包方式加载不需要设置).
   */
  private String menuPath;

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setMenuPath(String menuPath) {
    this.menuPath = menuPath;
  }

  /**
   * 加载配置信息.
   */
  public Map<String, String> getProperties(Locale locale) {
    ResourceBundle bundle = null;
    if (StringUtils.isBlank(appName)) {
      bundle = ResourceBundle.getBundle(path, locale);
    } else {
      bundle = ResourceBundle.getBundle(path, locale, CONTROL);
    }
    if (bundle != null) {
      Map<String, String> p = Maps.newHashMap();
      Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        p.put(key, bundle.getString(key));
      }
      return p;
    }
    return Maps.newHashMap();
  }

  /**
   * 获取支持的国际化语言列表.
   */
  public List<LangDto> getLanguageList() {
    try {
      if (menuCache.isEmpty()) {
        synchronized (menuCache) {
          if (menuCache.isEmpty()) {
            if (StringUtils.isBlank(menuPath)) {
              loadFromSpi();
            } else {
              loadFromLocal();
            }
          }
        }
      }
    } catch (IOException e) {
      log.error("getLanguageList error", e);
    }
    return menuCache;
  }

  /**
   * 加载本地的菜单配置.
   */
  private void loadFromLocal() throws IOException {
    InputStream is = UniauthResourceService.class.getClassLoader().getResourceAsStream(menuPath);
    if (is != null) {
      try {
        Properties p = new Properties();
        p.load(is);
        for (Object key : p.keySet()) {
          menuCache.add(new LangDto(String.valueOf(key), p.getProperty(String.valueOf(key))));
        }
      } finally {
        is.close();
      }
    }
  }

  /**
   * 从语言包中加载菜单配置.
   */
  private void loadFromSpi() throws IOException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Enumeration<URL> resources = loader.getResources(BASE_PATH + "menu.properties");
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      InputStream is = url.openStream();
      try {
        Properties p = new Properties();
        p.load(is);
        for (Object key : p.keySet()) {
          menuCache.add(new LangDto(String.valueOf(key), p.getProperty(String.valueOf(key))));
        }
      } finally {
        is.close();
      }
    }
  }

  /**
   * 初始化.
   */
  public void init() {
    try {
      afterPropertiesSet();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (appName == null && path == null || appName != null && path != null) {
      throw new UniauthCommonException("param error!");
    }
  }

}
