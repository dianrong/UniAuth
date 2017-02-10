package com.dianrong.platform.open.cfg;

/**
 * Created by klose on 16-7-27.
 *
 */
public interface CfgEventListener {

  void onEvent(CfgEvent event, String key, String value);
}
