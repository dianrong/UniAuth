package com.dianrong.common.uniauth.server.track;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Audit;
import com.dianrong.common.uniauth.server.data.mapper.AuditMapper;

@Component
public class GlobalVarQueue {

	@Autowired
	private AuditMapper auditMapper;

	private static Logger logger = LoggerFactory.getLogger(GlobalVarQueue.class);

	private BlockingQueue<GlobalVar> GLOBALVAR_QUEUE = new ArrayBlockingQueue<GlobalVar>(AppConstants.GLOBALVAR_QUEUE_SIZE);

	private GlobalVarQueue() {

	}

	public void add(GlobalVar gv) {
		boolean isSuccess = GLOBALVAR_QUEUE.offer(gv);
		if (!isSuccess) {
			logger.error("Can not add " + gv + " into global queue.");
		}
	}

	@PostConstruct
	private void init() {
		new Thread() {
			public void run() {
				List<Audit> auditList = new ArrayList<Audit>();
				while (true) {
					if (auditList.size() == AppConstants.AUDIT_INSERT_LIST_SIZE) {
						try {
							auditMapper.insertBatch(auditList);
						} catch (Exception e) {
							logger.error("Batch insert db error.", e);
						}
						auditList.clear();
					} else {
						GlobalVar gv = null;
						try {
							gv = GLOBALVAR_QUEUE.take();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (gv != null) {
							Audit audit = new Audit();
							audit.setReqDate(gv.getReqDate());
							audit.setReqIp(gv.getIp());
							audit.setReqElapse(gv.getElapse());
							String expInfo = gv.getException();
							if(expInfo != null && expInfo.length() > AppConstants.AUDIT_INSERT_EXP_LENGTH){
								expInfo = expInfo.substring(0, AppConstants.AUDIT_INSERT_EXP_LENGTH);
							}
							audit.setReqExp(expInfo);
							audit.setReqMethod(gv.getMethod());
							
							String reqParam = gv.getReqParam();
							if(reqParam != null && reqParam.length() > AppConstants.AUDIT_INSET_PARAM_LENGTH){
								reqParam = reqParam.substring(0, AppConstants.AUDIT_INSET_PARAM_LENGTH);
							}
							audit.setReqParam(gv.getReqParam());
							audit.setReqSuccess(gv.getSuccess());
							audit.setReqUrl(gv.getReqUrl());
							audit.setReqUuid(gv.getUuid());
							audit.setUserId(gv.getUserId());
							audit.setReqClass(gv.getMapper());
							audit.setReqSeq(gv.getInvokeSeq());
							auditList.add(audit);
						}
					}
				}
			}
		}.start();
	}

}
