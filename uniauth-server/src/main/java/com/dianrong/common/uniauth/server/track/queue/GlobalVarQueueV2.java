package com.dianrong.common.uniauth.server.track.queue;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Audit;
import com.dianrong.common.uniauth.server.data.mapper.AuditMapper;
import com.dianrong.common.uniauth.server.track.GlobalVar;
import com.dianrong.common.uniauth.server.util.RegExpUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("globalVarQueueV2")
@Slf4j
public class GlobalVarQueueV2 {

  @Autowired
  private AuditMapper auditMapper;

  private BlockingQueue<GlobalVar> globVarQueue =
      new ArrayBlockingQueue<GlobalVar>(AppConstants.GLOBALVAR_QUEUE_SIZE);

  /**
   * 实际插入数据库的线程池.
   */
  private final ExecutorService insertDbThreadPool = new ThreadPoolExecutor(1, 1, 0L,
      TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));

  /**
   * 记录内存中实际cache的audit的数量.
   */
  private final AtomicLong totalCacheNum = new AtomicLong(0L);

  /**
   * 缓存audit列表.
   */
  private volatile List<Audit> auditList;

  private final Object lock = new Object();

  private GlobalVarQueueV2() {
    this.auditList = new ArrayList<>(AppConstants.AUDIT_INSERT_LIST_CACHE_SIZE);
    // 启动定时任务
    Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        setUpDbInsertRunnable();
      }
    }, AppConstants.AUDIT_INSERT_EVERY_SECOND, AppConstants.AUDIT_INSERT_EVERY_SECOND,
        TimeUnit.SECONDS);
  }

  /**
   * 添加上下文信息到队列.
   */
  public void add(GlobalVar gv) {
    boolean isSuccess = globVarQueue.offer(gv);
    if (!isSuccess) {
      log.error("Can not add " + gv + " into global queue.");
    }
  }

  /**
   * 创建一个新的list用于缓存audit列表信息, 返回当前正在使用的audit列表.
   *
   * @return currentAuditList
   */
  private List<Audit> takeAuditList() {
    synchronized (lock) {
      List<Audit> currentAuditList = this.auditList;
      // replace auditList with a new list
      this.auditList = new ArrayList<>(AppConstants.AUDIT_INSERT_LIST_SIZE);
      return currentAuditList;
    }
  }

  @PostConstruct
  private void init() {
    new Thread() {

      /**
       * 构造数据库插入的Audit对象.
       *
       * @param gv GlobalVar
       * @return Audit
       */
      private Audit constructAudit(GlobalVar gv) {
        if (gv == null) {
          return null;
        }
        Audit audit = new Audit();
        audit.setReqDate(gv.getReqDate());
        audit.setReqIp(gv.getIp());
        audit.setReqElapse(gv.getElapse());
        String expInfo = gv.getException();
        if (expInfo != null && expInfo.length() > AppConstants.AUDIT_INSERT_EXP_LENGTH) {
          expInfo = expInfo.substring(0, AppConstants.AUDIT_INSERT_EXP_LENGTH);
        }
        audit.setReqExp(expInfo);
        audit.setReqMethod(gv.getMethod());

        String reqParam = gv.getReqParam();
        if (reqParam != null && reqParam.length() > AppConstants.AUDIT_INSET_PARAM_LENGTH) {
          reqParam = reqParam.substring(0, AppConstants.AUDIT_INSET_PARAM_LENGTH);
        }
        audit.setReqParam(RegExpUtil.purgePassword(reqParam));
        audit.setReqSuccess(gv.getSuccess());
        audit.setReqUrl(gv.getReqUrl());
        audit.setReqUuid(gv.getUuid());
        audit.setUserId(gv.getUserId());
        audit.setReqClass(gv.getMapper());
        audit.setReqSeq(gv.getInvokeSeq());
        audit.setDomainId(gv.getDomainId());
        audit.setTenancyId(gv.getTenancyId());
        audit.setRequestDomainCode(gv.getRequestDomainCode());
        return audit;
      }

      public void run() {
        while (true) {
          try {
            GlobalVar gv = globVarQueue.take();
            cacheAudit(constructAudit(gv));
            GlobalVar polgv = globVarQueue.poll();
            // 队列中没有数据了, 立即将缓存中的数据插入到数据库
            if (polgv == null) {
              setUpDbInsertRunnable();
            } else {
              cacheAudit(constructAudit(polgv));
            }
          } catch (Exception e) {
            log.error("Take from GLOBALVAR_QUEUE error.", e);
          }
        }
      }
    }.start();
  }

  /**
   * 将新的audit加入缓存列表.
   *
   * @param audit 新的audit信息
   */
  private void cacheAudit(Audit audit) {
    if (audit == null) {
      return;
    }
    // 超出缓存列表大小
    if (this.auditList.size() >= AppConstants.AUDIT_INSERT_LIST_SIZE) {
      if (this.totalCacheNum.get() >= AppConstants.AUDIT_INSERT_LIST_CACHE_SIZE) {
        // ignore current audit info
        log.error("ignore current audit info {}", audit);
      } else {
        setUpDbInsertRunnable();
      }
    } else {
      // 缓存数量 +1
      totalCacheNum.incrementAndGet();
      this.auditList.add(audit);
    }
  }

  /**
   * 将缓存列表中的数据插入到数据库.
   */
  private void setUpDbInsertRunnable() {
    log.debug("set up 1 fast audit insert runnable");
    insertDbThreadPool.execute(new SaveToDbThread(takeAuditList()));
  }

  /**
   * 日志数据插入任务.
   */
  private class SaveToDbThread implements Runnable {

    private List<Audit> toBeInsertedAuditList;

    public SaveToDbThread(List<Audit> toBeInsertedAuditList) {
      this.toBeInsertedAuditList = toBeInsertedAuditList;
    }

    public void run() {
      if (this.toBeInsertedAuditList == null || this.toBeInsertedAuditList.isEmpty()) {
        log.debug("no audit need to insert to DB");
        return;
      }
      int size = toBeInsertedAuditList.size();
      log.debug("Size for insertAuditList: {}", size);
      try {
        // 一次性插入所有数据
        auditMapper.insertBatch(toBeInsertedAuditList);
      } catch (Exception e) {
        log.error("Inner:Batch insert db error.", e);
      } finally {
        // 更新内存缓存对象数量
        totalCacheNum.addAndGet(-size);
      }
    }
  }
}
