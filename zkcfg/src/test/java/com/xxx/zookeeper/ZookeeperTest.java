package com.xxx.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by W510 on 2015/12/3.
 */
public class ZookeeperTest {

  public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
    Watcher watcher = new Watcher() {
      @Override
      public void process(WatchedEvent event) {
        System.out.println(event.toString());
      }
    };
    ZooKeeper zooKeeper = new ZooKeeper("192.168.71.136:2181", 3000, watcher);
    zooKeeper.create("/temp", "hahaha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

    while (true) {
      String value = new String(zooKeeper.getData("/temp", false, null));
      System.out.println("value = " + value.toString());
      Thread.sleep(3000);
    }


  }
}
