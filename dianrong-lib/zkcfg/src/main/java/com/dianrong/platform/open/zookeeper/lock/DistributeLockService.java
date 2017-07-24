package com.dianrong.platform.open.zookeeper.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("openDistributeLockService")
public class DistributeLockService {

  private static final Logger logger = LoggerFactory.getLogger(DistributeLockService.class);

  @Value("#{openCuratorClientFactory.getClient()}")
  private CuratorFramework client;

  private ExecutorService executorService = Executors.newFixedThreadPool(3);

  /**
   * TryAcquiredLockAndExcute.
   */
  public Object tryAcquiredLockAndExcute(String path, Callable<Object> callable) {
    InterProcessMutex lock = new InterProcessMutex(client, path);
    try {
      if (lock.acquire(5, TimeUnit.SECONDS)) {
        logger.info(Thread.currentThread().getName() + " acquired and hold Lock,path:{}", path);
        Future<Object> future = executorService.submit(callable);
        return future.get();
      } else {
        logger.info(Thread.currentThread().getName() + " can't acquired lock,path:{}", path);
        return null;
      }
    } catch (Exception e) {
      logger.error("Try Acquired Lock And Excute Exception,path:{}", path, e);
      return null;
    } finally {
      try {
        lock.release();
        logger.info(Thread.currentThread().getName() + " Release Lock,,path:{}", path);
      } catch (Exception e) {
        logger.warn("failed releace lock", e);
      }
    }
  }

}
