package com.xxx.zookeeper;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

/**
 * Curator framework's distributed lock test.
 */
public class CuratorDistrLockTest {

  /**
   * Zookeeper info.
   */
  private static final String ZK_ADDRESS = "127.0.0.1:12181";
  private static final String ZK_LOCK_PATH = "/temp/lock";

  /**
   * 主方法.
   */
  public static void main(String[] args) throws InterruptedException {
    // 1.Connect to zk
    final CuratorFramework client = CuratorFrameworkFactory.newClient(
        ZK_ADDRESS,
        new RetryNTimes(10, 5000)
    );
    client.start();
    System.out.println("zk client start successfully!");

    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {

        doWithLock(client);
      }
    }, "t1");
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        doWithLock(client);
      }
    }, "t2");

    t1.start();
    t2.start();
  }

  private static void doWithLock(CuratorFramework client) {
    InterProcessMutex lock = new InterProcessMutex(client, ZK_LOCK_PATH);
    try {
      if (lock.acquire(10, TimeUnit.SECONDS)) {
        System.out.println(Thread.currentThread().getName() + " hold lock");
        Thread.sleep(10000L);
        System.out.println(Thread.currentThread().getName() + " release lock");
      } else {
        System.out.println(Thread.currentThread().getName() + " Can not get lock");
      }
    } catch (Exception e) {
      System.out.println(Thread.currentThread().getName() + " Exception ");
      e.printStackTrace();
    } finally {
      try {
        System.out.println(Thread.currentThread().getName() 
            + " ======== " + Arrays.toString(lock.getParticipantNodes().toArray()));
        System.out.println(Thread.currentThread().getName() 
            + " isAcquiredInThisProcess " + lock.isAcquiredInThisProcess());
        lock.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }
}
