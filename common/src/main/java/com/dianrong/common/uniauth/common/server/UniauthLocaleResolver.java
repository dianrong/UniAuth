package com.dianrong.common.uniauth.common.server;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.LocaleResolver;

/**
 * . uniauth local resolver.
 *
 * @author wanglin
 */
public class UniauthLocaleResolver implements LocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    return UniauthLocaleInfoHolder.getLocale();
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    UniauthLocaleInfoHolder.setLocale(locale);
  }
}
