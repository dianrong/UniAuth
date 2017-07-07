package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendParam;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.server.resource.GroupProfileResource;
import com.dianrong.common.uniauth.server.test.BaseTest;
import com.google.common.collect.Maps;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户Profile操作相关接口.
 */
public class GroupProfileResourceTest extends BaseTest {

  @Autowired
  private GroupProfileResource groupProfileResource;

  @Test
  public void testAddOrUpdateGrpProfile() {
    CxfHeaderHolder.TENANCYCODE.set("dianrong");
    Long profileId = 8L;
    Integer grpId = 100003;
    ProfileParam param = new ProfileParam();
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    attributes.put("name",
        new AttributeExtendParam().setValue("小王的测试组"));
    attributes.put("code",
        new AttributeExtendParam().setValue("code_unmodifiable"));
    attributes.put("user_target",
        new AttributeExtendParam().setValue("指定组的目的"));
    attributes.put("color",
        new AttributeExtendParam().setValue("black"));
    attributes.put("class",
        new AttributeExtendParam().setValue("good"));
    Response<Map<String, Object>> response =
        groupProfileResource.addOrUpdateGrpProfile(grpId, profileId, param);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testGetGroupProfile() {
    Long profileId = 8L;
    Integer grpId = 100003;
    Response<Map<String, Object>> response =
        groupProfileResource.getGroupProfile(grpId, profileId, null);
    System.out.println(JsonUtil.object2Jason(response));
  }
}
