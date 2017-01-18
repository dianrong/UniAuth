package com.dianrong.common.uniauth.common.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Arc on 14/1/16.
 */
public class Response<T> {
    private static final Info internalError = Info.build(InfoName.INTERNAL_ERROR, "Internal error");
    private String uuid;
    /** 返回数据 */
    private T data;
    /** 返回信息 */
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
