package com.dianrong.common.uniauth.server.synchronous.hr.bean;

/**
 * 加载内容.
 */
public class LoadContent<T> {

  private T content;

  private String sourceName;

  public LoadContent() {}

  public LoadContent(T content, String sourceName) {
    this.content = content;
    this.sourceName = sourceName;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }
}
