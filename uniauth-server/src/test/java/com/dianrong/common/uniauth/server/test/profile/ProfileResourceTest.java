package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendParam;
import com.dianrong.common.uniauth.common.bean.request.ProfileDefinitionParam;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.server.resource.ProfileResource;
import com.dianrong.common.uniauth.server.test.BaseTest;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileResourceTest extends BaseTest {

  @Autowired
  private ProfileResource profileResource;
  
  @Test
  public void testAddNewProfileDefinition() {
    CxfHeaderHolder.TENANCYCODE.set("dianrong");
    ProfileDefinitionParam param = new ProfileDefinitionParam();
    param.setName("ProfileGeo");
    param.setCode("ProfileGeo");
    param.setDescription("Geo的Profile");
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    attributes.put("age", new AttributeExtendParam().setDescription("年龄").setCategory("basic"));
    attributes.put("color", new AttributeExtendParam().setDescription("肤色").setCategory("basic"));
    attributes.put("work", new AttributeExtendParam().setDescription("职位描述").setCategory("basic"));
    attributes.put("eye", new AttributeExtendParam().setDescription("视力").setCategory("basic"));
    attributes.put("qq", new AttributeExtendParam().setDescription("qq号码").setCategory("basic"));
    Response<ProfileDefinitionDto>response = profileResource.addNewProfileDefinition(param);
    System.out.println(JsonUtil.object2Jason(response));
  }
  
  @Test
  public void testGetProfileDefinition() {
    CxfHeaderHolder.TENANCYCODE.set("dianrong");
    Long profileId = 4L;
    Response<ProfileDefinitionDto>response = profileResource.getProfileDefinition(profileId);
    System.out.println(JsonUtil.object2Jason(response));
  }
  
  @Test
  public void testUpdateProfileDefinition() {
    CxfHeaderHolder.TENANCYCODE.set("dianrong");
    Long profileId = 4L;
    
    // extend attributes
    ProfileDefinitionParam param = new ProfileDefinitionParam();
    param.setName("ProfileGeo");
    param.setCode("ProfileGeo");
    param.setDescription("Geo的Profile");
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    attributes.put("age", new AttributeExtendParam().setDescription("年龄").setCategory("basic"));
    attributes.put("color", new AttributeExtendParam().setDescription("肤色").setCategory("basic"));
    attributes.put("work", new AttributeExtendParam().setDescription("职位描述").setCategory("basic"));
    attributes.put("eye", new AttributeExtendParam().setDescription("视力").setCategory("basic"));
    attributes.put("qq", new AttributeExtendParam().setDescription("qq号码").setCategory("basic"));
    Set<Long> descendantProfileIds = Sets.newHashSet();
    descendantProfileIds.add(3L);
    param.setDescendantProfileIds(descendantProfileIds);
    Response<ProfileDefinitionDto>response = profileResource.updateProfileDefinition(profileId, param);
    System.out.println(JsonUtil.object2Jason(response));
  }
}
