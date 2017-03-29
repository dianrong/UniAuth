package com.dianrong.common.uniauth.server.support.apicontrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianrong.common.uniauth.common.apicontrol.StringHeaderValueOperator;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 用于协议header的get 和 set
 * 
 * @author wanglin
 */
public class UniauthServerHeaderOperator implements StringHeaderValueOperator {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    public UniauthServerHeaderOperator(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request);
        Assert.notNull(response);
        this.request = request;
        this.response = response;
    }

    @Override
    public String getHeader(String key) {
        return request.getHeader(key);
    }

    @Override
    public void setHeader(String key, String value) {
        if (key != null && value != null) {
            response.setHeader(key, value);
        }
    }
}
