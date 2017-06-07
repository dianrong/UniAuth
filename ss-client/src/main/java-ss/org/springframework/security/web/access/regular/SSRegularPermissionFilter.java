package org.springframework.security.web.access.regular;

import com.dianrong.common.uniauth.client.custom.LoginUserInfoHolder;
import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.exp.UserNotLoginException;
import com.dianrong.common.uniauth.client.support.ExtractRequestUrl;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

/**
 * uniauth中的regular权限的处理filter,必须处理登陆成功的情况
 *
 * @author wanglin
 */
@Slf4j
public class SSRegularPermissionFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain chain) throws IOException, ServletException {
    long start = System.nanoTime();
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    boolean checkPass = false;
    try {
      UserExtInfo loginUser = LoginUserInfoHolder.getLoginUserInfo();
      Set<SSRegularPattern> regularPatterns = loginUser.getAllPermittedRegularPattern();
      // no any permission for requesting
      if (regularPatterns.isEmpty()) {
        return;
      }
      String url = ExtractRequestUrl.extractRequestUrl(request, false);
      String requetMethod = request.getMethod();
      for (SSRegularPattern p : regularPatterns) {
        if (p.permissonCheck(requetMethod, url)) {
          checkPass = true;
          break;
        }
      }
    } catch (UserNotLoginException ex) {
      log.debug("not login", ex);
      checkPass = true;
    } finally {
      try {
        if (checkPass) {
          chain.doFilter(request, response);
        } else {
          unPermittedRequest(response);
        }
      } finally {
        log.debug(this.getClass().getName() + " consume times : " + (System.nanoTime() - start));
      }
    }
  }

  /**
   * .
   * process not permitted request, response code 403
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   */
  private void unPermittedRequest(HttpServletResponse response) {
    try {
      response.setStatus(org.apache.http.HttpStatus.SC_FORBIDDEN);
      response.getWriter().write(JsonUtil.object2Jason(
          new ForbiddenResponseMsg(AppConstants.NO_PRIVILEGE,
              "Sorry! You do not have permission to access the resource!")));
    } catch (IOException ex) {
      log.warn("failed to send unpermitted warn", ex);
    }
  }

  // inner class for unPermittedRequest reponse
  class ForbiddenResponseMsg {

    private final List<Info> info;

    public ForbiddenResponseMsg(String name, String msg) {
      info = new ArrayList<>();
      info.add(new Info(name, msg));
    }

    public void addInfo(String name, String msg) {
      this.info.add(new Info(name, msg));
    }

    public List<Info> getInfo() {
      return info;
    }

    class Info {

      private String name;
      private String msg;

      public Info(String name, String msg) {
        this.name = name;
        this.msg = msg;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public String getMsg() {
        return msg;
      }

      public void setMsg(String msg) {
        this.msg = msg;
      }
    }
  }
}
