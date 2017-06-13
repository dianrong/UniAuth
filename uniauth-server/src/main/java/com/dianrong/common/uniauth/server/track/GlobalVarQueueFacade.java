package com.dianrong.common.uniauth.server.track;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.track.queue.GlobalVarQueue;
import com.dianrong.common.uniauth.server.track.queue.GlobalVarQueueV2;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 一个GlobalVarQueue的开关控制类.
 *
 * @author wanglin
 */
@Component
@Slf4j
public class GlobalVarQueueFacade {

  @Autowired
  private GlobalVarQueue globalVarQueue;

  @Autowired
  private GlobalVarQueueV2 globalVarQueueV2;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  /**
   * 添加上下文信息到队列.
   */
  public void add(GlobalVar gv) {
    if (userOldGlobalVarQueueImpl()) {
      log.info("use old GlobalVarQueue implement.");
      globalVarQueue.add(gv);
    } else {
      globalVarQueueV2.add(gv);
    }
  }

  /**
   * 开关控制, 是否走旧的实现. 默认采用新的实现
   *
   * @return true 走旧的实现
   */
  private boolean userOldGlobalVarQueueImpl() {
    // 每次都从zk的本地缓存中获取,达到不重启系统开启能起作用的地步
    return "true"
        .equalsIgnoreCase(allZkNodeMap.get(AppConstants.UNIAUTH_GLOBAL_VAR_QUEUE_USE_OLD_IMPL));
  }
}
