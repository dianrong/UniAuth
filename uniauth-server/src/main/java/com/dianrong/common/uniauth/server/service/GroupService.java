package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Linkage;
import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.entity.TagExample.Criteria;
import com.dianrong.common.uniauth.server.data.entity.ext.UserExt;
import com.dianrong.common.uniauth.server.data.mapper.*;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;
import com.dianrong.common.uniauth.server.mq.v1.UniauthNotify;
import com.dianrong.common.uniauth.server.mq.v1.ninfo.*;
import com.dianrong.common.uniauth.server.service.cache.AttributeExtendCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.*;
import com.dianrong.common.uniauth.server.service.support.AtrributeDefine;
import com.dianrong.common.uniauth.server.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Arc on 14/1/16.
 */
@Service
public class GroupService extends TenancyBasedService {

  @Autowired
  private GrpMapper grpMapper;
  @Autowired
  private GrpPathMapper grpPathMapper;
  @Autowired
  private GrpRoleMapper grpRoleMapper;
  @Autowired
  private UserGrpMapper userGrpMapper;
  @Autowired
  private RoleMapper roleMapper;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserRoleMapper userRoleMapper;
  @Autowired
  private GrpTagMapper grpTagMapper;
  @Autowired
  private UserTagMapper userTagMapper;
  @Autowired
  private TagMapper tagMapper;
  @Autowired
  private TagTypeMapper tagTypeMapper;
  @Autowired
  private GrpExtendValMapper grpExtendValMapper;
  @Autowired
  private UserExtendValMapper userExtendValMapper;

  // Notification
  @Autowired
  private UniauthNotify uniauthNotify;

  // Cache
  @Autowired
  private AttributeExtendCache attributeExtendCache;

  // Another service
  @Autowired
  private GroupInnerService groupInnerService;

  @Autowired
  private RoleInnerService roleInnerService;

  @Autowired
  private TagInnerService tagInnerService;

  @Autowired
  private UserInnerService userInnerService;

  @Autowired
  private GroupProfileInnerService groupProfileInnerService;

  // Data filter
  @Resource(name = "groupDataFilter")
  private DataFilter dataFilter;

  /**
   * 移动组.
   */
  @Transactional
  public void moveGroup(Integer sourceGroup, Integer targetGroup) {
    CheckEmpty.checkEmpty(sourceGroup, "sourceGroupId");
    CheckEmpty.checkEmpty(targetGroup, "targetGroupId");
    if (sourceGroup.equals(targetGroup)) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("group.parameter.targetid.equals.sourceid"));
    }
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, sourceGroup);
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, targetGroup);
    grpPathMapper.moveTreeStepOne(sourceGroup);
    Map<String, Object> moveParam = new HashMap<>();
    moveParam.put("subAncestor", sourceGroup);
    moveParam.put("superDecendant", targetGroup);
    grpPathMapper.moveTreeStepTwo(moveParam);

    // 发送通知
    uniauthNotify
        .notify(new GroupMoveNotifyInfo().setTargetGroupId(targetGroup).setGroupId(sourceGroup));
  }

  /**
   * 更具条件批量查询组信息.
   */
  public PageDto<GroupDto> searchGroup(Byte userGroupType, Long userId, Integer roleId, Integer id,
      List<Integer> groupIds, String name, String code, String description, Byte status,
      Integer tagId, Boolean needTag, Boolean needUser, Boolean needParentId, Integer pageNumber,
      Integer pageSize) {

    if (pageNumber == null || pageSize == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "pageNumber, pageSize"));
    }

    GrpExample grpExample = new GrpExample();
    grpExample.setOrderByClause("create_date desc");
    grpExample.setPageOffSet(pageNumber * pageSize);
    grpExample.setPageSize(pageSize);
    GrpExample.Criteria criteria = grpExample.createCriteria();
    if (!StringUtils.isEmpty(name)) {
      criteria.andNameLike("%" + name + "%");
    }
    if (!StringUtils.isEmpty(description)) {
      criteria.andDescriptionLike("%" + description + "%");
    }
    if (status != null) {
      criteria.andStatusEqualTo(status);
    }

    if (id != null) {
      criteria.andIdEqualTo(id);
    }

    if (code != null) {
      criteria.andCodeEqualTo(code);
    }

    if (!CollectionUtils.isEmpty(groupIds)) {
      criteria.andIdIn(groupIds);
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    // join user table to find group
    if (userId != null) {
      UserGrpExample userGrpExample = new UserGrpExample();
      UserGrpExample.Criteria userGrpExampleCriteria = userGrpExample.createCriteria();
      userGrpExampleCriteria.andUserIdEqualTo(userId);
      if (userGroupType != null) {
        userGrpExampleCriteria.andTypeEqualTo(userGroupType);
      } else {
        userGrpExampleCriteria.andTypeEqualTo(AppConstants.ZERO_BYTE);
      }
      List<UserGrpKey> userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
      List<Integer> grpIds = new ArrayList<>();
      if (!CollectionUtils.isEmpty(userGrpKeys)) {
        for (UserGrpKey userGrpKey : userGrpKeys) {
          grpIds.add(userGrpKey.getGrpId());
        }
      }
      if (grpIds.isEmpty()) {
        return null;
      } else {
        criteria.andIdIn(grpIds);
      }
    }
    if (roleId != null) {
      // query all groupIds which's roleId equals parameter roleId
      GrpRoleExample grpRoleExample = new GrpRoleExample();
      GrpRoleExample.Criteria grpRoleCriteria = grpRoleExample.createCriteria();
      grpRoleCriteria.andRoleIdEqualTo(roleId);
      List<GrpRoleKey> grpRoles = grpRoleMapper.selectByExample(grpRoleExample);
      if (CollectionUtils.isEmpty(grpRoles)) {
        return null;
      }
      List<Integer> rootGrpIds = new ArrayList<Integer>();
      for (GrpRoleKey grpRole : grpRoles) {
        rootGrpIds.add(grpRole.getGrpId());
      }

      // query all sub groupIds from group table
      GrpPathExample selectGrpPathCondtion = new GrpPathExample();
      GrpPathExample.Criteria selectGrpPathCriteria = selectGrpPathCondtion.createCriteria();
      selectGrpPathCriteria.andAncestorIn(rootGrpIds);
      List<GrpPath> selectGrppaths = grpPathMapper.selectByExample(selectGrpPathCondtion);
      if (CollectionUtils.isEmpty(selectGrppaths)) {
        return null;
      }
      List<Integer> grpIds = new ArrayList<Integer>();
      for (GrpPath sgrpPath : selectGrppaths) {
        grpIds.add(sgrpPath.getDescendant());
      }
      criteria.andIdIn(grpIds);
    }
    if (tagId != null) {
      GrpTagExample grpTagExample = new GrpTagExample();
      grpTagExample.createCriteria().andTagIdEqualTo(tagId);
      List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
      if (!CollectionUtils.isEmpty(grpTagKeys)) {
        List<Integer> grpTagKeysGrpIds = new ArrayList<>();
        for (GrpTagKey grpTagKey : grpTagKeys) {
          grpTagKeysGrpIds.add(grpTagKey.getGrpId());
        }
        criteria.andIdIn(grpTagKeysGrpIds);
      } else {
        return null;
      }
    }
    int count = grpMapper.countByExample(grpExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<Grp> grps = grpMapper.selectByExample(grpExample);
    if (!CollectionUtils.isEmpty(grps)) {
      List<GroupDto> groupDtos = new ArrayList<>();
      Map<Integer, GroupDto> groupIdGroupDtoPair = new HashMap<>();
      for (Grp grp : grps) {
        GroupDto groupDto = BeanConverter.convert(grp);
        groupIdGroupDtoPair.put(grp.getId(), groupDto);
        groupDtos.add(groupDto);
      }
      if (needUser != null && needUser) {
        // 1. query all userIds and index them with grpIds
        UserGrpExample userGrpExample = new UserGrpExample();
        userGrpExample.createCriteria()
            .andGrpIdIn(new ArrayList<Integer>(groupIdGroupDtoPair.keySet()));
        List<UserGrpKey> userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
        Map<Long, List<Integer>> userGrpIdsPair = new HashMap<>();
        if (!CollectionUtils.isEmpty(userGrpKeys)) {
          for (UserGrpKey userGrpKey : userGrpKeys) {
            Integer grpId = userGrpKey.getGrpId();
            Long grpUserId = userGrpKey.getUserId();
            List<Integer> grpIds = userGrpIdsPair.get(grpUserId);
            if (grpIds == null) {
              grpIds = new ArrayList<>();
              userGrpIdsPair.put(grpUserId, grpIds);
            }
            grpIds.add(grpId);
          }
          // 2. query all users in the groups
          UserExample userExample = new UserExample();
          userExample.createCriteria().andIdIn(new ArrayList<Long>(userGrpIdsPair.keySet()))
              .andStatusEqualTo(AppConstants.STATUS_ENABLED)
              .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
          List<User> users = userMapper.selectByExample(userExample);
          for (User user : users) {
            UserDto userDto = BeanConverter.convert(user);
            List<Integer> grpIds = userGrpIdsPair.get(user.getId());
            for (Integer grpId : grpIds) {
              GroupDto groupDto = groupIdGroupDtoPair.get(grpId);
              List<UserDto> userDtoList = groupDto.getUsers();
              if (userDtoList == null) {
                userDtoList = new ArrayList<>();
                groupDto.setUsers(userDtoList);
              }
              userDtoList.add(userDto);
            }
          }
        }
      }

      if (needTag != null && needTag) {
        // 1. query all tagIds and index them with grpIds
        GrpTagExample grpTagExample = new GrpTagExample();
        grpTagExample.createCriteria()
            .andGrpIdIn(new ArrayList<Integer>(groupIdGroupDtoPair.keySet()));
        List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
        if (!CollectionUtils.isEmpty(grpTagKeys)) {
          Map<Integer, List<Integer>> tagIdGrpIdsPair = new HashMap<>();
          for (GrpTagKey grpTagKey : grpTagKeys) {
            Integer grpId = grpTagKey.getGrpId();
            Integer grpTagId = grpTagKey.getTagId();
            List<Integer> grpIds = tagIdGrpIdsPair.get(grpTagId);
            if (grpIds == null) {
              grpIds = new ArrayList<>();
              tagIdGrpIdsPair.put(grpTagId, grpIds);
            }
            grpIds.add(grpId);
          }
          // 2. query all tags, convert into dto and index them with tagIds
          TagExample tagExample = new TagExample();
          tagExample.createCriteria().andIdIn(new ArrayList<Integer>(tagIdGrpIdsPair.keySet()))
              .andStatusEqualTo(AppConstants.STATUS_ENABLED)
              .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
          List<Tag> tags = tagMapper.selectByExample(tagExample);
          if (!CollectionUtils.isEmpty(tags)) {
            Map<Integer, TagDto> tagIdTagDtoPair = new HashMap<>();
            List<Integer> tagTypeIds = new ArrayList<>();
            for (Tag tag : tags) {
              tagTypeIds.add(tag.getTagTypeId());
              tagIdTagDtoPair.put(tag.getId(), BeanConverter.convert(tag));
            }
            // 3. query tagTypes info and index them with tagTypeId
            TagTypeExample tagTypeExample = new TagTypeExample();
            tagTypeExample.createCriteria().andIdIn(tagTypeIds)
                .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
            List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
            Map<Integer, String> tagTypeIdTagCodePair = new HashMap<>();
            for (TagType tagType : tagTypes) {
              tagTypeIdTagCodePair.put(tagType.getId(), tagType.getCode());
            }
            Collection<TagDto> tagDtos = tagIdTagDtoPair.values();
            for (TagDto tagDto : tagDtos) {
              // 4. construct tagtype info into tagDto
              String tagTypeCode = tagTypeIdTagCodePair.get(tagDto.getTagTypeId());
              tagDto.setTagTypeCode(tagTypeCode);
              // 5. construct tagDto into groupDto
              List<Integer> grpIds = tagIdGrpIdsPair.get(tagDto.getId());
              for (Integer grpId : grpIds) {
                GroupDto groupDto = groupIdGroupDtoPair.get(grpId);
                List<TagDto> tagDtoList = groupDto.getTags();
                if (tagDtoList == null) {
                  tagDtoList = new ArrayList<>();
                  groupDto.setTags(tagDtoList);
                }
                tagDtoList.add(tagDto);
              }
            }
          }
        }
      }
      if (needParentId != null && needParentId) {
        GrpPathExample grpPathExample = new GrpPathExample();
        grpPathExample.createCriteria()
            .andDescendantIn(new ArrayList<>(groupIdGroupDtoPair.keySet()))
            .andDeepthEqualTo(AppConstants.ONE_BYTE);
        List<GrpPath> grpPaths = grpPathMapper.selectByExample(grpPathExample);
        if (!CollectionUtils.isEmpty(grpPaths)) {
          for (GrpPath grpPath : grpPaths) {
            GroupDto groupDto = groupIdGroupDtoPair.get(grpPath.getDescendant());
            groupDto.setParentId(grpPath.getAncestor());
          }
        }
      }
      return new PageDto<>(pageNumber, pageSize, count, groupDtos);
    } else {
      return null;
    }
  }

  /**
   * 创建组.
   */
  @Transactional
  public GroupDto createDescendantGroup(GroupParam groupParam) {
    Integer targetGroupId = groupParam.getTargetGroupId();
    String groupCode = groupParam.getCode();
    if (targetGroupId == null || groupCode == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "targetGroupId, groupCode"));
    }

    // 父group需要存在
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, targetGroupId);
    // 子group不能存在
    dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_CODE, groupCode);

    Grp grp = BeanConverter.convert(groupParam);
    Date now = new Date();
    grp.setStatus(AppConstants.STATUS_ENABLED);
    grp.setCreateDate(now);
    grp.setLastUpdate(now);
    grp.setTenancyId(tenancyService.getTenancyIdWithCheck());
    grpMapper.insert(grp);
    // 联动添加组的扩展属性值
    Map<String, String> attributes = Maps.newHashMap();
    attributes.put(AtrributeDefine.GROUP_NAME.getAttributeCode(), groupParam.getName());
    attributes.put(AtrributeDefine.GROUP_CODE.getAttributeCode(), groupParam.getCode());
    attributes.put(AtrributeDefine.GROUP_DESCRiPTION.getAttributeCode(),
        groupParam.getDescription());
    groupProfileInnerService.addOrUpdateUserAttributes(grp.getId(), attributes);

    GrpPath grpPath = new GrpPath();
    grpPath.setDeepth(AppConstants.ZERO_BYTE);
    grpPath.setDescendant(grp.getId());
    grpPath.setAncestor(targetGroupId);
    grpPathMapper.insertNewNode(grpPath);
    Integer count = grpMapper.selectNameCountBySameLayerGrpId(grp.getId());
    if (count != null && count > 1) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("group.parameter.name", grp.getName()));
    }

    // 发送通知
    uniauthNotify
        .notify(new GroupAddNotifyInfo().setCode(grp.getCode()).setDescription(grp.getDescription())
            .setName(grp.getName()).setParentGroupId(targetGroupId).setGroupId(grp.getId()));
    return BeanConverter.convert(grp);
  }

  /**
   * 删除组.
   */
  @Transactional
  public GroupDto deleteGroup(Integer groupId) {
    CheckEmpty.checkEmpty(groupId, "groupId");
    Grp grp = grpMapper.selectByPrimaryKey(groupId);
    // 根组不能进行修改
    if (UniauthServerConstant.isRootGrp(grp.getCode())) {
      throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("group.rootgrp.unmodifiable"));
    }

    // 找出子节点，然后设置为失效
    List<Grp> children = queryGroupByAncestor(groupId);
    List<Integer> disableGrpIds = new ArrayList<>(children.size());
    if (!ObjectUtil.collectionIsEmptyOrNull(children)) {
      for (Grp childrenGrp : children) {
        disableGrpIds.add(childrenGrp.getId());
      }
    }
    if (!disableGrpIds.isEmpty()) {
      Grp record = new Grp();
      record.setStatus(AppConstants.STATUS_DISABLED);
      GrpExample deleteExample = new GrpExample();
      GrpExample.Criteria criteria = deleteExample.createCriteria();
      criteria.andIdIn(disableGrpIds);
      grpMapper.updateByExample(record, deleteExample);
    }
    // 发送通知
    uniauthNotify.notify(new BaseGroupNotifyInfo().setGroupId(grp.getId()).setType(NotifyInfoType.GROUP_DISABLE));

    grp.setStatus(AppConstants.STATUS_DISABLED);
    return BeanConverter.convert(grp);
  }


  /**
   * 更新组.
   */
  @Transactional
  public GroupDto updateGroup(Integer groupId, String groupCode, String groupName, Byte status,
      String description) {
    CheckEmpty.checkEmpty(groupId, "groupId");
    Grp grp = grpMapper.selectByPrimaryKey(groupId);
    CheckEmpty.checkEmpty(groupCode, "groupCode");
    if (status != null) {
      ParamCheck.checkStatus(status);
    }
    if (grp == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", groupId, Grp.class.getSimpleName()));
    }

    // 根组不能进行修改，修改只能走数据库修改
    if (UniauthServerConstant.isRootGrp(grp.getCode())) {
      throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("group.rootgrp.unmodifiable"));
    }

    // 启用或者启用状态的修改
    if ((status != null && status == AppConstants.STATUS_ENABLED)
        || (status == null && grp.getStatus() == AppConstants.STATUS_ENABLED)) {
      // 判断是否存在重复的code
      dataFilter.updateFieldCheck(groupId, FieldType.FIELD_TYPE_CODE, groupCode);
    }

    grp.setName(groupName);
    grp.setStatus(status);
    grp.setDescription(description);
    grp.setCode(groupCode);
    grp.setLastUpdate(new Date());
    grpMapper.updateByPrimaryKeySelective(grp);

    // 联动添加组的扩展属性值
    Map<String, String> attributes = Maps.newHashMap();
    attributes.put(AtrributeDefine.GROUP_NAME.getAttributeCode(), groupName);
    attributes.put(AtrributeDefine.GROUP_CODE.getAttributeCode(), groupCode);
    attributes.put(AtrributeDefine.GROUP_DESCRiPTION.getAttributeCode(), description);
    groupProfileInnerService.addOrUpdateUserAttributes(grp.getId(), attributes);

    Integer count = grpMapper.selectNameCountBySameLayerGrpId(grp.getId());
    if (count != null && count > 1) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("group.parameter.name", grp.getName()));
    }

    // cache original status
    Byte originalStatus = grp.getStatus();
    // 发送通知
    if (status != null && !status.equals(originalStatus)) {
      NotifyInfoType type = NotifyInfoType.GROUP_DISABLE;
      if (status.equals(AppConstants.STATUS_ENABLED)) {
        // 启用
        type = NotifyInfoType.GROUP_ENABLE;
      }
      uniauthNotify.notify(new BaseGroupNotifyInfo().setGroupId(grp.getId()).setType(type));
    }

    return BeanConverter.convert(grp);
  }

  /**
   * 添加用户到组.
   */
  @Transactional
  public void addUsersIntoGroup(Integer groupId, List<Long> userIds, Boolean normalMember) {
    if (groupId == null || CollectionUtils.isEmpty(userIds)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "groupId, userIds"));
    }
    UserGrpExample userGrpExample = new UserGrpExample();
    List<UserGrpKey> userGrpKeys;
    if (normalMember == null || normalMember) {
      userGrpExample.createCriteria().andGrpIdEqualTo(groupId)
          .andTypeEqualTo(AppConstants.ZERO_BYTE);
      userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
    } else {
      userGrpExample.createCriteria().andGrpIdEqualTo(groupId)
          .andTypeEqualTo(AppConstants.ONE_BYTE);
      userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
    }
    Set<Long> userIdSet = new HashSet<>();
    if (!CollectionUtils.isEmpty(userGrpKeys)) {
      for (UserGrpKey userGrpKey : userGrpKeys) {
        userIdSet.add(userGrpKey.getUserId());
      }
    }
    for (Long userId : userIds) {
      if (!userIdSet.contains(userId)) {
        UserGrp userGrp = new UserGrp();
        userGrp.setGrpId(groupId);
        userGrp.setUserId(userId);
        if (normalMember == null || normalMember) {
          userGrp.setType((byte) 0);
        } else {
          userGrp.setType((byte) 1);
        }
        userGrpMapper.insert(userGrp);

        // 只处理普通的关系
        if (normalMember == null || normalMember) {
          uniauthNotify.notify(new UsersToGroupNotifyInfo().setGroupId(groupId).setUserId(userId)
              .setType(NotifyInfoType.USERS_TO_GROUP_ADD));
        }
      }
    }
  }

  /**
   * 从组中将用户移除.
   */
  @Transactional
  public void removeUsersFromGroup(List<Linkage<Long, Integer>> userIdGrpIdPairs,
      Boolean normalMember) {
    if (CollectionUtils.isEmpty(userIdGrpIdPairs)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "groupId, userId pair"));
    }

    for (Linkage<Long, Integer> linkage : userIdGrpIdPairs) {
      Long userId = linkage.getEntry1();
      Integer grpId = linkage.getEntry2();
      UserGrpExample userGrpExample = new UserGrpExample();
      userGrpExample.createCriteria().andGrpIdEqualTo(grpId).andUserIdEqualTo(userId);
      if (normalMember == null || normalMember) {
        userGrpExample.createCriteria().andTypeEqualTo(AppConstants.ZERO_BYTE_PRIMITIVE);
      } else {
        userGrpExample.createCriteria().andTypeEqualTo(AppConstants.ONE_BYTE_PRIMITIVE);
      }
      userGrpMapper.deleteByExample(userGrpExample);

      // 通知 处理普通关系进行通知
      if (normalMember == null || normalMember) {
        uniauthNotify.notify(new UsersToGroupNotifyInfo().setGroupId(grpId).setUserId(userId)
            .setType(NotifyInfoType.USERS_TO_GROUP_REMOVE));
      }
    }
  }

  /**
   * 在组之间移动用户.
   */
  @Transactional
  public void moveUser(Integer targetGroupId, List<Linkage<Long, Integer>> userIdGrpIdPairs,
      Boolean normalMember) {
    if (targetGroupId == null || CollectionUtils.isEmpty(userIdGrpIdPairs)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "groupId, userId pair"));
    }
    UserGrpExample userGrpExample = new UserGrpExample();
    List<UserGrpKey> userGrpKeys;
    Byte type =
        normalMember == null || normalMember ? AppConstants.ZERO_BYTE : AppConstants.ONE_BYTE;
    userGrpExample.createCriteria().andGrpIdEqualTo(targetGroupId).andTypeEqualTo(type);

    // 查询目标分组的用户，用以后面检测用户是否已经存在该组下
    userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
    Set<Long> userIdSet = new HashSet<>();
    if (!CollectionUtils.isEmpty(userGrpKeys)) {
      for (UserGrpKey userGrpKey : userGrpKeys) {
        userIdSet.add(userGrpKey.getUserId());
      }
    }

    // 移动用户到新组
    for (Linkage<Long, Integer> linkage : userIdGrpIdPairs) {
      Long userId = linkage.getEntry1();
      Integer grpId = linkage.getEntry2();

      if (!userIdSet.contains(userId)) {
        UserGrpExample deleteExample = new UserGrpExample();
        deleteExample.createCriteria().andGrpIdEqualTo(grpId).andTypeEqualTo(type)
            .andUserIdEqualTo(userId);

        // 删除原组里的该用户
        userGrpMapper.deleteByExample(deleteExample);

        UserGrp userGrp = new UserGrp();
        userGrp.setGrpId(targetGroupId);
        userGrp.setUserId(userId);
        userGrp.setType(type);
        // 将该用户加入到新组
        userGrpMapper.insert(userGrp);

        // 通知 处理普通关系进行通知
        if (normalMember == null || normalMember) {
          uniauthNotify.notify(new UsersToGroupExchangeNotifyInfo().setTargetGroupId(targetGroupId)
              .setGroupId(grpId).setUserId(userId));
        }
      }
    }
  }

  /**
   * 保存角色信息到组.
   */
  @Transactional
  public void saveRolesToGroup(Integer groupId, List<Integer> roleIds) {
    if (groupId == null || CollectionUtils.isEmpty(roleIds)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "groupId, roleIds"));
    }
    GrpRoleExample grpRoleExample = new GrpRoleExample();
    grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
    List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
    Set<Integer> roleIdSet = new TreeSet<>();
    if (!CollectionUtils.isEmpty(grpRoleKeys)) {
      for (GrpRoleKey grpRoleKey : grpRoleKeys) {
        roleIdSet.add(grpRoleKey.getRoleId());
      }
    }
    for (Integer roleId : roleIds) {
      if (!roleIdSet.contains(roleId)) {
        GrpRoleKey grpRoleKey = new GrpRoleKey();
        grpRoleKey.setGrpId(groupId);
        grpRoleKey.setRoleId(roleId);
        grpRoleMapper.insert(grpRoleKey);
      }
    }
  }

  /**
   * 获取所有的组,域的角色信息.
   */
  public List<RoleDto> getAllRolesToGroupAndDomain(Integer groupId, Integer domainId) {
    if (groupId == null || domainId == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "groupId, domainId"));
    }
    if (grpMapper.selectByPrimaryKey(groupId) == null) {
      return null;
    }
    GrpRoleExample grpRoleExample = new GrpRoleExample();
    grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
    List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
    Set<Integer> checkedRoleIds = new HashSet<>();
    if (!CollectionUtils.isEmpty(grpRoleKeys)) {
      for (GrpRoleKey grpRoleKey : grpRoleKeys) {
        checkedRoleIds.add(grpRoleKey.getRoleId());
      }
    }
    RoleExample roleExample = new RoleExample();
    roleExample.createCriteria().andDomainIdEqualTo(domainId)
        .andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<Role> roles = roleMapper.selectByExample(roleExample);
    if (!CollectionUtils.isEmpty(roles)) {
      List<RoleDto> roleDtos = new ArrayList<>();
      for (Role role : roles) {
        if (role.getStatus().equals(AppConstants.ONE_BYTE)) {
          continue;
        }
        RoleDto roleDto = BeanConverter.convert(role);
        if (checkedRoleIds.contains(roleDto.getId())) {
          roleDto.setChecked(Boolean.TRUE);
        } else {
          roleDto.setChecked(Boolean.FALSE);
        }
        roleDtos.add(roleDto);
      }
      return roleDtos;
    } else {
      return null;
    }
  }

  /**
   * 根据组Id获取组的Owners.
   */
  public List<UserDto> getGroupOwners(Integer groupId) {
    List<User> users = userMapper.getGroupOwners(groupId);
    if (!CollectionUtils.isEmpty(users)) {
      List<UserDto> userDtos = new ArrayList<>();
      for (User user : users) {
        if (user.getStatus().equals(AppConstants.ONE_BYTE)) {
          continue;
        }
        UserDto userDto = BeanConverter.convert(user);
        userDtos.add(userDto);
      }
      return userDtos;
    } else {
      return null;
    }
  }

  /**
   * 根据组Id或者组code获取组信息.
   */
  public List<GroupDto> getGroupTreeByIdOrCode(Integer groupId, String groupCode,
      Byte userGroupType, Byte userStatus) {
    Grp rootGrp = null;
    if (groupId != null) {
      rootGrp = grpMapper.selectByPrimaryKey(groupId);
      if (rootGrp == null || !AppConstants.ZERO_BYTE.equals(rootGrp.getStatus())) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.entity.not found", groupId, Grp.class.getSimpleName()));
      }
    } else if (groupCode != null) {
      GrpExample grpExample = new GrpExample();
      grpExample.createCriteria().andCodeEqualTo(groupCode)
          .andStatusEqualTo(AppConstants.STATUS_ENABLED)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      List<Grp> grps = grpMapper.selectByExample(grpExample);
      if (CollectionUtils.isEmpty(grps)) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.entity.code.not found", groupCode, Grp.class.getSimpleName()));
      }
      rootGrp = grps.get(0);
    } else if (groupCode == null && groupId == null) {
      throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.not found",
          groupId, groupCode, Grp.class.getSimpleName()));
    }

    List<Grp> grps = grpMapper.getGroupTree(rootGrp.getId());
    List<GroupDto> groupDtos = new ArrayList<>();

    Set<Integer> groupIds = new HashSet<>();
    if (!CollectionUtils.isEmpty(grps)) {
      for (Grp grp : grps) {
        GroupDto groupDto = BeanConverter.convert(grp);
        groupIds.add(grp.getId());
        groupDtos.add(groupDto);
      }
    }

    if (userGroupType == null) {
      userGroupType = AppConstants.ZERO_TYPE;
    }
    UserGrpExample userGrpExample = new UserGrpExample();
    userGrpExample.createCriteria().andTypeEqualTo(userGroupType)
        .andGrpIdIn(new ArrayList<Integer>(groupIds));
    List<UserGrpKey> userGrpKeys = userGrpMapper.selectByExample(userGrpExample);

    // grpId作为key，userId集合作为value
    Map<Integer, List<Long>> userGrpIdsPair = new HashMap<>();
    ArrayList<Long> userIdList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(userGrpKeys)) {
      for (UserGrpKey userGrpKey : userGrpKeys) {
        Long userId = userGrpKey.getUserId();
        userIdList.add(userId);
        Integer grpId = userGrpKey.getGrpId();
        List<Long> userIds = userGrpIdsPair.get(grpId);
        if (userIds == null) {
          userIds = new ArrayList<>();
          userGrpIdsPair.put(grpId, userIds);
        }
        userIds.add(userId);
      }

      if (userStatus == null) {
        userStatus = AppConstants.STATUS_ENABLED;
      }
      // 查询组里所有的用户
      UserExample userExample = new UserExample();
      userExample.createCriteria().andIdIn(userIdList).andStatusEqualTo(userStatus);
      List<User> users = userMapper.selectByExample(userExample);

      Map<Long, UserDto> userDtoMap = new HashMap<>();
      if (!CollectionUtils.isEmpty(users)) {
        for (User user : users) {
          UserDto userDto = BeanConverter.convert(user);
          userDtoMap.put(userDto.getId(), userDto);
        }
      }

      // 将用户组装成用户集合到组里
      for (GroupDto groupDto : groupDtos) {
        List<Long> userIds = userGrpIdsPair.get(groupDto.getId());
        List<UserDto> userDtoList = groupDto.getUsers();
        if (!CollectionUtils.isEmpty(userIds)) {
          for (Long userId : userIds) {
            UserDto userDto = userDtoMap.get(userId);
            if (userDtoList == null) {
              userDtoList = new ArrayList<>();
              groupDto.setUsers(userDtoList);
            }
            userDtoList.add(userDto);
          }
        }
      }
    }

    return groupDtos;
  }

  /**
   * 获取组信息,以Tree的形式返回.
   */
  public GroupDto getGroupTree(Integer groupId, String groupCode, Boolean onlyShowGroup,
      Byte userGroupType, Integer roleId, Integer tagId, Boolean needOwnerMarkup, Long ownerId,
      Boolean includeDisableUser) {
    Grp rootGrp;
    if (groupCode == null && (groupId == null || Integer.valueOf(-1).equals(groupId))) {
      GrpExample grpExample = new GrpExample();
      grpExample.createCriteria().andCodeEqualTo(AppConstants.GRP_ROOT)
          .andStatusEqualTo(AppConstants.STATUS_ENABLED)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      rootGrp = grpMapper.selectByExample(grpExample).get(0);
    } else if (groupCode != null && groupId != null) {
      GrpExample grpExample = new GrpExample();
      grpExample.createCriteria().andCodeEqualTo(groupCode)
          .andStatusEqualTo(AppConstants.STATUS_ENABLED)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      List<Grp> grps = grpMapper.selectByExample(grpExample);
      Grp grp = grpMapper.selectByPrimaryKey(groupId);
      if (grp == null) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.entity.notfound", groupId, Grp.class.getSimpleName()));
      }
      if (CollectionUtils.isEmpty(grps)) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.entity.code.notfound", groupCode, Grp.class.getSimpleName()));
      }
      if (!grp.getId().equals(grps.get(0).getId())) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("group.parameter.code.id.dif", groupCode, groupId));
      }
      rootGrp = grp;
    } else if (groupCode != null && groupId == null) {
      GrpExample grpExample = new GrpExample();
      grpExample.createCriteria().andCodeEqualTo(groupCode)
          .andStatusEqualTo(AppConstants.STATUS_ENABLED)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      List<Grp> grps = grpMapper.selectByExample(grpExample);
      if (CollectionUtils.isEmpty(grps)) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.entity.code.notfound", groupCode, Grp.class.getSimpleName()));
      }
      rootGrp = grps.get(0);
    } else {
      // else if(groupCode == null && groupId != null)
      rootGrp = grpMapper.selectByPrimaryKey(groupId);
      if (rootGrp == null || !AppConstants.ZERO_BYTE.equals(rootGrp.getStatus())) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.entity.notfound", groupId, Grp.class.getSimpleName()));
      }
    }
    Integer realGroupId = rootGrp.getId();
    List<Grp> grps = grpMapper.getGroupTree(realGroupId);
    if (!CollectionUtils.isEmpty(grps)) {
      Set<Integer> ownGrpIds = null;
      if (needOwnerMarkup != null && needOwnerMarkup && ownerId != null) {
        ownGrpIds = grpMapper.getOwnGrpIds(ownerId);
      }
      List<HashMap<String, Integer>> descendantAncestorPairs =
          grpMapper.getGroupTreeLinks(realGroupId);
      Map<Integer, GroupDto> idGroupDtoPair = new HashMap<Integer, GroupDto>();
      for (Grp grp : grps) {
        GroupDto groupDto = new GroupDto().setId(grp.getId()).setCode(grp.getCode())
            .setName(grp.getName()).setDescription(grp.getDescription());
        // mark root group unmodifiable
        if (UniauthServerConstant.isRootGrp(groupDto.getCode())) {
          groupDto.setIsRootGrp(Boolean.TRUE);
        }
        if (!CollectionUtils.isEmpty(ownGrpIds)) {
          if (ownGrpIds.contains(grp.getId())) {
            groupDto.setOwnerMarkup(Boolean.TRUE);
          }
        }
        idGroupDtoPair.put(groupDto.getId(), groupDto);
      }

      // construct the tree
      if (!CollectionUtils.isEmpty(descendantAncestorPairs)) {
        for (HashMap<String, Integer> descendantAncestorPair : descendantAncestorPairs) {
          Integer descendantId = descendantAncestorPair.get("descendant");
          Integer ancestorId = descendantAncestorPair.get("ancestor");
          GroupDto ancestorDto = idGroupDtoPair.get(ancestorId);
          GroupDto descendantDto = idGroupDtoPair.get(descendantId);
          if (ancestorDto != null) {
            List<GroupDto> groupDtos = ancestorDto.getGroups();
            if (groupDtos == null && descendantDto != null) {
              groupDtos = new ArrayList<>();
              ancestorDto.setGroups(groupDtos);
            }
            if (descendantDto != null) {
              groupDtos.add(descendantDto);
            }
          }
        }
      }

      if (roleId != null) {
        // role checked on group
        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andRoleIdEqualTo(roleId)
            .andGrpIdIn(new ArrayList<Integer>(idGroupDtoPair.keySet()));
        List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
        if (!CollectionUtils.isEmpty(grpRoleKeys)) {
          for (GrpRoleKey grpRoleKey : grpRoleKeys) {
            Integer checkedGroupId = grpRoleKey.getGrpId();
            idGroupDtoPair.get(checkedGroupId).setRoleChecked(Boolean.TRUE);
          }
        }
      }

      if (tagId != null) {
        GrpTagExample grpTagExample = new GrpTagExample();
        grpTagExample.createCriteria().andTagIdEqualTo(tagId)
            .andGrpIdIn(new ArrayList<Integer>(idGroupDtoPair.keySet()));
        List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
        if (!CollectionUtils.isEmpty(grpTagKeys)) {
          for (GrpTagKey grpTagKey : grpTagKeys) {
            Integer checkedGroupId = grpTagKey.getGrpId();
            idGroupDtoPair.get(checkedGroupId).setTagChecked(Boolean.TRUE);
          }
        }
      }

      if (onlyShowGroup != null && !onlyShowGroup) {
        CheckEmpty.checkEmpty(userGroupType, "user's type in the group");
        Map<String, Object> groupIdAndUserType = new HashMap<String, Object>();
        groupIdAndUserType.put("id", realGroupId);
        groupIdAndUserType.put("userGroupType", userGroupType);
        groupIdAndUserType.put("includeDisableUser", includeDisableUser);
        List<UserExt> userExts = userMapper.getUsersByParentGrpIdByUserType(groupIdAndUserType);
        // construct the users on the tree
        if (!CollectionUtils.isEmpty(userExts)) {
          // role checked on users
          Set<Long> userIdsOnRole = null;
          if (roleId != null) {
            UserRoleExample userRoleExample = new UserRoleExample();
            userRoleExample.createCriteria().andRoleIdEqualTo(roleId);
            List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
            if (!CollectionUtils.isEmpty(userRoleKeys)) {
              userIdsOnRole = new TreeSet<>();
              for (UserRoleKey userRoleKey : userRoleKeys) {
                userIdsOnRole.add(userRoleKey.getUserId());
              }
            }
          }
          // tag checked on users
          Set<Long> userIdsOnTag = null;
          if (tagId != null) {
            UserTagExample userTagExample = new UserTagExample();
            userTagExample.createCriteria().andTagIdEqualTo(tagId);
            List<UserTagKey> userTagKeys = userTagMapper.selectByExample(userTagExample);
            if (!CollectionUtils.isEmpty(userTagKeys)) {
              userIdsOnTag = new TreeSet<>();
              for (UserTagKey userTagKey : userTagKeys) {
                userIdsOnTag.add(userTagKey.getUserId());
              }
            }
          }
          for (UserExt userExt : userExts) {
            UserDto userDto = new UserDto().setEmail(userExt.getEmail()).setId(userExt.getId())
                .setUserGroupType(userExt.getUserGroupType()).setName(userExt.getName())
                .setPhone(userExt.getPhone()).setCreateDate(userExt.getCreateDate())
                .setStatus(userExt.getStatus());
            GroupDto groupDto = idGroupDtoPair.get(userExt.getGroupId());
            if (groupDto != null) {
              List<UserDto> userDtos = groupDto.getUsers();
              if (userDtos == null) {
                userDtos = new ArrayList<>();
                groupDto.setUsers(userDtos);
              }
              userDtos.add(userDto);
              if (userIdsOnRole != null && userIdsOnRole.contains(userDto.getId())) {
                userDto.setRoleChecked(Boolean.TRUE);
              }
              if (userIdsOnTag != null && userIdsOnTag.contains(userDto.getId())) {
                userDto.setTagChecked(Boolean.TRUE);
              }
            }
          }
        }
      }
      return idGroupDtoPair.get(realGroupId);
    } else {
      return null;
    }
  }

  /**
   * 判断用户是否是某个组的Owner.
   */
  public void checkOwner(Long opUserId, List<Integer> targetGroupIds) {
    CheckEmpty.checkEmpty(opUserId, "当前用户");
    CheckEmpty.checkEmpty(targetGroupIds, "添加的目标组(s)");

    for (Integer targetGroupId : targetGroupIds) {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("userId", opUserId);
      paramMap.put("targetGroupId", targetGroupId);

      Integer ownerGroupCount = grpMapper.checkOwner(paramMap);
      if (ownerGroupCount == 0) {
        throw new AppException(InfoName.GRP_NOT_OWNER,
            UniBundle.getMsg("group.checkowner.not.owner", opUserId, targetGroupId));
      }
    }
  }

  /**
   * 替换组的角色信息.
   */
  @Transactional
  public void replaceRolesToGroupUnderDomain(Integer grpId, List<Integer> roleIds,
      Integer domainId) {

    CheckEmpty.checkEmpty(grpId, "grpId");
    CheckEmpty.checkEmpty(domainId, "domainId");
    // step 1. get roleIds in the specific domain.
    RoleExample roleExample = new RoleExample();
    roleExample.createCriteria().andDomainIdEqualTo(domainId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<Role> roles = roleMapper.selectByExample(roleExample);
    List<Integer> roleIdsInDomain = new ArrayList<>();
    if (!CollectionUtils.isEmpty(roles)) {
      for (Role role : roles) {
        roleIdsInDomain.add(role.getId());
      }
    }
    // Not null, otherwise it is an invalid call.
    CheckEmpty.checkEmpty(roleIdsInDomain, "roleIdsInDomain");

    GrpRoleExample grpRoleExample = new GrpRoleExample();
    GrpRoleExample.Criteria criteria = grpRoleExample.createCriteria();
    criteria.andGrpIdEqualTo(grpId).andRoleIdIn(roleIdsInDomain);
    // no roleIds means delete all the links of the role to group under the domain
    if (CollectionUtils.isEmpty(roleIds)) {
      grpRoleMapper.deleteByExample(grpRoleExample);
      return;
    }
    // if the input roleIds is not under the domain, then it is an invalid call
    if (!roleIdsInDomain.containsAll(roleIds)) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.ids.invalid", roleIds));
    }

    // step 2. get the roleIds in the specific domain and linked to group
    List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
    if (!CollectionUtils.isEmpty(grpRoleKeys)) {
      ArrayList<Integer> dbRoleIds = new ArrayList<>();
      for (GrpRoleKey grpRoleKey : grpRoleKeys) {
        dbRoleIds.add(grpRoleKey.getRoleId());
      }
      @SuppressWarnings("unchecked")
      ArrayList<Integer> intersections = ((ArrayList<Integer>) dbRoleIds.clone());
      // step 3. get the intersection of param roleIds and dbRoleIds.
      intersections.retainAll(roleIds);
      List<Integer> roleIdsNeedAddToDb = new ArrayList<>();
      List<Integer> roleIdsNeedDeleteFromDb = new ArrayList<>();
      // step 4. get the roleIds need to add
      for (Integer roleId : roleIds) {
        if (!intersections.contains(roleId)) {
          roleIdsNeedAddToDb.add(roleId);
        }
      }
      // step 5. get the roleIds need to delete
      for (Integer dbRoleId : dbRoleIds) {
        if (!intersections.contains(dbRoleId)) {
          roleIdsNeedDeleteFromDb.add(dbRoleId);
        }
      }
      // step 6. add and delete them.
      if (!CollectionUtils.isEmpty(roleIdsNeedAddToDb)) {
        for (Integer roleIdNeedAddToDb : roleIdsNeedAddToDb) {
          GrpRoleKey grpRoleKey = new GrpRoleKey();
          grpRoleKey.setRoleId(roleIdNeedAddToDb);
          grpRoleKey.setGrpId(grpId);
          grpRoleMapper.insert(grpRoleKey);
        }
      }
      if (!CollectionUtils.isEmpty(roleIdsNeedDeleteFromDb)) {
        GrpRoleExample grpRoleDeleteExample = new GrpRoleExample();
        grpRoleDeleteExample.createCriteria().andGrpIdEqualTo(grpId)
            .andRoleIdIn(roleIdsNeedDeleteFromDb);
        grpRoleMapper.deleteByExample(grpRoleDeleteExample);
      }
    } else {
      for (Integer roleId : roleIds) {
        GrpRoleKey grpRoleKey = new GrpRoleKey();
        grpRoleKey.setRoleId(roleId);
        grpRoleKey.setGrpId(grpId);
        grpRoleMapper.insert(grpRoleKey);
      }
    }
  }


  /**
   * 获取所有的tags，并且根据groupId打上对应的checked标签.
   *
   * @param groupId 组id
   * @param domainId 域idd
   */
  public List<TagDto> searchTagsWithrChecked(Integer groupId, Integer domainId) {
    CheckEmpty.checkEmpty(groupId, "groupId");
    CheckEmpty.checkEmpty(domainId, "domainId");

    // 获取tagType信息
    TagTypeExample tagTypeExample = new TagTypeExample();
    // 添加查询条件
    tagTypeExample.createCriteria().andDomainIdEqualTo(domainId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
    if (tagTypes == null || tagTypes.isEmpty()) {
      return new ArrayList<TagDto>();
    }
    Map<Integer, TagType> tagTypeIdMap = new HashMap<Integer, TagType>();
    if (!CollectionUtils.isEmpty(tagTypes)) {
      for (TagType tagType : tagTypes) {
        tagTypeIdMap.put(tagType.getId(), tagType);
      }
    }

    // 查询组和tag的关联关系信息
    GrpTagExample grpTagExample = new GrpTagExample();
    grpTagExample.createCriteria().andGrpIdEqualTo(groupId);
    List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
    List<TagDto> tagDtos = new ArrayList<TagDto>();

    Set<Integer> tagIdLinkedToGrp = new HashSet<Integer>();
    if (!CollectionUtils.isEmpty(grpTagKeys)) {
      for (GrpTagKey grpTagKey : grpTagKeys) {
        tagIdLinkedToGrp.add(grpTagKey.getTagId());
      }
    }

    // 查询tag信息
    TagExample tagConditon = new TagExample();
    Criteria andStatusEqualTo = tagConditon.createCriteria();
    andStatusEqualTo.andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());

    // 加入domainId的限制
    andStatusEqualTo.andTagTypeIdIn(new ArrayList<Integer>(tagTypeIdMap.keySet()));
    List<Tag> allTags = tagMapper.selectByExample(tagConditon);

    // 优化
    if (ObjectUtil.collectionIsEmptyOrNull(allTags)) {
      return new ArrayList<>();
    }

    for (Tag tag : allTags) {
      TagDto tagDto = BeanConverter.convert(tag);
      if (tagIdLinkedToGrp.contains(tagDto.getId())) {
        tagDto.setTagGrouprChecked(Boolean.TRUE);
      }

      if (tagTypeIdMap.get(tagDto.getTagTypeId()) != null) {
        tagDto.setTagTypeCode(tagTypeIdMap.get(tagDto.getTagTypeId()).getCode());
      } else {
        tagDto.setTagTypeCode("UNKNOW");
      }
      tagDtos.add(tagDto);
    }
    return tagDtos;
  }

  /**
   * 替换组的Tag信息.
   */
  @Transactional
  public void replaceTagsToGroup(Integer grpId, List<Integer> tagIds) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    // step 1. delete all relationship
    GrpTagExample delCondtion = new GrpTagExample();
    delCondtion.createCriteria().andGrpIdEqualTo(grpId);
    grpTagMapper.deleteByExample(delCondtion);

    // step2 .batch insert relationship
    if (tagIds == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.empty", "tagIds"));
    }

    if (tagIds.isEmpty()) {
      return;
    }

    List<GrpTagKey> infoes = new ArrayList<GrpTagKey>();
    for (Integer tagId : tagIds) {
      infoes.add(new GrpTagKey().setGrpId(grpId).setTagId(tagId));
    }
    grpTagMapper.bacthInsert(infoes);
  }

  public List<Grp> queryGroupByAncestor(Integer id) {
    return grpMapper.getGroupTree(id);
  }

  /**
   * Check if the user witch's id is userId is in group or in the sub group.
   *
   * @param userId can not be null
   * @param code can not be null
   * @param includeOwner if group-user owner relationship is include
   * @return true or false
   * @throws AppException the input parameter is null
   */
  public boolean isUserInGroupOrSub(Long userId, String code, Boolean includeOwner) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(code, "groupCode");
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("userId", userId);
    paramMap.put("includeOwner", includeOwner);
    paramMap.put("code", code);
    paramMap.put("tenancyId", tenancyService.getTenancyIdWithCheck());
    Integer count = grpMapper.getUserIdInGroupOrSub(paramMap);
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 根据用户id查询关联的组的信息集合.
   *
   * @param userId 用户id 不能为空
   * @param includeOwner 是否包含owner的从属关系(默认为false)
   * @param includeIndirectAncestors 是否查询非直属的关系
   * @return 符合条件的组信息列表
   */
  public List<GroupDto> listGroupsRelateToUser(Long userId, Boolean includeOwner,
      Boolean includeIndirectAncestors) {
    CheckEmpty.checkEmpty(userId, "userId");
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("userId", userId);
    paramMap.put("includeOwner", includeOwner);
    paramMap.put("includeIndirectAncestors", includeIndirectAncestors);
    List<Grp> grps = grpMapper.listGroupsRelateToUser(paramMap);
    List<GroupDto> groupDtos = Lists.newArrayList();
    if (grps != null) {
      for (Grp grp : grps) {
        groupDtos.add(BeanConverter.convert(grp));
      }
    }
    return groupDtos;
  }

  /**
   * 获取用户最新关联的组的结构.
   *
   * @param userId 用户id, 不能为空
   * @return 组信息列表. 以从根组到子组的顺序排序
   */
  public List<Grp> listUserLastGrpPath(Long userId) {
    return groupInnerService.listUserLastGrpPath(userId);
  }

  /**
   * 获取组的各种关联信息.
   */
  public PageDto<GroupDto> queryTotalInfo(Integer id, String code, Integer domainId,
      Boolean needGrpRole, Boolean needGrpExtendVal, Boolean needGrpTag, Boolean needGrpUser,
      Boolean includeDisableUser, Boolean needGrpUserRole, Boolean needGrpUserTag,
      Boolean needGrpUserExtendVal, Integer pageNumber, Integer pageSize) {
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");

    Integer grpId = id;
    if (grpId == null) {
      CheckEmpty.checkEmpty(code, "code");
      // 根据编码Code来确定查询的根组id
      GrpExample selectExample = new GrpExample();
      GrpExample.Criteria criteria = selectExample.createCriteria();
      criteria.andCodeEqualTo(code).andStatusEqualTo(AppConstants.STATUS_ENABLED)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      List<Grp> grps = grpMapper.selectByExample(selectExample);
      if (ObjectUtil.collectionIsEmptyOrNull(grps)) {
        return PageDto.emptyPageDto(GroupDto.class);
      }
      grpId = grps.get(0).getId();
    }
    // 查询所有的组的基本信息
    Map<String, Object> paramMap = Maps.newHashMap();
    paramMap.put("grpId", grpId);
    paramMap.put("pageSize", pageSize);
    paramMap.put("pageOffSet", pageNumber * pageSize);
    List<Grp> grps = grpMapper.queryPageGroup(paramMap);
    if (ObjectUtil.collectionIsEmptyOrNull(grps)) {
      return PageDto.emptyPageDto(GroupDto.class);
    }

    // 获取组id和组信息的映射
    Map<Integer, GroupDto> grpMap = Maps.newHashMap();
    for (Grp grp : grps) {
      grpMap.put(grp.getId(), BeanConverter.convert(grp));
    }

    // 构造树关系
    GroupDto rootGrp = grpMap.get(grpId);
    if (rootGrp == null) {
      return PageDto.emptyPageDto(GroupDto.class);
    }

    // 查询组与组的关联关系
    List<HashMap<String, Integer>> groupMaps = grpMapper.queryPageGroupTreeLinks(paramMap);
    Map<Integer, Set<Integer>> groupMapList = Maps.newHashMap();

    // 组织父组和子组的关联关系
    if (groupMaps != null) {
      for (HashMap<String, Integer> hm : groupMaps) {
        Integer ancestor = hm.get("ancestor");
        Integer descendant = hm.get("descendant");
        Set<Integer> descendants = groupMapList.get(ancestor);
        if (descendants == null) {
          descendants = Sets.newHashSet();
          groupMapList.put(ancestor, descendants);
        }
        descendants.add(descendant);
      }
    }
    // 构造树形结构
    constructGrpTree(rootGrp, groupMapList, grpMap);

    if (needGrpRole != null && needGrpRole) {
      CheckEmpty.checkEmpty(domainId, "domainId");
      Map<Integer, List<RoleDto>> grpRoleMap =
          roleInnerService.queryGrpRole(new ArrayList<>(grpMap.keySet()), domainId);
      for (GroupDto grp : grpMap.values()) {
        grp.setRoles(grpRoleMap.get(grp.getId()));
      }
    }

    if (needGrpExtendVal != null && needGrpExtendVal) {
      GrpExtendValExample example = new GrpExtendValExample();
      GrpExtendValExample.Criteria criteria = example.createCriteria();
      criteria.andGrpIdIn(new ArrayList<>(grpMap.keySet()));
      List<GrpExtendVal> grpExtendValList = grpExtendValMapper.selectByExample(example);
      if (!ObjectUtil.collectionIsEmptyOrNull(grpExtendValList)) {
        for (GrpExtendVal gev : grpExtendValList) {
          Integer gevgid = gev.getGrpId();
          GroupDto grpDto = grpMap.get(gevgid);
          if (grpDto == null) {
            continue;
          }
          List<GrpExtendValDto> grpExtendValDtoList = grpDto.getGrpExtendVals();
          if (grpExtendValDtoList == null) {
            grpExtendValDtoList = Lists.newArrayList();
            grpDto.setGrpExtendVals(grpExtendValDtoList);
          }

          GrpExtendValDto gevDto = BeanConverter.convert(gev);
          AttributeExtendDto attributeExtendDto =
              attributeExtendCache.getById(gevDto.getExtendId());
          if (attributeExtendDto != null) {
            gevDto.setExtendCode(attributeExtendDto.getCode());
            gevDto.setExtendDescription(attributeExtendDto.getDescription());
          }
          grpExtendValDtoList.add(gevDto);
        }
      }
    }
    
    if (needGrpTag != null && needGrpTag) {
      CheckEmpty.checkEmpty(domainId, "domainId");
      Map<Integer, List<TagDto>> grpTagMap =
          tagInnerService.queryGrpTag(new ArrayList<>(grpMap.keySet()), domainId);
      for (GroupDto grp : grpMap.values()) {
        grp.setTags(grpTagMap.get(grp.getId()));
      }
    }

    if (needGrpUser != null && needGrpUser) {
      Map<Integer, List<UserDto>> grpUserMap = userInnerService.queryGrpUser(
          new ArrayList<>(grpMap.keySet()), includeDisableUser, AppConstants.ZERO_TYPE);
      for (GroupDto grp : grpMap.values()) {
        grp.setUsers(grpUserMap.get(grp.getId()));
      }

      // 收集所有需要关心的用户的id
      Map<Long, UserDto> userMap = Maps.newHashMap();
      for (Entry<Integer, List<UserDto>> entry : grpUserMap.entrySet()) {
        List<UserDto> userDtos = entry.getValue();
        if (!ObjectUtil.collectionIsEmptyOrNull(userDtos)) {
          for (UserDto udto : userDtos) {
            userMap.put(udto.getId(), udto);
          }
        }
      }

      // 获取关联用户的角色信息
      if (needGrpUserRole != null && needGrpUserRole) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        Map<Long, List<RoleDto>> userRoleMap =
            roleInnerService.queryUserRole(new ArrayList<>(userMap.keySet()), domainId);
        for (UserDto user : userMap.values()) {
          user.setRoles(userRoleMap.get(user.getId()));
        }
      }

      // 获取关联用户的标签信息
      if (needGrpUserTag != null && needGrpUserTag) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        Map<Long, List<TagDto>> userTagMap =
            tagInnerService.queryUserTag(new ArrayList<>(userMap.keySet()), domainId);
        for (UserDto user : userMap.values()) {
          user.setTagDtos(userTagMap.get(user.getId()));
        }
      }

      // 获取关联用户的扩展信息
      if (needGrpUserExtendVal != null && needGrpUserExtendVal) {
        UserExtendValExample example = new UserExtendValExample();
        UserExtendValExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdIn(new ArrayList<>(userMap.keySet()));
        List<UserExtendVal> userExtendValList = userExtendValMapper.selectByExample(example);
        if (!ObjectUtil.collectionIsEmptyOrNull(userExtendValList)) {
          for (UserExtendVal uev : userExtendValList) {
            Long uevuid = uev.getUserId();
            UserDto userDto = userMap.get(uevuid);
            if (userDto == null) {
              continue;
            }
            List<UserExtendValDto> userExtendValDtoList = userDto.getUserExtendValDtos();
            if (userExtendValDtoList == null) {
              userExtendValDtoList = Lists.newArrayList();
              userDto.setUserExtendValDtos(userExtendValDtoList);
            }

            UserExtendValDto uevDto = BeanConverter.convert(uev);
            AttributeExtendDto attributeExtendDto =
                attributeExtendCache.getById(uevDto.getExtendId());
            if (attributeExtendDto != null) {
              uevDto.setExtendCode(attributeExtendDto.getCode());
              uevDto.setExtendDescription(attributeExtendDto.getDescription());
            }
            userExtendValDtoList.add(uevDto);
          }
        }
      }
    }

    // 总数
    int totalCount = grpMapper.queryPageGroupCount(grpId);
    return new PageDto<>(pageNumber, pageSize, totalCount, Arrays.asList(rootGrp));
  }

  /**
   * 构造树形结构.
   */
  private void constructGrpTree(GroupDto rootGrp, Map<Integer, Set<Integer>> groupMapList,
      Map<Integer, GroupDto> grpMap) {
    if (rootGrp == null) {
      return;
    }
    Set<Integer> descendantIds = groupMapList.get(rootGrp.getId());
    if (!ObjectUtil.collectionIsEmptyOrNull(descendantIds)) {
      for (Integer did : descendantIds) {
        GroupDto gdto = grpMap.get(did);
        if (gdto == null) {
          continue;
        }
        constructGrpTree(gdto, groupMapList, grpMap);
        List<GroupDto> descendantDtos = rootGrp.getGroups();
        if (descendantDtos == null) {
          descendantDtos = Lists.newArrayList();
          rootGrp.setGroups(descendantDtos);
        }
        descendantDtos.add(gdto);
      }
    }
  }
}
