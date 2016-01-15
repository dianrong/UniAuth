package com.dianrong.common.uniauth.common.bean;

/**
 * Created by Arc on 14/1/16.
 */
public class Error {

    private ErrorName name;
    private String msg;

    public Error(ErrorName errorName) {
        this.name = errorName;
    }

    public Error(ErrorName errorName, String msg) {
        this.name = errorName;
        this.msg = msg;
    }

    public ErrorName getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public static Error build(ErrorName name,String msg){
        return new Error(name, msg);
    }
}
