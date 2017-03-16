package com.dianrong.common.uniauth.common.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Arc on 14/1/16.
 */
@ApiModel(value = "接口返回结果model", description = "返回结果中包含 data 和 info 两部分。如果info有值，不为空，则代表操作发生了异常")
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -7608658495072131893L;

    private static final Info internalError = Info.build(InfoName.INTERNAL_ERROR, "Internal error");
    @ApiModelProperty("每一次请求的uuid")
    private String uuid;
    /** 返回数据 */
    @ApiModelProperty("成功请求返回结果")
    private T data;
    /** 返回信息 */
    @ApiModelProperty("请求失败的异常信息")
    private List<Info> info;

    public Response() {
        super();
    }

    public Response(List<Info> info) {
        this.info = info;
    }

    public Response(Info info) {
        this.info = Arrays.asList(info);
    }

    public Response(T result) {
        this.data = result;
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
