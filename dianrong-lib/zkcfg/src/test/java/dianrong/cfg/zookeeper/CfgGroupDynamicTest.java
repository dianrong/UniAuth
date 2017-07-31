package dianrong.cfg.zookeeper;

import com.dianrong.platform.open.cfg.CfgEvent;
import com.dianrong.platform.open.cfg.CfgEventListener;
import com.dianrong.platform.open.cfg.CfgGroup;

import javax.annotation.Resource;

import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:applicationContext-cfg-group-test.xml",})
public class CfgGroupDynamicTest {

  private static final String PREX = "/com/xxx/cfg";

  private static final String KEY = "SL_TENPAY_NEW_OPEN";

  @Resource
  private CfgGroup mainappGroup;

  @Test
  public void updateCfgTest() throws Exception {
    mainappGroup.updateCfg(PREX + "/" + KEY, "on");
    Thread.sleep(300);
    Assert.assertTrue("on".equals(mainappGroup.get(KEY)));
    mainappGroup.updateCfg(PREX + "/" + KEY, "off");
    Thread.sleep(300);
    Assert.assertTrue("off".equals(mainappGroup.get(KEY)));
  }

  @Test
  public void crudCfgTest() throws Exception {
    String key = "keyXie";
    String value = "xiexxxxx";
    try {
      mainappGroup.addCfg(PREX + "/" + KEY, value);
    } catch (Exception e) {
      Assert.assertTrue(e.getMessage().endsWith("Already Exists."));
    }
    mainappGroup.addCfg(PREX + "/" + key, value);
    Thread.sleep(200);
    String currentValue = mainappGroup.get(key);
    Assert.assertEquals(value, currentValue);
    mainappGroup.deleteCfg(PREX + "/" + key);
    Thread.sleep(200);
    currentValue = mainappGroup.get(key);
    Assert.assertTrue(Strings.isEmpty(currentValue));
  }

  static class MyListener implements CfgEventListener {
    CfgEvent event;
    String key;
    String value;

    @Override
    public void onEvent(CfgEvent event, String key, String value) {
      this.event = event;
      this.key = key;
      this.value = value;
    }
  }

  @Test
  public void cfgEventListenerTest() throws Exception {
    String key = "keyXie";
    String value1 = "xie";
    MyListener listener = new MyListener();
    mainappGroup.addCfgEventListener(listener);

    mainappGroup.addCfg(PREX + "/" + key, value1);
    Thread.sleep(1000);
    Assert.assertEquals(value1, listener.value);
    Assert.assertEquals(CfgEvent.ADD, listener.event);
    Assert.assertEquals(key, listener.key);

    String value2 = "xing";
    mainappGroup.updateCfg(PREX + "/" + key, value2);
    Thread.sleep(1000);
    Assert.assertEquals(value2, listener.value);
    Assert.assertEquals(CfgEvent.UPDATE, listener.event);
    Assert.assertEquals(key, listener.key);

    mainappGroup.deleteCfg(PREX + "/" + key);
    Thread.sleep(1000);
    Assert.assertEquals(value2, listener.value);
    Assert.assertEquals(CfgEvent.DELETE, listener.event);
    Assert.assertEquals(key, listener.key);

    String actualValue = mainappGroup.get(key);
    Assert.assertTrue(Strings.isEmpty(actualValue));
  }

  @Test
  public void testNonSpring() throws Exception {

    // test with 2 bad IP address start with 10.20
    // and one good address 10.18.19.28
    // the cfg grop will be initialized synchronously
    CfgGroup group = new CfgGroup("127.0.0.1:22181,127.0.0.2:22181,127.0.0.3:22181",
        // CfgGroup group = new CfgGroup("10.18.19.28:22181",
        "/com/xxxx/cfg", true);
    System.out.println("group is " + group.toString());
  }


}


