package com.dianrong.common.uniauth.server.service.attributerecord;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.GrpAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.data.mapper.GrpAttributeRecordsMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserAttributeRecordsMapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异步记录属性修改记录的队列.
 */
@Component
@Slf4j
public class AttributeRecordsQueue implements InitializingBean {
  /**
   * 队列的大小.
   */
  private static final int QUEUE_SIEZ = 1024;

  @Autowired
  private GrpAttributeRecordsMapper grpAttributeRecordsMapper;

  @Autowired
  private UserAttributeRecordsMapper userAttributeRecordsMapper;

  private BlockingQueue<AttributeRecords> attributeRecordsQueue =
      new ArrayBlockingQueue<AttributeRecords>(QUEUE_SIEZ);

  /**
   * 单个线程插入数据池..
   */
  private final ExecutorService insertThreadPool = Executors.newSingleThreadExecutor();

  private AttributeRecordsQueue() {}

  /**
   * 添加上下文信息到队列.
   */
  public boolean add(AttributeRecords attributeRecord) {
    boolean isSuccess = attributeRecordsQueue.offer(attributeRecord);
    if (!isSuccess) {
      log.error("Can not add " + attributeRecord + " into attribute records queue.");
    }
    return isSuccess;
  }

  private void init() {
    insertThreadPool.execute(new Runnable() {
      public void run() {
        while (true) {
          try {
            AttributeRecords record = attributeRecordsQueue.take();
            if (record instanceof UserAttributeRecords) {
              userAttributeRecordsMapper.insert((UserAttributeRecords) record);
              log.debug("success insert {}", record);
              return;
            }
            if (record instanceof GrpAttributeRecords) {
              grpAttributeRecordsMapper.insert((GrpAttributeRecords) record);
              log.debug("success insert {}", record);
              return;
            }
            log.warn("{} is not supported to insert, so just ignored!", record);
          } catch (Exception e) {
            log.error("Insert into attribute records error!", e);
          }
        }
      }
    });
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    init();
  }
}
