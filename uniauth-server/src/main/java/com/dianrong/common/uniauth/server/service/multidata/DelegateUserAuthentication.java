package com.dianrong.common.uniauth.server.service.multidata;

import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.exp.AuthenticationNotSupportedException;
import com.dianrong.common.uniauth.server.service.multidata.UserAuthentication;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;

/**
 * 处理多数据源的登陆实现.
 */
@Service
public class DelegateUserAuthentication
    implements UserAuthentication, ApplicationContextAware, InitializingBean {

  // 默认值
  private List<UserAuthentication> authenticationList = Collections.emptyList();

  private ApplicationContext applicationContext;

  /**
   * 用户登陆.
   */
  public UserDto login(LoginParam loginParam) {
    for (UserAuthentication da : authenticationList) {
      if (da.supported(loginParam)) {
        return da.login(loginParam);
      }
    }
    throw notSupportedException();
  }

  /**
   * 获取用户详细信息.
   */
  public UserDetailDto getUserDetailInfo(LoginParam loginParam) {
    for (UserAuthentication da : authenticationList) {
      if (da.supported(loginParam)) {
        return da.getUserDetailInfo(loginParam);
      }
    }
    throw notSupportedException();
  }

  /**
   * 根据用户的邮箱或者电话获取用户信息.
   */
  public UserDto getUserByEmailOrPhone(LoginParam loginParam) {
    for (UserAuthentication da : authenticationList) {
      if (da.supported(loginParam)) {
        return da.getUserByEmailOrPhone(loginParam);
      }
    }
    throw notSupportedException();
  }

  /**
   * 便利方法.当前的实现不支持请求的参数时,抛出的异常.
   */
  private AuthenticationNotSupportedException notSupportedException() {
    AuthenticationNotSupportedException exception = new AuthenticationNotSupportedException(
        UniBundle.getMsg("login.multiple.data.user.authentication.not.supported"));
    return exception;
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public boolean supported(LoginParam loginParam) {
    return false;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Assert.notNull(applicationContext);
    this.applicationContext = applicationContext;
  }

  /**
   * 启动获取所有的多数据源验证实现.
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    String[] multipleDataAuthenticationNames =
        this.applicationContext.getBeanNamesForType(UserAuthentication.class);
    List<UserAuthentication> tempAuthenticationList = Lists.newArrayList();
    for (String multipleDataAuthenticationName : multipleDataAuthenticationNames) {
      UserAuthentication multipleDataAuthentication =
          this.applicationContext.getBean(multipleDataAuthenticationName, UserAuthentication.class);
      // 排除facade实现
      if (multipleDataAuthentication instanceof DelegateUserAuthentication) {
        continue;
      }
      tempAuthenticationList.add(multipleDataAuthentication);
    }
    Comparator<Object> comparator = OrderComparator.INSTANCE;
    Collections.sort(tempAuthenticationList, comparator);
    this.authenticationList = Collections.unmodifiableList(tempAuthenticationList);
  }
}
