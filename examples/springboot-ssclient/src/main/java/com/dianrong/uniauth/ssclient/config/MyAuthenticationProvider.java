package com.dianrong.uniauth.ssclient.config;

import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.customer.basicauth.DelegateAuthenticationProvider;

/**
 * Created by denghb on 6/16/17.
 */
public class MyAuthenticationProvider extends DelegateAuthenticationProvider {

  public MyAuthenticationProvider(UniClientFacade uniClientFacade) {
    super(uniClientFacade);
  }

}
