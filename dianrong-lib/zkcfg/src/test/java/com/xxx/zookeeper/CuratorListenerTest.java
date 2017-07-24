package com.xxx.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by W510 on 2015/12/3.
 */
public class CuratorListenerTest {

  /**
   * 主方法.
   */
  public static void main(String[] args) throws Exception {
    CuratorFramework curatorFramework = null;
    try {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(6000, 10);
      curatorFramework = CuratorFrameworkFactory.builder()
          .connectString("127.0.0.1:2181")
          .retryPolicy(retryPolicy)
          .connectionTimeoutMs(5000)
          .build();
      curatorFramework.start();

      curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
          System.out.println(newState.toString());
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
    }

    while (true) {
      String value = new String(curatorFramework.getData().forPath("/temp"));
      System.out.println("value === " + value);
      Thread.sleep(5000);
    }
  }


}
