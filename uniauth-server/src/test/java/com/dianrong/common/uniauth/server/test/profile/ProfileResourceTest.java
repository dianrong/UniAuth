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
    ProfileDefinitionParam param = new ProfileDefinitionParam();
    param.setName("组的基础Profile");
    param.setCode("BasicGroupProle");
    param.setDescription("组的基础Profile");
    param.setTenancyCode("dianrong");
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    Set<Long> descendantProfileIds = Sets.newHashSet();
    descendantProfileIds.add(3L);
    param.setDescendantProfileIds(descendantProfileIds);
    attributes.put("user_target",
        new AttributeExtendParam().setDescription("组的使用目标").setCategory("basic_group"));
    attributes.put("class",
        new AttributeExtendParam().setDescription("类型").setCategory("basic_group"));
    attributes.put("color",
        new AttributeExtendParam().setDescription("组的标识色").setCategory("basic_group"));
    attributes.put("description",
        new AttributeExtendParam().setDescription("描述").setCategory("basic_group"));
    attributes.put("name",
        new AttributeExtendParam().setDescription("名称").setCategory("basic_group"));
    attributes.put("code",
        new AttributeExtendParam().setDescription("组编码").setCategory("basic_group"));
    Response<ProfileDefinitionDto> response = profileResource.addNewProfileDefinition(param);
    System.out.println(JsonUtil.object2Jason(response));
  }
  
  @Test
  public void testUpdateProfileDefinition2() {
    CxfHeaderHolder.TENANCYCODE.set("dianrong");
    ProfileDefinitionParam param = new ProfileDefinitionParam();
    param.setName("组的基础Profile");
    param.setCode("BasicGroupProle");
    param.setDescription("组的基础Profile");
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    Set<Long> descendantProfileIds = Sets.newHashSet();
    descendantProfileIds.add(3L);
    param.setDescendantProfileIds(descendantProfileIds);
    attributes.put("user_target",
        new AttributeExtendParam().setDescription("组的使用目标").setCategory("basic_group"));
    attributes.put("class",
        new AttributeExtendParam().setDescription("类型").setCategory("basic_group"));
    attributes.put("color",
        new AttributeExtendParam().setDescription("组的标识色").setCategory("basic_group"));
    attributes.put("description",
        new AttributeExtendParam().setDescription("描述").setCategory("basic_group"));
    attributes.put("name",
        new AttributeExtendParam().setDescription("名称").setCategory("basic_group"));
    attributes.put("code",
        new AttributeExtendParam().setDescription("组编码").setCategory("basic_group"));
    Long profileId = 8L;
    Response<ProfileDefinitionDto> response =
        profileResource.updateProfileDefinition(profileId, param);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testGetProfileDefinition() {
    Long profileId = 4L;
    Response<ProfileDefinitionDto> response = profileResource.getProfileDefinition(profileId);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testUpdateProfileDefinition() {
    CxfHeaderHolder.TENANCYCODE.set("dianrong");

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
    attributes.put("mother_name",
        new AttributeExtendParam().setDescription("母亲名字").setCategory("basic"));
    attributes.put("father_name",
        new AttributeExtendParam().setDescription("父亲名字").setCategory("basic"));
    Set<Long> descendantProfileIds = Sets.newHashSet();
    descendantProfileIds.add(3L);
    descendantProfileIds.add(6L);
    descendantProfileIds.add(5L);
    param.setDescendantProfileIds(descendantProfileIds);
    Long profileId = 4L;
    Response<ProfileDefinitionDto> response =
        profileResource.updateProfileDefinition(profileId, param);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testExtendProfileDefinition() {
    // extend attributes
    ProfileDefinitionParam param = new ProfileDefinitionParam();
    param.setTenancyCode("dianrong");
    Map<String, AttributeExtendParam> attributes = Maps.newHashMap();
    param.setAttributes(attributes);
    attributes.put("description",
        new AttributeExtendParam().setDescription("描述").setCategory("basic"));
    Set<Long> descendantProfileIds = Sets.newHashSet();
    param.setDescendantProfileIds(descendantProfileIds);
    Long profileId = 1L;
    Response<ProfileDefinitionDto> response =
        profileResource.extendProfileDefinition(profileId, param);
    System.out.println(JsonUtil.object2Jason(response));
  }
}
