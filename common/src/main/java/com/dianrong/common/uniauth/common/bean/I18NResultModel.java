package com.dianrong.common.uniauth.common.bean;

import java.util.List;

public class I18NResultModel {

  private List<LangDto> supportLanguages;

  private String current;

  /**
   * 构造函数.
   */
  public I18NResultModel(List<LangDto> supportLanguages, String current) {
    super();
    this.supportLanguages = supportLanguages;
    this.current = current;
  }

  public List<LangDto> getSupportLanguages() {
    return supportLanguages;
  }

  public I18NResultModel supportLanguages(List<LangDto> supportLanguages) {
    this.supportLanguages = supportLanguages;
    return this;
  }

  public String getCurrent() {
    return current;
  }

  public I18NResultModel current(String current) {
    this.current = current;
    return this;
  }

  public static I18NResultModel of(String current) {
    return new I18NResultModel(null, current);
  }

  public static I18NResultModel of(List<LangDto> supportLangs) {
    return new I18NResultModel(supportLangs, null);
  }

}
