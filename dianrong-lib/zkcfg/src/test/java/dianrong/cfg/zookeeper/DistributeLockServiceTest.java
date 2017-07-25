package dianrong.cfg.zookeeper;

import com.dianrong.platform.open.zookeeper.lock.DistributeLockService;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:applicationContext-test.xml"})
public class DistributeLockServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(DistributeLockServiceTest.class);

  @Autowired
  private DistributeLockService distributeLockService;

  @Value("#{openCuratorClientFactory.getClient()}")
  private CuratorFramework client;

  @Test
  public void find() throws ExecutionException, InterruptedException {
    logger.info("find begin...");
    Object result = distributeLockService.tryAcquiredLockAndExcute("/com/xxx/cfg",
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            logger.info("work done");
            return BigDecimal.ONE;
          }
        });
    Assert.assertEquals(result.toString(), "1");
  }

  @After
  public void after() {
    client.close();
  }

}
