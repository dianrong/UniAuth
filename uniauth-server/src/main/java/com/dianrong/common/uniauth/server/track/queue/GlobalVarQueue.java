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
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalVarQueue {

  @Autowired
  private AuditMapper auditMapper;

  private BlockingQueue<GlobalVar> globVarQueue =
      new ArrayBlockingQueue<GlobalVar>(AppConstants.GLOBALVAR_QUEUE_SIZE);

  private Object lock = new Object();

  private GlobalVarQueue() {

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

  @PostConstruct
  private void init() {
    new Thread() {
      public void run() {
        List<Audit> auditList = new ArrayList<Audit>();
        new SaveToDbThread(auditList).start();

        while (true) {
          try {
            GlobalVar gv = globVarQueue.take();
            if (gv != null) {
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
              synchronized (lock) {
                auditList.add(audit);
              }
            }
          } catch (Exception e) {
            log.error("Take from GLOBALVAR_QUEUE error.", e);
          }
        }
      }
    }.start();
  }

  private class SaveToDbThread extends Thread {

    private List<Audit> toBeInsertedAuditList;

    public SaveToDbThread(List<Audit> toBeInsertedAuditList) {
      this.toBeInsertedAuditList = toBeInsertedAuditList;
    }

    public void run() {
      List<Audit> insertAuditList = new ArrayList<Audit>();
      while (true) {
        try {
          sleep(AppConstants.AUDIT_INSERT_EVERY_SECOND * 1000L);
          synchronized (lock) {
            insertAuditList.addAll(toBeInsertedAuditList);
            toBeInsertedAuditList.clear();
          }

          int size = insertAuditList.size();
          log.debug("Size for insertAuditList:" + insertAuditList.size());
          if (size > 0) {
            int start = 0;
            int end = AppConstants.AUDIT_INSERT_LIST_SIZE;
            while (true) {
              if (end > size) {
                end = size;
              }
              try {
                auditMapper.insertBatch(insertAuditList.subList(start, end));
              } catch (Exception e) {
                log.error("Inner:Batch insert db error.", e);
              }
              if (end == size) {
                break;
              }
              start = end;
              end += AppConstants.AUDIT_INSERT_LIST_SIZE;
            }
            insertAuditList.clear();
          }
        } catch (Exception e) {
          log.error("Sleep error.", e);
        }
      }
    }
  }
}
