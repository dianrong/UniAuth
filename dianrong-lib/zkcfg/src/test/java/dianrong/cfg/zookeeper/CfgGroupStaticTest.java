package dianrong.cfg.zookeeper;

import com.dianrong.platform.open.cfg.CfgGroup;

import javax.annotation.Resource;

import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    value = {"classpath:applicationContext-cfg-group-test.xml",})
public class CfgGroupStaticTest {

  @Resource
  private CfgGroup testGroup;

  @Value("#{testGroup['OSS_ACCESS_KEY_ID']}")
  private String ossAccessKeyId;

  @Test
  public void test() throws Exception {
    System.out.println("testGroup size =" + testGroup.size());
    System.out.println("ossAccessKeyId = " + ossAccessKeyId);
    Assert.assertTrue(testGroup.size() != 0);
    Assert.assertTrue(Strings.isNotEmpty(ossAccessKeyId));
  }
}


