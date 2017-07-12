package com.dianrong.common.uniauth.cas.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 定义I18n所有支持的语言.
 *
 * @author wanglin
 */
@Slf4j
public final class I18nLanguageConstantUtil {

  private I18nLanguageConstantUtil() {
    super();
  }

  /**
   * I18n menu配置文件路径.
   */
  private static final String MENU_PATH = "menus.properties";

  private static final Map<String, String> LOCAL_MAP;

  // INIT
  static {
    try {
      LOCAL_MAP = new LinkedHashMap<>(FileUtil.loadProperties(MENU_PATH));
    } catch (Exception ex) {
      log.error("failed load i18n menues from filepath : " + MENU_PATH
          + ", please check whether it is exsists." + ex);
      throw ex;
    }
  }

  /**
   * 辅助方法 返回当前系统支持的国际化语言.
   */
  public static List<I18nContent> getAllI18nLanguages() {
    List<I18nContent> data = new ArrayList<>();
    for (Map.Entry<String, String> entry : LOCAL_MAP.entrySet()) {
      String localeStr = entry.getKey();
      data.add(new I18nContent(localeStr, LOCAL_MAP.get(localeStr),
          UniBundleUtil.isSelected(localeStr)));
    }
    return data;
  }

  @ToString
  public static class I18nContent {

    private String localeStr;
    private String language;
    private boolean isSelected;

    /**
     * 构造一个I18nContent.
     */
    public I18nContent(String localeStr, String language, boolean isSelected) {
      this.localeStr = localeStr;
      this.language = language;
      this.isSelected = isSelected;
    }

    public String getLocaleStr() {
      return localeStr;
    }

    public void setLocaleStr(String localeStr) {
      this.localeStr = localeStr;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }

    public boolean isSelected() {
      return isSelected;
    }

    public void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
    }
  }
}
