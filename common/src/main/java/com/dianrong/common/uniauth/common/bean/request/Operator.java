package com.dianrong.common.uniauth.common.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by Arc on 14/1/16.
 */
public class Operator {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    protected String email;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    protected String phone;
}
