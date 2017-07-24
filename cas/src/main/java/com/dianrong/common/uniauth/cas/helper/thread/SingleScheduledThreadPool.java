package com.dianrong.common.uniauth.cas.helper.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * . 单例模式的单线程的线程池 只服务于cas cfg的缓存更新操作
 *
 * @author wanglin
 */
public final class SingleScheduledThreadPool {

  public static final SingleScheduledThreadPool INSTANCE = new SingleScheduledThreadPool();

  /**
   * 线程池.
   */
  private final ScheduledExecutorService scheduler;

  /**
   * 私有构造函数.
   */
  private SingleScheduledThreadPool() {
    scheduler = Executors.newSingleThreadScheduledExecutor();
  }

  /**
   * 加载一个定期执行的任务.
   *
   * @param runnable 任务执行的runnable
   * @param delayMilles 延迟执行
   * @param periodMilles 任务执行的周期
   */
  public void loadScheduledTask(Runnable runnable, long delayMilles, long periodMilles) {
    scheduler.scheduleAtFixedRate(runnable, delayMilles, periodMilles, TimeUnit.MILLISECONDS);
  }

  /**
   * 停止接受任务.
   */
  public void shutDown() {
    this.scheduler.shutdown();
  }

  /**
   * 尝试停止线程的执行.
   */
  public void shutDownNow() {
    this.scheduler.shutdownNow();
  }
}
