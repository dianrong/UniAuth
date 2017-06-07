package com.dianrong.common.uniauth.common.server;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.springframework.util.StringUtils;

/**
 * . uniauth的cxf的locale的filter实现
 *
 * @author wanglin
 */
@Provider
@Deprecated
public final class UniauthCxfServerLocaleFillter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String localeStr = requestContext.getHeaderString(AppConstants.CAS_CXF_HEADER_LOCALE_KEY);
    if (localeStr != null) {
      UniauthLocaleInfoHolder.setLocale(StringUtils.parseLocaleString(localeStr));
    }
  }
}
