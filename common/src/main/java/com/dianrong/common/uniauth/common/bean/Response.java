package com.dianrong.common.uniauth.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arc on 14/1/16.
 */
@ApiModel(value = "接口返回结果model", description = "返回结果中包含 data 和 info 两部分。如果info有值，不为空，则代表操作发生了异常")
public class Response<T> implements Serializable {

  /**
   * 返回结果常量-成功.
   */
  public static final String SUCCESS = "success";

  /**
   * 返回结果常量-失败.
   */
  public static final String FAILURE = "error";

  private static final long serialVersionUID = -7608658495072131893L;

  private static final Info internalError = Info.build(InfoName.INTERNAL_ERROR, "Internal error");

  @ApiModelProperty("请求结果:success或error")
  private String result;

  @ApiModelProperty("每一次请求的uuid")
  private String uuid;

  @ApiModelProperty("成功请求返回结果")
  private T data;

  @ApiModelProperty("请求失败的异常信息")
  private List<Info> info;

  public Response() {
    this(null, null);
  }

  public Response(List<Info> info) {
    this(null, info);
  }

  public Response(Info info) {
    this(null, Arrays.asList(info));
  }

  public Response(T result) {
    this(result, null);
  }

  /**
   * 构造请求返回结果.
   *
   * @param result 请求结果
   * @param info 错误信息列表
   */
  public Response(T result, List<Info> info) {
    super();
    this.data = result;
    this.info = info;
    if (info == null || info.isEmpty()) {
      this.result = SUCCESS;
    } else {
      this.result = FAILURE;
    }
  }

  public T getData() {
    return data;
  }

  public Response<T> setData(T data) {
    this.data = data;
    return this;
  }

  public List<Info> getInfo() {
    return info;
  }

  public Response<T> setInfo(List<Info> info) {
    this.info = info;
    return this;
  }

  public String getUuid() {
    return uuid;
  }

  public Response<T> setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public String getResult() {
    return result;
  }

  public Response<T> setResult(String result) {
    this.result = result;
    return this;
  }

  public static <T> Response<T> success(T result) {
    return new Response<>(result);
  }

  public static Response<Void> success() {
    return new Response<>();
  }

  public static <T> Response<T> failure(Info info) {
    return new Response<>(info);
  }

  public static <T> Response<T> failure(List<Info> info) {
    return new Response<>(info);
  }

  public static <T> Response<T> failure() {
    return new Response<>(internalError);
  }

  public static <T> Response<T> failure(InfoName name, String errMsg) {
    return failure(Info.build(name, errMsg));
  }
}
