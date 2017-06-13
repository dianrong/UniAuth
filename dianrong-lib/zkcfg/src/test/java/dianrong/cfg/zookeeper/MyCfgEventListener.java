package dianrong.cfg.zookeeper;

import com.dianrong.platform.open.cfg.CfgEvent;
import com.dianrong.platform.open.cfg.CfgEventListener;

/**
 * Created by klose on 16-7-27.
 *
 */
public class MyCfgEventListener implements CfgEventListener {
  @Override
  public void onEvent(CfgEvent event, String key, String value) {
    System.err.println("my cfg event listener value = " + value);
  }
}
