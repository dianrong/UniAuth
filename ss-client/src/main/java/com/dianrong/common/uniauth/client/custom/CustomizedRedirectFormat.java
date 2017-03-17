package com.dianrong.common.uniauth.client.custom;

import javax.servlet.http.HttpServletRequest;

public interface CustomizedRedirectFormat {

    public Object getRedirectInfo(HttpServletRequest request, String loginUrl);

}
