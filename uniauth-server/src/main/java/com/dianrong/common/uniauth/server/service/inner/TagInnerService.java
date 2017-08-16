package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.GrpTagExample;
import com.dianrong.common.uniauth.server.data.entity.GrpTagKey;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagExample;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.entity.UserTagExample;
import com.dianrong.common.uniauth.server.data.entity.UserTagKey;
import com.dianrong.common.uniauth.server.data.mapper.GrpTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserTagMapper;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 标签操作的内部服务.
 */
@Service
public class TagInnerService extends TenancyBasedService {

  @Autowired
  private TagMapper tagMapper;

  @Autowired
  private GrpTagMapper grpTagMapper;
  
  @Autowired
  private UserTagMapper userTagMapper;

  @Autowired
  private TagTypeMapper tagTypeMapper;

  /**
   * 获取组与标签的映射信息.
   * 
   * @param grpIds 查询组的范围.
   * @param domainId 所在的域id.
   */
  public Map<Integer, List<TagDto>> queryGrpTag(List<Integer> grpIds, Integer domainId) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    Map<Integer, List<TagDto>> grpTagMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(grpIds)) {
      return grpTagMap;
    }

    // 控制tag的范围
    GrpTagExample grpTagExample = new GrpTagExample();
    GrpTagExample.Criteria criteria = grpTagExample.createCriteria();
    criteria.andGrpIdIn(grpIds);
    List<GrpTagKey> grpTagList = grpTagMapper.selectByExample(grpTagExample);
    if (ObjectUtil.collectionIsEmptyOrNull(grpTagList)) {
      return grpTagMap;
    }
    List<Integer> tagIds = Lists.newArrayList();
    for (GrpTagKey gt : grpTagList) {
      tagIds.add(gt.getTagId());
    }

    // 根据domainId控制TagType的范围
    TagTypeExample tagTypeExample = new TagTypeExample();
    TagTypeExample.Criteria tagTypeCriteria = tagTypeExample.createCriteria();
    tagTypeCriteria.andDomainIdEqualTo(domainId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
    if (ObjectUtil.collectionIsEmptyOrNull(tagTypes)) {
      return grpTagMap;
    }
    Map<Integer, TagType> tagTypeMap = Maps.newHashMap();
    for (TagType tagType : tagTypes) {
      tagTypeMap.put(tagType.getId(), tagType);
    }

    TagExample tagExample = new TagExample();
    TagExample.Criteria tagCriteria = tagExample.createCriteria();
    tagCriteria.andIdIn(tagIds).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTagTypeIdIn(new ArrayList<>(tagTypeMap.keySet()));
    List<Tag> tags = tagMapper.selectByExample(tagExample);
    if (ObjectUtil.collectionIsEmptyOrNull(tags)) {
      return grpTagMap;
    }
    Map<Integer, Tag> tagMap = Maps.newHashMap();
    for (Tag tag : tags) {
      tagMap.put(tag.getId(), tag);
    }

    // 构建组与角色的映射
    for (GrpTagKey gtk : grpTagList) {
      Integer grpId = gtk.getGrpId();
      List<TagDto> tagDtos = grpTagMap.get(grpId);
      if (tagDtos == null) {
        tagDtos = Lists.newArrayList();
        grpTagMap.put(grpId, tagDtos);
      }
      TagDto tagDto = BeanConverter.convert(tagMap.get(gtk.getTagId()));
      if (tagDto == null) {
        continue;
      }
      TagType tagType = tagTypeMap.get(tagDto.getTagTypeId());
      if (tagType != null) {
        tagDto.setTagTypeCode(tagType.getCode());
      }
      tagDtos.add(tagDto);
    }

    return grpTagMap;
  }
  
  /**
   * 获取用户与标签的映射信息.
   * 
   * @param userIds 查询用户的范围.
   * @param domainId 所在的域id.
   */
  public Map<Long, List<TagDto>> queryUserTag(List<Long> userIds, Integer domainId) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    Map<Long, List<TagDto>> userTagMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(userIds)) {
      return userTagMap;
    }

    // 控制tag的范围
    UserTagExample userTagExample = new UserTagExample();
    UserTagExample.Criteria criteria = userTagExample.createCriteria();
    criteria.andUserIdIn(userIds);
    List<UserTagKey> userTagList = userTagMapper.selectByExample(userTagExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userTagList)) {
      return userTagMap;
    }
    List<Integer> tagIds = Lists.newArrayList();
    for (UserTagKey gt : userTagList) {
      tagIds.add(gt.getTagId());
    }

    // 根据domainId控制TagType的范围
    TagTypeExample tagTypeExample = new TagTypeExample();
    TagTypeExample.Criteria tagTypeCriteria = tagTypeExample.createCriteria();
    tagTypeCriteria.andDomainIdEqualTo(domainId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
    if (ObjectUtil.collectionIsEmptyOrNull(tagTypes)) {
      return userTagMap;
    }
    Map<Integer, TagType> tagTypeMap = Maps.newHashMap();
    for (TagType tagType : tagTypes) {
      tagTypeMap.put(tagType.getId(), tagType);
    }

    TagExample tagExample = new TagExample();
    TagExample.Criteria tagCriteria = tagExample.createCriteria();
    tagCriteria.andIdIn(tagIds).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTagTypeIdIn(new ArrayList<>(tagTypeMap.keySet()));
    List<Tag> tags = tagMapper.selectByExample(tagExample);
    if (ObjectUtil.collectionIsEmptyOrNull(tags)) {
      return userTagMap;
    }
    Map<Integer, Tag> tagMap = Maps.newHashMap();
    for (Tag tag : tags) {
      tagMap.put(tag.getId(), tag);
    }

    // 构建组与角色的映射
    for (UserTagKey gtk : userTagList) {
      Long userId = gtk.getUserId();
      List<TagDto> tagDtos = userTagMap.get(userId);
      if (tagDtos == null) {
        tagDtos = Lists.newArrayList();
        userTagMap.put(userId, tagDtos);
      }
      TagDto tagDto = BeanConverter.convert(tagMap.get(gtk.getTagId()));
      if (tagDto == null) {
        continue;
      }
      TagType tagType = tagTypeMap.get(tagDto.getTagTypeId());
      if (tagType != null) {
        tagDto.setTagTypeCode(tagType.getCode());
      }
      tagDtos.add(tagDto);
    }

    return userTagMap;
  }
}
