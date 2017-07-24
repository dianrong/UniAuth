package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendParam;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
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
    ProfileParam param = new ProfileParam();
    param.setTenancyCode("dianrong");
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    attributes.put("name",
        new AttributeExtendParam().setValue("小王的测试组"));
    attributes.put("code",
        new AttributeExtendParam().setValue("code_unmodifiable"));
    attributes.put("user_target",
        new AttributeExtendParam().setValue("指定组的目的"));
    attributes.put("color",
        new AttributeExtendParam().setValue("black-white"));
    attributes.put("class",
        new AttributeExtendParam().setValue(null));
    attributes.put("description",
        new AttributeExtendParam().setValue("小王写的description"));
    Long profileId = 1L;
    Integer grpId = 100013;
    Response<Map<String, Object>> response =
        groupProfileResource.addOrUpdateGrpProfile(grpId, profileId, param);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testGetGroupProfile() {
    Long profileId = 1L;
    Integer grpId = 100013;
    Response<Map<String, Object>> response =
        groupProfileResource.getGroupProfile(grpId, profileId, 1499417817655L);
    System.out.println(JsonUtil.object2Jason(response));
  }
}
