package com.dianrong.common.uniauth.common.server;

import com.dianrong.common.uniauth.common.cons.AppConstants;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Deprecated
public final class UniauthCxfClientLocaleFilter implements ClientRequestFilter {

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    requestContext.getStringHeaders().add(AppConstants.CAS_CXF_HEADER_LOCALE_KEY,
        UniauthLocaleInfoHolder.getLocale().toString());
  }
}
