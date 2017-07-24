package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.AuditDto;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionPathDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailInfoDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.UserWorkRelationshipDto;
import com.dianrong.common.uniauth.common.bean.dto.VPNLoginResult;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendParam;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.ApiPermission;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.Audit;
import com.dianrong.common.uniauth.server.data.entity.Cfg;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinition;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionPath;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserDetail;
import com.dianrong.common.uniauth.server.data.entity.UserWorkRelationship;
import com.dianrong.common.uniauth.server.data.entity.ext.PermissionExt;
import com.dianrong.common.uniauth.server.data.entity.ext.RoleExt;
import com.dianrong.common.uniauth.server.data.entity.ext.UrlRoleMappingExt;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.StringUtils;

/**
 * Created by Arc on 15/1/16.
 */
@SuppressWarnings("deprecation")
public class BeanConverter {

  /**
   * 将Entity转化为Dto.
   */
  public static TagDto convert(Tag tag) {
    if (tag == null) {
      return null;
    } else {
      return new TagDto().setCode(tag.getCode()).setCreateDate(tag.getCreateDate())
          .setId(tag.getId()).setLastUpdate(tag.getLastUpdate()).setStatus(tag.getStatus())
          .setTagTypeId(tag.getTagTypeId()).setDescription(tag.getDescription());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static TagTypeDto convert(TagType tagType) {
    if (tagType == null) {
      return null;
    } else {
      return new TagTypeDto().setCode(tagType.getCode()).setDomainId(tagType.getDomainId())
          .setId(tagType.getId());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static RoleCodeDto convert(RoleCode roleCode) {
    if (roleCode == null) {
      return null;
    } else {
      return new RoleCodeDto().setCode(roleCode.getCode()).setId(roleCode.getId())
          .setDescription(roleCode.getDescription());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static ConfigDto convert(Cfg cfg) {
    if (cfg == null) {
      return null;
    } else {
      return new ConfigDto().setId(cfg.getId()).setCfgKey(cfg.getCfgKey())
          .setCfgTypeId(cfg.getCfgTypeId()).setFile(cfg.getFile()).setValue(cfg.getValue());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static DomainDto convert(Domain domain) {
    if (domain == null) {
      return null;
    } else {
      return new DomainDto().setCode(domain.getCode()).setCreateDate(domain.getCreateDate())
          .setDescription(domain.getDescription()).setDisplayName(domain.getDisplayName())
          .setId(domain.getId()).setLastUpdate(domain.getLastUpdate())
          .setStatus(domain.getStatus());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static Domain convert(DomainParam domainParam) {
    if (domainParam == null) {
      return null;
    } else {
      Domain domain = new Domain();
      domain.setCode(domainParam.getCode());
      domain.setDescription(domainParam.getDescription());
      domain.setDisplayName(domainParam.getDisplayName());
      domain.setId(domainParam.getId());
      domain.setStatus(domainParam.getStatus());
      return domain;
    }
  }

  /**
   * 将Param转化为Entity.
   */
  public static Grp convert(GroupParam groupParam) {
    if (groupParam == null) {
      return null;
    } else {
      Grp grp = new Grp();
      grp.setDescription(groupParam.getDescription());
      grp.setCode(groupParam.getCode());
      grp.setId(groupParam.getId());
      grp.setName(groupParam.getName());
      grp.setStatus(groupParam.getStatus());
      return grp;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static GroupDto convert(Grp grp) {
    if (grp == null) {
      return null;
    } else {
      GroupDto groupDto = new GroupDto();
      groupDto.setCode(grp.getCode());
      groupDto.setId(grp.getId());
      groupDto.setName(grp.getName());
      groupDto.setDescription(grp.getDescription());
      groupDto.setStatus(grp.getStatus());
      groupDto.setCreateDate(grp.getCreateDate());
      groupDto.setLastUpdate(grp.getLastUpdate());
      return groupDto;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static StakeholderDto convert(Stakeholder stakeholder) {
    if (stakeholder == null) {
      return null;
    } else {
      return new StakeholderDto().setDomainId(stakeholder.getDomainId())
          .setEmail(stakeholder.getEmail()).setId(stakeholder.getId())
          .setJobtitle(stakeholder.getJobtitle()).setName(stakeholder.getName())
          .setPhone(stakeholder.getPhone());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static RoleDto convert(Role role) {
    if (role == null) {
      return null;
    } else {
      RoleDto roleDto = new RoleDto();
      roleDto.setDescription(role.getDescription()).setId(role.getId()).setStatus(role.getStatus())
          .setName(role.getName()).setRoleCodeId(role.getRoleCodeId())
          .setDomainId(role.getDomainId());
      return roleDto;
    }
  }

  /**
   * 将Param转化为Entity.
   */
  public static Stakeholder convert(StakeholderParam stakeholderParam, boolean needSetId) {
    if (stakeholderParam == null) {
      return null;
    } else {
      Stakeholder stakeholder = new Stakeholder();
      if (needSetId) {
        stakeholder.setId(stakeholderParam.getId());
      }
      stakeholder.setDomainId(stakeholderParam.getDomainId());
      stakeholder.setEmail(stakeholderParam.getEmail());
      stakeholder.setJobtitle(stakeholderParam.getJobtitle());
      stakeholder.setName(stakeholderParam.getName());
      stakeholder.setPhone(stakeholderParam.getPhone());
      return stakeholder;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static UserDto convert(User user) {
    if (user == null) {
      return null;
    } else {
      UserDto userDto = new UserDto();
      userDto.setName(user.getName()).setEmail(user.getEmail()).setId(user.getId())
          .setPhone(user.getPhone()).setLastLoginTime(user.getLastLoginTime())
          .setFailCount(user.getFailCount()).setLastLoginIp(user.getLastLoginIp())
          .setCreateDate(user.getCreateDate()).setStatus(user.getStatus())
          .setLastUpdate(user.getLastUpdate())
          .setTenancyId(StringUtil.translateLongToInteger(user.getTenancyId()));
      return userDto;
    }
  }

  /**
   * 将UserDto转化为VPNLoginResult.
   */
  public static VPNLoginResult convert(UserDto user) {
    if (user == null) {
      return null;
    } else {
      VPNLoginResult result = new VPNLoginResult();
      result.setMobileNum(user.getPhone());
      result.setTenancyCode(user.getTenancyCode());
      result.setTenancyId(user.getTenancyId());
      result.setUserName(StringUtils.hasText(user.getName()) ? user.getName()
          : StringUtils.hasText(user.getEmail()) ? user.getEmail()
              : StringUtils.hasText(user.getPhone()) ? user.getPhone() : null);
      result.setUserNote(result.getUserName());
      return result;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static PermTypeDto convert(PermType pt) {
    if (pt == null) {
      return null;
    } else {
      return new PermTypeDto().setId(pt.getId()).setType(pt.getType());
    }
  }

  /**
   * 将Param转化为Entity.
   */
  public static Permission convert(PermissionParam permissionParam, boolean needSetId) {
    if (permissionParam == null) {
      return null;
    } else {
      Permission permission = new Permission();
      if (needSetId) {
        permission.setId(permissionParam.getId());
      }
      permission.setDescription(permissionParam.getDescription());
      permission.setDomainId(permissionParam.getDomainId());
      permission.setPermTypeId(permissionParam.getPermTypeId());
      permission.setStatus(permissionParam.getStatus());
      permission.setValue(permissionParam.getValue());
      permission.setValueExt(permissionParam.getValueExt());
      return permission;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static PermissionDto convert(Permission permission) {
    if (permission == null) {
      return null;
    } else {
      return new PermissionDto().setDescription(permission.getDescription())
          .setDomainId(permission.getDomainId()).setId(permission.getId())
          .setPermTypeId(permission.getPermTypeId()).setStatus(permission.getStatus())
          .setValue(permission.getValue()).setValueExt(permission.getValueExt());
    }
  }


  /**
   * 将Entity转化为Dto.
   */
  public static RoleDto convert(RoleExt roleExt) {
    if (roleExt == null) {
      return null;
    } else {
      RoleDto roleDto = new RoleDto();
      roleDto.setDescription(roleExt.getDescription()).setId(roleExt.getId())
          .setStatus(roleExt.getStatus()).setName(roleExt.getName())
          .setRoleCodeId(roleExt.getRoleCodeId()).setDomainId(roleExt.getDomainId());

      roleDto.setRoleCodeId(roleExt.getRoleCodeId());
      roleDto.setRoleCode(roleExt.getRoleCode());

      roleDto.setPermissionId(roleExt.getPermissionId());
      return roleDto;
    }
  }

  /**
   * 将Query转化为Entity.
   */
  public static PermissionExt convert(PermissionQuery permissionQuery) {
    if (permissionQuery == null) {
      return null;
    } else {
      PermissionExt permissionExt = new PermissionExt();
      permissionExt.setDomainId(permissionQuery.getDomainId());
      permissionExt.setStatus(permissionQuery.getStatus());
      permissionExt.setValue(permissionQuery.getValue());
      permissionExt.setPermTypeId(permissionQuery.getPermTypeId());
      permissionExt.setValueExt(permissionQuery.getValueExt());
      return permissionExt;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static UrlRoleMappingDto convert(UrlRoleMappingExt urlRoleMappingExt) {
    if (urlRoleMappingExt == null) {
      return null;
    } else {
      UrlRoleMappingDto urlRoleMappingDto = new UrlRoleMappingDto();
      urlRoleMappingDto.setPermType(urlRoleMappingExt.getPermType());
      urlRoleMappingDto.setPermUrl(urlRoleMappingExt.getPermUrl());
      urlRoleMappingDto.setRoleCode(urlRoleMappingExt.getRoleCode());
      urlRoleMappingDto.setHttpMethod(urlRoleMappingExt.getHttpMethod());
      urlRoleMappingDto.setTenancyId(urlRoleMappingExt.getTenancyId());

      return urlRoleMappingDto;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static AuditDto convert(Audit audit) {
    if (audit == null) {
      return null;
    } else {
      AuditDto auditDto = new AuditDto().setDomainId(audit.getDomainId())
          .setReqElapse(audit.getReqElapse()).setReqClass(audit.getReqClass())
          .setReqException(audit.getReqExp()).setReqMethod(audit.getReqMethod())
          .setReqSuccess(audit.getReqSuccess()).setReqUuid(audit.getReqUuid())
          .setReqResult(audit.getReqResult()).setReqIp(audit.getReqIp())
          .setReqUrl(audit.getReqUrl()).setReqParam(audit.getReqParam())
          .setRequestDate(audit.getReqDate()).setReqClass(audit.getReqClass())
          .setUserId(audit.getUserId()).setId(audit.getId()).setReqSequence(audit.getReqSeq());
      return auditDto;
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static TenancyDto convert(Tenancy tenancy) {
    if (tenancy == null) {
      return null;
    } else {
      return new TenancyDto().setCode(tenancy.getCode()).setContactName(tenancy.getContactName())
          .setCreateDate(tenancy.getCreateDate()).setDescription(tenancy.getDescription())
          .setId(tenancy.getId()).setLastUpdate(tenancy.getLastUpdate()).setName(tenancy.getName())
          .setPhone(tenancy.getPhone()).setStatus(tenancy.getStatus());
    }
  }

  /**
   * 将Entity转化为Dto.
   */
  public static ApiPermissionDto convert(ApiPermission apiPermission) {
    if (apiPermission == null) {
      return null;
    } else {
      return new ApiPermissionDto().setId(apiPermission.getId())
          .setMethod(apiPermission.getMethod()).setStatus(apiPermission.getStatus())
          .setType(apiPermission.getType()).setUri(apiPermission.getUri());
    }
  }

  public static <T> T convert(Object srcObject, Class<T> toValueType) {
    return JasonUtil.o2o(srcObject, toValueType);
  }

  /**
   * 将Entity转化为Dto.
   */
  public static UserDto convert(com.dianrong.common.uniauth.server.ldap.ipa.entity.User user) {
    if (user == null) {
      return null;
    } else {
      UserDto userDto = new UserDto();
      userDto.setAccount(user.getUid()).setName(user.getCn()).setEmail(user.getEmail())
          .setPhone(user.getPhone()).setStatus(AppConstants.STATUS_ENABLED);
      return userDto;
    }
  }

  /**
   * 从AttributeExtendDto转换为UserExtendDto.
   */
  public static UserExtendDto convert(AttributeExtendDto attributeExtend) {
    if (attributeExtend == null) {
      return null;
    } else {
      UserExtendDto userExtendDto = new UserExtendDto();
      userExtendDto.setCode(attributeExtend.getCode())
          .setDescription(attributeExtend.getDescription()).setId(attributeExtend.getId())
          .setTenancyCode(attributeExtend.getTenancyCode())
          .setTenancyId(attributeExtend.getTenancyId());
      return userExtendDto;
    }
  }

  /**
   * 从ProfileDefinition转换为UserExtendDto.
   */
  public static ProfileDefinitionDto convert(ProfileDefinition profileDefinition) {
    return convert(profileDefinition, null, null, null);
  }

  /**
   * 转换为ProfileDefinitionDto.
   */
  public static ProfileDefinitionDto convert(ProfileDefinition profileDefinition,
      Map<String, AttributeExtendDto> attributes, Set<Long> descendantProfileIds,
      Set<SimpleProfileDefinitionDto> subProfiles) {
    ProfileDefinitionDto profileDefinitionDto = new ProfileDefinitionDto();
    if (profileDefinition != null) {
      profileDefinitionDto.setCode(profileDefinition.getCode())
          .setDescription(profileDefinition.getDescription()).setId(profileDefinition.getId())
          .setName(profileDefinition.getName())
          .setTenancyId(StringUtil.translateLongToInteger(profileDefinition.getTenancyId()));
    }
    profileDefinitionDto.setAttributes(attributes).setDescendantProfileIds(descendantProfileIds)
        .setSubProfiles(subProfiles);
    return profileDefinitionDto;
  }

  /**
   * 从ProfileDefinitionPath转换为ProfileDefinitionPathDto.
   */
  public static ProfileDefinitionPathDto convert(ProfileDefinitionPath profileDefinitionPath) {
    if (profileDefinitionPath == null) {
      return null;
    }
    ProfileDefinitionPathDto profileDefinitionPathDto = new ProfileDefinitionPathDto();
    profileDefinitionPathDto.setAncestor(profileDefinitionPath.getAncestor())
        .setCreateDate(profileDefinitionPath.getCreateDate())
        .setDescendant(profileDefinitionPath.getDescendant())
        .setLastUpdate(profileDefinitionPath.getLastUpdate());
    return profileDefinitionPathDto;
  }

  /**
   * 从AttributeExtend转换为ProfileDefinitionAttribute.
   */
  public static ProfileDefinitionAttribute convert(Long profileId,
      AttributeExtend attributeExtend) {
    if (attributeExtend == null) {
      return null;
    }
    Assert.notNull(profileId);
    ProfileDefinitionAttribute item = new ProfileDefinitionAttribute();
    item.setExtendId(attributeExtend.getId());
    item.setProfileId(profileId);
    item.setCreateDate(new Date());
    item.setLastUpdate(new Date());
    return item;
  }

  /**
   * 从ProfileDefinitionPath转换为ProfileDefinitionPathDto.
   */
  public static List<ProfileDefinitionAttribute> convert(Long profileId,
      List<AttributeExtend> attributeExtends) {
    if (attributeExtends == null) {
      return null;
    }
    List<ProfileDefinitionAttribute> results = Lists.newArrayList();
    for (AttributeExtend ae : attributeExtends) {
      results.add(convert(profileId, ae));
    }
    return results;
  }

  /**
   * 从AttributeExtendParam转换为AttributeValModel.
   */
  public static AttributeValModel convert(AttributeExtendParam attributeExtendParam) {
    if (attributeExtendParam == null) {
      return null;
    }
    AttributeValModel model = new AttributeValModel();
    model.setCategory(attributeExtendParam.getCategory());
    model.setCode(attributeExtendParam.getCode());
    model.setDescription(attributeExtendParam.getDescription());
    model.setValue(attributeExtendParam.getValue());
    model.setSubcategory(attributeExtendParam.getSubcategory());
    model.setId(attributeExtendParam.getId());
    return model;
  }

  /**
   * 对象转换.
   */
  public static AttributeValModel convert(AttributeExtend attributeExtend) {
    if (attributeExtend == null) {
      return null;
    }
    AttributeValModel model = new AttributeValModel();
    model.setCategory(attributeExtend.getCategory());
    model.setCode(attributeExtend.getCode());
    model.setDescription(attributeExtend.getDescription());
    model.setSubcategory(attributeExtend.getSubcategory());
    model.setId(attributeExtend.getId());
    return model;
  }

  /**
   * 从AttributeValModel转换为AttributeExtendDto.
   */
  public static AttributeExtendDto convert(AttributeValModel attributeValModel) {
    if (attributeValModel == null) {
      return null;
    }
    AttributeExtendDto attributeExtendDto = new AttributeExtendDto();
    attributeExtendDto.setCategory(attributeValModel.getCategory());
    attributeExtendDto.setCode(attributeValModel.getCode());
    attributeExtendDto.setDescription(attributeValModel.getDescription());
    attributeExtendDto.setValue(attributeValModel.getValue());
    attributeExtendDto.setSubcategory(attributeValModel.getSubcategory());
    attributeExtendDto.setId(attributeValModel.getId());
    return attributeExtendDto;
  }

  /**
   * 对象转换.
   */
  public static UserDetailInfoDto convert(UserDetail userDetail) {
    if (userDetail == null) {
      return null;
    }
    UserDetailInfoDto userDetailDto = new UserDetailInfoDto();
    userDetailDto.setAddress(userDetail.getAddress()).setAid(userDetail.getAid())
        .setBirthday(userDetail.getBirthday()).setCreateDate(userDetail.getCreateDate())
        .setDepartment(userDetail.getDepartment()).setDisplayName(userDetail.getDisplayName())
        .setEntryDate(userDetail.getEntryDate()).setFirstName(userDetail.getFirstName())
        .setGender(userDetail.getGender()).setId(userDetail.getId())
        .setIdentityNo(userDetail.getIdentityNo()).setImage(userDetail.getImage())
        .setLastName(userDetail.getLastName())
        .setLastPositionModifyDate(userDetail.getLastPositionModifyDate())
        .setLastUpdate(userDetail.getLastUpdate()).setLeaveDate(userDetail.getLeaveDate())
        .setMotto(userDetail.getMotto()).setNickName(userDetail.getNickName())
        .setPosition(userDetail.getPosition()).setRemark(userDetail.getRemark())
        .setSsn(userDetail.getSsn()).setTitle(userDetail.getTitle())
        .setUserId(userDetail.getUserId()).setWechatNo(userDetail.getWechatNo())
        .setWeibo(userDetail.getWeibo())
        .setTenancyId(StringUtil.translateLongToInteger(userDetail.getTenancyId()));
    return userDetailDto;
  }

  /**
   * 对象转换.
   */
  public static UserDetail convert(UserDetailInfoDto userDetailDto) {
    if (userDetailDto == null) {
      return null;
    }
    UserDetail userDetail = new UserDetail();
    userDetail.setAddress(userDetailDto.getAddress());
    userDetail.setAid(userDetailDto.getAid());
    userDetail.setBirthday(userDetailDto.getBirthday());
    userDetail.setDepartment(userDetailDto.getDepartment());
    userDetail.setDisplayName(userDetailDto.getDisplayName());
    userDetail.setEntryDate(userDetailDto.getEntryDate());
    userDetail.setFirstName(userDetailDto.getFirstName());
    userDetail.setGender(userDetailDto.getGender());
    userDetail.setIdentityNo(userDetailDto.getIdentityNo());
    userDetail.setImage(userDetailDto.getImage());
    userDetail.setLastName(userDetailDto.getLastName());
    userDetail.setLastPositionModifyDate(userDetailDto.getLastPositionModifyDate());
    userDetail.setLeaveDate(userDetailDto.getLeaveDate());
    userDetail.setMotto(userDetailDto.getMotto());
    userDetail.setNickName(userDetailDto.getNickName());
    userDetail.setPosition(userDetailDto.getPosition());
    userDetail.setRemark(userDetailDto.getRemark());
    userDetail.setSsn(userDetailDto.getSsn());
    userDetail.setTitle(userDetailDto.getTitle());
    userDetail.setUserId(userDetailDto.getUserId());
    userDetail.setWechatNo(userDetailDto.getWechatNo());
    userDetail.setWeibo(userDetailDto.getWeibo());
    userDetail.setTenancyId(StringUtil.translateIntegerToLong(userDetailDto.getTenancyId()));
    return userDetail;
  }

  /**
   * 对象转换.
   */
  public static UserWorkRelationshipDto convert(UserWorkRelationship userWorkRelationship) {
    if (userWorkRelationship == null) {
      return null;
    }
    UserWorkRelationshipDto userWorkRelationshipDto = new UserWorkRelationshipDto();
    userWorkRelationshipDto.setAssignmentDate(userWorkRelationship.getAssignmentDate())
        .setBusinessUnitName(userWorkRelationship.getBusinessUnitName())
        .setCreateDate(userWorkRelationship.getCreateDate())
        .setDepartmentName(userWorkRelationship.getDepartmentName())
        .setHireDate(userWorkRelationship.getHireDate()).setId(userWorkRelationship.getId())
        .setLastUpdate(userWorkRelationship.getLastUpdate())
        .setLegalEntityName(userWorkRelationship.getLegalEntityName())
        .setManagerId(userWorkRelationship.getManagerId())
        .setSupervisorId(userWorkRelationship.getSupervisorId())
        .setType(userWorkRelationship.getType()).setUserId(userWorkRelationship.getUserId())
        .setWorkAddress(userWorkRelationship.getWorkAddress())
        .setWorkLocation(userWorkRelationship.getWorkLocation())
        .setWorkPhone(userWorkRelationship.getWorkPhone())
        .setTenancyId(StringUtil.translateLongToInteger(userWorkRelationship.getTenancyId()));
    return userWorkRelationshipDto;
  }
  

  /**
   * 对象转换.
   */
  public static Map<String, AttributeExtendDto> convertToDto(
      Map<String, AttributeValModel> attributes) {
    if (attributes == null) {
      return null;
    }
    Map<String, AttributeExtendDto> resultMap = Maps.newHashMap();
    for (Entry<String, AttributeValModel> entry : attributes.entrySet()) {
      resultMap.put(entry.getKey(), convert(entry.getValue()));
    }
    return resultMap;
  }
  
  /**
   * 对象转换.
   */
  public static Map<String, AttributeValModel> convertToModel(
      Map<String, AttributeExtendParam> attributes) {
    if (attributes == null) {
      return null;
    }
    Map<String, AttributeValModel> resultMap = Maps.newHashMap();
    for (Entry<String, AttributeExtendParam> entry : attributes.entrySet()) {
      resultMap.put(entry.getKey(), convert(entry.getValue()));
    }
    return resultMap;
  }
}
