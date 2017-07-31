package com.xxx.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by W510 on 2015/12/3.
 */
public class ZookeeperTest {

  /**
   * 主方法.
   */
  public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
    Watcher watcher = new Watcher() {
      @Override
      public void process(WatchedEvent event) {
        System.out.println(event.toString());
      }
    };
    ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 3000, watcher);
    zooKeeper.create("/temp", "hahaha".getBytes(), 
        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

    while (true) {
      String value = new String(zooKeeper.getData("/temp", false, null));
      System.out.println("value = " + value.toString());
      Thread.sleep(3000);
    }


  }
}
