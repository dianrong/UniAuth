package com.dianrong.common.uniauth.common.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arc on 14/1/16.
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 4391955854748049770L;
    private static Error errorOK = new Error(ErrorName.OK);
    private static Error internalError = Error.build(ErrorName.INTERNAL_ERROR, "Internal error");
    private static List<Error> okErrorList = Arrays.asList(errorOK);
    private String uuid;
    /** 数据记录 */
    private T result;
    /** 错误信息 */
    private List<Error> errors;

    public Response() {
    }

    public Response(List<Error> errors) {
        this.errors = errors;
    }

    public Response(Error error) {
        this.errors = Arrays.asList(error);
    }

    public Response(T result, List<Error> errors) {
        this.result = result;
        this.errors = errors;
    }

    public Response(T result, Error error) {
        this.result = result;
        this.errors = Arrays.asList(error);
    }

    public T getResult() {
        return result;
    }

    public Response<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Response setErrors(List<Error> errors) {
        this.errors = errors;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Response setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public static <T> Response<T> success(T result) {
        return new Response<T>(result, okErrorList);
    }

    public static <T> Response<T> success() {
        return new Response<T>(okErrorList);
    }

    public static <T> Response<T> success(T result, Error error) {
        return new Response<T>(result, error);
    }

    public static <T> Response<T> failure(Error error) {
        return new Response<T>(error);
    }

    public static <T> Response<T> failure(List<Error> errors) {
        return new Response<T>(errors);
    }

    public static <T> Response<T> failure() {
        return new Response<T>(internalError);
    }

    public static <T> Response<T> failure(ErrorName name, String errMsg) {
        return failure(Error.build(name, errMsg));
    }

}
