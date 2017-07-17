package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.GrpTagExample;
import com.dianrong.common.uniauth.server.data.entity.GrpTagKey;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagExample;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.entity.UserTagExample;
import com.dianrong.common.uniauth.server.data.entity.UserTagKey;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserTagMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Created by Arc on 7/4/2016.
 */
@Service
public class TagService extends TenancyBasedService {

  @Autowired
  private UserTagMapper userTagMapper;
  @Autowired
  private TagMapper tagMapper;
  @Autowired
  private TagTypeMapper tagTypeMapper;
  @Autowired
  private GrpTagMapper grpTagMapper;
  @Autowired
  private DomainMapper domainMapper;
  @Autowired
  private DomainService domainService;

  @Resource(name = "tagTypeDataFilter")
  private DataFilter dataFilter;

  @Resource(name = "tagDataFilter")
  private DataFilter tagDataFilter;

  /**
   * 根据条件查询标签信息列表.
   */
  public PageDto<TagDto> searchTags(Integer tagId, List<Integer> tagIds, String tagCode,
      String fuzzyTagCode, Byte tagStatus, Integer tagTypeId, Long userId, Integer domainId,
      String domainCode, List<Integer> domainIds, Integer grpId, Boolean needDomainInfo,
      Integer pageNumber, Integer pageSize) {
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");
    TagExample tagExample = new TagExample();
    tagExample.setOrderByClause("status asc");
    tagExample.setPageOffSet(pageNumber * pageSize);
    tagExample.setPageSize(pageSize);
    TagExample.Criteria criteria = tagExample.createCriteria();
    if (tagId != null) {
      criteria.andIdEqualTo(tagId);
    }
    if (!CollectionUtils.isEmpty(tagIds)) {
      criteria.andIdIn(tagIds);
    }
    if (!StringUtils.isEmpty(tagCode)) {
      criteria.andCodeEqualTo(tagCode);
    }
    if (tagStatus != null) {
      criteria.andStatusEqualTo(tagStatus);
    }
    if (tagTypeId != null) {
      criteria.andTagTypeIdEqualTo(tagTypeId);
    }
    if (!StringUtils.isEmpty(fuzzyTagCode)) {
      criteria.andCodeLike("%" + fuzzyTagCode + "%");
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());

    if (userId != null) {
      UserTagExample userTagExample = new UserTagExample();
      userTagExample.createCriteria().andUserIdEqualTo(userId);
      List<UserTagKey> userTagKeys = userTagMapper.selectByExample(userTagExample);
      if (!CollectionUtils.isEmpty(userTagKeys)) {
        List<Integer> tagIdsQueryByUserId = new ArrayList<>();
        for (UserTagKey userTagKey : userTagKeys) {
          tagIdsQueryByUserId.add(userTagKey.getTagId());
        }
        criteria.andIdIn(tagIdsQueryByUserId);
      } else {
        return null;
      }
    }

    if (domainId != null || !CollectionUtils.isEmpty(domainIds) || domainCode != null) {
      List<Integer> unionDomainIds = new ArrayList<Integer>();
      if (domainId != null) {
        unionDomainIds.add(domainId);
      }
      if (!CollectionUtils.isEmpty(domainIds)) {
        unionDomainIds.addAll(domainIds);
      }
      if (domainCode != null) {
        DomainExample domainExample = new DomainExample();
        domainExample.createCriteria().andCodeEqualTo(domainCode)
            .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
        List<Domain> domains = domainMapper.selectByExample(domainExample);
        if (!CollectionUtils.isEmpty(domains)) {
          for (Domain domain : domains) {
            unionDomainIds.add(domain.getId());
          }
        } else {
          return null;
        }
      }
      TagTypeExample tagTypeExample = new TagTypeExample();
      tagTypeExample.createCriteria().andDomainIdIn(unionDomainIds)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
      if (!CollectionUtils.isEmpty(tagTypes)) {
        List<Integer> tagTypeIds = new ArrayList<>();
        for (TagType tagType : tagTypes) {
          tagTypeIds.add(tagType.getId());
        }
        criteria.andTagTypeIdIn(tagTypeIds);
      } else {
        return null;
      }
    }

    if (grpId != null) {
      GrpTagExample grpTagExample = new GrpTagExample();
      grpTagExample.createCriteria().andGrpIdEqualTo(grpId);
      List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
      if (!CollectionUtils.isEmpty(grpTagKeys)) {
        List<Integer> grpTagKeysTagIds = new ArrayList<>();
        for (GrpTagKey grpTagKey : grpTagKeys) {
          grpTagKeysTagIds.add(grpTagKey.getTagId());
        }
        criteria.andIdIn(grpTagKeysTagIds);
      } else {
        return null;
      }
    }
    int count = tagMapper.countByExample(tagExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<Tag> tags = tagMapper.selectByExample(tagExample);
    if (!CollectionUtils.isEmpty(tags)) {
      List<TagDto> tagDtos = new ArrayList<>();
      for (Tag tag : tags) {
        tagDtos.add(BeanConverter.convert(tag));
      }
      if (needDomainInfo != null && needDomainInfo) {
        List<Integer> tagTypeIds = Lists.newArrayList();
        for (TagDto tagDto : tagDtos) {
          tagTypeIds.add(tagDto.getTagTypeId());
        }
        Map<Integer, DomainDto> domainMap = domainService.getDomainMapByTagTypeIds(tagTypeIds);
        for (TagDto tagDto : tagDtos) {
          tagDto.setDomain(domainMap.get(tagDto.getTagTypeId()));
        }
      }
      return new PageDto<>(pageNumber, pageSize, count, tagDtos);
    } else {
      return null;
    }
  }

  /**
   * 添加新的标签.
   */
  public TagDto addNewTag(String code, Integer tagTypeId, String description) {
    CheckEmpty.checkEmpty(code, "code");
    CheckEmpty.checkEmpty(tagTypeId, "tagTypeId");

    // 不能存在重复的数据
    tagDataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        new FilterData(FieldType.FIELD_TYPE_TAG_TYPE_ID, tagTypeId),
        new FilterData(FieldType.FIELD_TYPE_CODE, code));

    Tag tag = new Tag();
    Date now = new Date();
    tag.setCreateDate(now);
    tag.setLastUpdate(now);
    tag.setStatus(AppConstants.ZERO_BYTE);
    tag.setTagTypeId(tagTypeId);
    tag.setDescription(description);
    tag.setCode(code);
    tag.setTenancyId(tenancyService.getTenancyIdWithCheck());
    tagMapper.insert(tag);
    return BeanConverter.convert(tag);
  }

  /**
   * 更新标签.
   */
  public TagDto updateTag(Integer tagId, String code, Byte status, Integer tagTypeId,
      String description) {
    CheckEmpty.checkEmpty(tagId, "tagId");

    Tag tag = tagMapper.selectByPrimaryKey(tagId);
    if (tag == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", tagId, Tag.class.getSimpleName()));
    }

    // 除了禁用的情况
    if (!(status != null && status != AppConstants.ZERO_BYTE)) {
      // 过滤重复数据 启用状态的才管
      tagDataFilter.updateFieldsCheck(tagId,
          new FilterData(FieldType.FIELD_TYPE_CODE,
              StringUtil.strIsNullOrEmpty(code) ? tag.getCode() : code),
          new FilterData(FieldType.FIELD_TYPE_TAG_TYPE_ID,
              tagTypeId == null ? tag.getTagTypeId() : tagTypeId));
    }

    tag.setStatus(status);
    tag.setCode(code);
    tag.setTagTypeId(tagTypeId);
    tag.setDescription(description);
    tagMapper.updateByPrimaryKey(tag);
    return BeanConverter.convert(tag);
  }

  /**
   * 查询标签的类型信息.
   */
  public List<TagTypeDto> searchTagTypes(Integer tagId, List<Integer> domainIds, Integer domainId,
      String code) {
    TagTypeExample tagTypeExample = new TagTypeExample();
    TagTypeExample.Criteria criteria = tagTypeExample.createCriteria();
    if (domainId != null) {
      criteria.andDomainIdEqualTo(domainId);
    }
    if (tagId != null) {
      criteria.andIdEqualTo(tagId);
    }
    if (domainIds != null) {
      criteria.andDomainIdIn(domainIds);
    }
    if (code != null) {
      criteria.andCodeEqualTo(code);
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
    if (!CollectionUtils.isEmpty(tagTypes)) {
      List<TagTypeDto> tagTypeDtos = new ArrayList<>();
      for (TagType tagType : tagTypes) {
        tagTypeDtos.add(BeanConverter.convert(tagType));
      }
      return tagTypeDtos;
    } else {
      return null;
    }
  }

  /**
   * 添加新的标签类型.
   */
  public TagTypeDto addNewTagType(String code, Integer domainId) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    CheckEmpty.checkEmpty(code, "code");
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_CODE, code),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_DOMAIN_ID, domainId));
    TagType tagType = new TagType();
    tagType.setDomainId(domainId);
    tagType.setCode(code);
    tagType.setTenancyId(tenancyService.getTenancyIdWithCheck());
    tagTypeMapper.insert(tagType);
    return BeanConverter.convert(tagType);
  }

  /**
   * 更新标签类型信息.
   */
  public TagTypeDto updateTagType(Integer tagTypeId, String code, Integer domainId) {
    CheckEmpty.checkEmpty(tagTypeId, "tagTypeId");
    CheckEmpty.checkEmpty(code, "code");
    TagType tagType = tagTypeMapper.selectByPrimaryKey(tagTypeId);
    if (tagType == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", tagTypeId, TagType.class.getSimpleName()));
    }
    tagType.setCode(code);
    if (domainId != null) {
      tagType.setDomainId(domainId);
    }

    // 判断
    dataFilter.updateFieldsCheck(tagTypeId,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_CODE, code),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_DOMAIN_ID, tagType.getDomainId()));

    tagTypeMapper.updateByPrimaryKey(tagType);
    return BeanConverter.convert(tagType);
  }

  /**
   * 删除标签类型.
   */
  public void deleteTagType(Integer tagTypeId) {
    CheckEmpty.checkEmpty(tagTypeId, "tagTypeId");
    TagType tagType = tagTypeMapper.selectByPrimaryKey(tagTypeId);
    if (tagType == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", tagTypeId, TagType.class.getSimpleName()));
    }
    TagExample tagExample = new TagExample();
    tagExample.createCriteria().andTagTypeIdEqualTo(tagTypeId)
        .andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    int count = tagMapper.countByExample(tagExample);
    if (count > 0) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("tagtype.delete.linked-tag.error"));
    }

    TagExample tagExample2 = new TagExample();
    tagExample2.createCriteria().andTagTypeIdEqualTo(tagTypeId)
        .andStatusEqualTo(AppConstants.STATUS_DISABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<Tag> tags = tagMapper.selectByExample(tagExample2);
    if (!CollectionUtils.isEmpty(tags)) {
      List<Integer> tagIds = new ArrayList<>();
      for (Tag tag : tags) {
        tagIds.add(tag.getId());
      }

      UserTagExample userTagExample = new UserTagExample();
      userTagExample.createCriteria().andTagIdIn(tagIds);
      GrpTagExample grpTagExample = new GrpTagExample();
      grpTagExample.createCriteria().andTagIdIn(tagIds);
      userTagMapper.deleteByExample(userTagExample);
      grpTagMapper.deleteByExample(grpTagExample);
      tagMapper.deleteByExample(tagExample2);
    }

    tagTypeMapper.deleteByPrimaryKey(tagTypeId);
  }

  /**
   * 替换Tag关联的组和用户.
   */
  @Transactional
  public void replaceGroupsAndUsersToTag(Integer tagId, List<Integer> grpIdsDup,
      List<Long> userIdsDup, Boolean needProcessGoupIds, Boolean needProcessUserIds) {
    CheckEmpty.checkEmpty(tagId, "tagId");
    List<Integer> grpIds = null;
    List<Long> userIds = null;
    if (needProcessGoupIds != null && needProcessGoupIds) {
      grpIds = new ArrayList<>(new HashSet<>(grpIdsDup));
    }
    if (needProcessUserIds != null && needProcessUserIds) {
      userIds = new ArrayList<>(new HashSet<>(userIdsDup));
    }

    if (grpIds != null) {
      GrpTagExample grpTagExample = new GrpTagExample();
      grpTagExample.createCriteria().andTagIdEqualTo(tagId);
      if (CollectionUtils.isEmpty(grpIds)) {
        grpTagMapper.deleteByExample(grpTagExample);
      } else {
        List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
        if (!CollectionUtils.isEmpty(grpTagKeys)) {
          ArrayList<Integer> dbGrpIds = new ArrayList<>();
          for (GrpTagKey grpTagKey : grpTagKeys) {
            dbGrpIds.add(grpTagKey.getGrpId());
          }
          @SuppressWarnings("unchecked")
          ArrayList<Integer> intersections = ((ArrayList<Integer>) dbGrpIds.clone());
          intersections.retainAll(grpIds);
          List<Integer> grpIdsNeedAddToDb = new ArrayList<>();
          List<Integer> grpIdsNeedDeleteFromDb = new ArrayList<>();
          for (Integer grpId : grpIds) {
            if (!intersections.contains(grpId)) {
              grpIdsNeedAddToDb.add(grpId);
            }
          }
          for (Integer dbGrpId : dbGrpIds) {
            if (!intersections.contains(dbGrpId)) {
              grpIdsNeedDeleteFromDb.add(dbGrpId);
            }
          }

          if (!CollectionUtils.isEmpty(grpIdsNeedAddToDb)) {
            for (Integer grpIdNeedAddToDb : grpIdsNeedAddToDb) {
              GrpTagKey grpTagKey = new GrpTagKey();
              grpTagKey.setTagId(tagId);
              grpTagKey.setGrpId(grpIdNeedAddToDb);
              grpTagMapper.insert(grpTagKey);
            }
          }
          if (!CollectionUtils.isEmpty(grpIdsNeedDeleteFromDb)) {
            GrpTagExample grpTagDeleteExample = new GrpTagExample();
            grpTagDeleteExample.createCriteria().andTagIdEqualTo(tagId)
                .andGrpIdIn(grpIdsNeedDeleteFromDb);
            grpTagMapper.deleteByExample(grpTagDeleteExample);
          }
        } else {
          for (Integer grpId : grpIds) {
            GrpTagKey grpTagKey = new GrpTagKey();
            grpTagKey.setTagId(tagId);
            grpTagKey.setGrpId(grpId);
            grpTagMapper.insert(grpTagKey);
          }
        }
      }
    }

    if (userIds != null) {
      UserTagExample userTagExample = new UserTagExample();
      userTagExample.createCriteria().andTagIdEqualTo(tagId);
      if (CollectionUtils.isEmpty(userIds)) {
        userTagMapper.deleteByExample(userTagExample);
      } else {
        List<UserTagKey> userTagKeys = userTagMapper.selectByExample(userTagExample);
        if (!CollectionUtils.isEmpty(userTagKeys)) {
          ArrayList<Long> dbUserIds = new ArrayList<>();
          for (UserTagKey userTagKey : userTagKeys) {
            dbUserIds.add(userTagKey.getUserId());
          }
          @SuppressWarnings("unchecked")
          ArrayList<Long> intersections = ((ArrayList<Long>) dbUserIds.clone());
          intersections.retainAll(userIds);
          List<Long> userIdsNeedAddToDb = new ArrayList<>();
          List<Long> userIdsNeedDeleteFromDb = new ArrayList<>();
          for (Long userId : userIds) {
            if (!intersections.contains(userId)) {
              userIdsNeedAddToDb.add(userId);
            }
          }
          for (Long dbUserId : dbUserIds) {
            if (!intersections.contains(dbUserId)) {
              userIdsNeedDeleteFromDb.add(dbUserId);
            }
          }

          if (!CollectionUtils.isEmpty(userIdsNeedAddToDb)) {
            for (Long userIdNeedAddToDb : userIdsNeedAddToDb) {
              UserTagKey userTagKey = new UserTagKey();
              userTagKey.setTagId(tagId);
              userTagKey.setUserId(userIdNeedAddToDb);
              userTagMapper.insert(userTagKey);
            }
          }
          if (!CollectionUtils.isEmpty(userIdsNeedDeleteFromDb)) {
            UserTagExample userTagDeleteExample = new UserTagExample();
            userTagDeleteExample.createCriteria().andTagIdEqualTo(tagId)
                .andUserIdIn(userIdsNeedDeleteFromDb);
            userTagMapper.deleteByExample(userTagDeleteExample);
          }
        } else {
          for (Long userId : userIds) {
            UserTagKey userTagKey = new UserTagKey();
            userTagKey.setTagId(tagId);
            userTagKey.setUserId(userId);
            userTagMapper.insert(userTagKey);
          }
        }
      }
    }
  }

  /**
   * 关联用户和标签.
   */
  @Transactional
  public void relateUsersAndTag(Integer tagId, List<Long> userIds) {
    CheckEmpty.checkEmpty(tagId, "tagId");
    // roleId 必须要存在
    tagDataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, tagId);
    UserTagExample userTagExample = new UserTagExample();
    UserTagExample.Criteria criteria = userTagExample.createCriteria();
    criteria.andTagIdEqualTo(tagId);
    List<UserTagKey> userTagKeys = userTagMapper.selectByExample(userTagExample);
    List<Long> existUserIds = Lists.newArrayList();
    if (!ObjectUtil.collectionIsEmptyOrNull(userTagKeys)) {
      for (UserTagKey utk : userTagKeys) {
        existUserIds.add(utk.getUserId());
      }
    }
    List<Long> insertUserIds = userIds;
    insertUserIds.removeAll(existUserIds);
    for (Long userId : insertUserIds) {
      UserTagKey record = new UserTagKey();
      record.setTagId(tagId);
      record.setUserId(userId);
      userTagMapper.insert(record);
    }
  }
}
