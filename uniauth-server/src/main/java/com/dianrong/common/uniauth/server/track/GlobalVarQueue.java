package com.dianrong.common.uniauth.server.track;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Audit;
import com.dianrong.common.uniauth.server.data.mapper.AuditMapper;
import com.dianrong.common.uniauth.server.util.RegExpUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlobalVarQueue {

    @Autowired
    private AuditMapper auditMapper;

    private BlockingQueue<GlobalVar> GLOBALVAR_QUEUE = new ArrayBlockingQueue<GlobalVar>(AppConstants.GLOBALVAR_QUEUE_SIZE);

    /**
     * 实际插入数据任务线程池 两个活跃线程: 1. 定时插入线程 2.数据量大的时候高速插入信息线程
     */
    private final ExecutorService insertDBThreadPool = Executors.newScheduledThreadPool(2);

    /**
     * 缓存audit列表
     */
    private volatile List<Audit> auditList;

    /**
     * 标识位, 用于指定高速插入数据的任务是否正在工作
     */
    private volatile boolean fastInsertRunnableBusy;

    private final Object lock = new Object();

    private GlobalVarQueue() {
        this.auditList = new ArrayList<>(AppConstants.AUDIT_INSERT_LIST_CACHE_SIZE);
        // 启动定时任务
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                insertDBThreadPool.execute(new SaveToDbThread(takeAuditList()));
            }
        }, AppConstants.AUDIT_INSERT_EVERY_SECOND, AppConstants.AUDIT_INSERT_EVERY_SECOND, TimeUnit.SECONDS);
    }

    public void add(GlobalVar gv) {
        boolean isSuccess = GLOBALVAR_QUEUE.offer(gv);
        if (!isSuccess) {
            log.error("Can not add " + gv + " into global queue.");
        }
    }

    /**
     * 创建一个新的list用于缓存audit列表信息, 返回当前正在使用的audit列表
     * 
     * @return currentAuditList
     */
    private List<Audit> takeAuditList() {
        synchronized (lock) {
            List<Audit> currentAuditList = this.auditList;
            // replace auditList with a new list
            this.auditList = new ArrayList<>(AppConstants.AUDIT_INSERT_LIST_CACHE_SIZE);
            return currentAuditList;
        }
    }

    @PostConstruct
    private void init() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        GlobalVar gv = GLOBALVAR_QUEUE.take();
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
                            cacheAudit(audit);
                        }
                    } catch (Exception e) {
                        log.error("Take from GLOBALVAR_QUEUE error.", e);
                    }
                }
            }
        }.start();
    }

    /**
     * 将新的audit加入缓存列表
     * 
     * @param audit 新的audit信息
     */
    private void cacheAudit(Audit audit) {
        if (audit == null) {
            return;
        }
        // 超出缓存列表大小
        if (this.auditList.size() >= AppConstants.AUDIT_INSERT_LIST_CACHE_SIZE) {
            if (this.fastInsertRunnableBusy) {
                // ignore current audit info
                log.debug("ignore current audit info {}", audit);
            } else {
                log.debug("set up 1 fast audit insert runnable");
                insertDBThreadPool.execute(new SaveToDbThread(takeAuditList(), new CallBefore() {
                    @Override
                    public void call() {
                        fastInsertRunnableBusy = true;
                    }
                }, new CallBack() {
                    @Override
                    public void call() {
                        fastInsertRunnableBusy = false;
                    }
                }));
            }
        } else {
            this.auditList.add(audit);
        }
    }

    private static interface CallBefore{
        void call();
    };
    
    private static interface CallBack{
        void call();
    };
    
    /**
     * 日志数据插入任务
     */
    private class SaveToDbThread implements Runnable {
        private List<Audit> toBeInsertedAuditList;
        
        /**
         * 任务扩展实现
         */
        private CallBefore callBefore;
        private CallBack callBack;

        public SaveToDbThread(List<Audit> toBeInsertedAuditList) {
            this(toBeInsertedAuditList, null, null);
        }
        
        public SaveToDbThread(List<Audit> toBeInsertedAuditList, CallBefore callBefore, CallBack callBack) {
            this.toBeInsertedAuditList = toBeInsertedAuditList;
            this.callBefore = callBefore;
            this.callBack = callBack;
        }

        public void run() {
            try {
                if (this.callBefore != null) {
                    this.callBefore.call();
                }
                if (this.toBeInsertedAuditList == null || this.toBeInsertedAuditList.isEmpty()) {
                    log.debug("no audit need to insert to DB");
                    return;
                }
                int size = toBeInsertedAuditList.size();
                log.debug("Size for insertAuditList:" + toBeInsertedAuditList.size());
                int start = 0;
                int end = AppConstants.AUDIT_INSERT_LIST_SIZE;
                while (true) {
                    if (end > size) {
                        end = size;
                    }
                    try {
                        auditMapper.insertBatch(toBeInsertedAuditList.subList(start, end));
                    } catch (Exception e) {
                        log.error("Inner:Batch insert db error.", e);
                    }
                    if (end >= size) {
                        break;
                    }
                    start = end;
                    end += AppConstants.AUDIT_INSERT_LIST_SIZE;
                }
            } finally {
                if (this.callBack != null) {
                    this.callBack.call();
                }
            }
        }
    }
}
