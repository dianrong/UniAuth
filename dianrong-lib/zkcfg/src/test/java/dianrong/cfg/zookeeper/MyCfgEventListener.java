package dianrong.cfg.zookeeper;

import com.dianrong.platform.cfg.CfgEvent;
import com.dianrong.platform.cfg.CfgEventListener;

/**
 * Created by klose on 16-7-27.
 *
 */
public class MyCfgEventListener implements CfgEventListener {
  @Override
  public void onEvent(CfgEvent event, String key, String value) {
    System.err.println("my cfg event listener value = "+ value);
  }
}
