package com.dianrong.common.uniauth.common.customer.basicauth.factory;

import com.dianrong.common.uniauth.common.customer.basicauth.handler.ModeHandler;
import com.dianrong.common.uniauth.common.customer.basicauth.handler.PermissionHandler;
import com.dianrong.common.uniauth.common.customer.basicauth.handler.PermissionTypeHandler;
import com.dianrong.common.uniauth.common.customer.basicauth.handler.RoleCodeHandler;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.Mode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by denghb on 6/21/17.
 */
public class ModeFactory implements IModeFactory {

  private static Map<Mode, ModeHandler> handlerMap = new HashMap<>();

  static {
    handlerMap.put(Mode.ROLE_CODE, new RoleCodeHandler());
    handlerMap.put(Mode.PERMISSION, new PermissionHandler());
    handlerMap.put(Mode.PERMISSION_TYPE, new PermissionTypeHandler());
  }

  @Override
  public ModeHandler getHandlerBean(Mode mode) {
    return handlerMap.get(mode);
  }

}
