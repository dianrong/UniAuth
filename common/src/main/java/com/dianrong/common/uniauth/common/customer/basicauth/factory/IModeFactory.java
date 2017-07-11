package com.dianrong.common.uniauth.common.customer.basicauth.factory;

import com.dianrong.common.uniauth.common.customer.basicauth.handler.ModeHandler;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.Mode;

/**
 * Created by denghb on 6/21/17.
 */
public interface IModeFactory {

  ModeHandler getHandlerBean(Mode mode);
}
