package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

/**
 * 定义一个标准的http response返回model的格式.
 *
 * @author wanglin
 */
public class HttpResponseModel<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = 788863139426941179L;

  private boolean success;

  private Integer code;

  private T content;

  private String msg;

  public boolean isSuccess() {
    return success;
  }

  public HttpResponseModel<T> setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  public Integer getCode() {
    return code;
  }

  public HttpResponseModel<T> setCode(Integer code) {
    this.code = code;
    return this;
  }

  public T getContent() {
    return content;
  }

  public HttpResponseModel<T> setContent(T content) {
    this.content = content;
    return this;
  }

  public String getMsg() {
    return msg;
  }

  public HttpResponseModel<T> setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public static <T extends Serializable> HttpResponseModel<T> buildSuccessResponse() {
    return new HttpResponseModel<T>().setSuccess(true);
  }
}
