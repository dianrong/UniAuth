package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.AuditDto;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.VPNLoginResult;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.ApiPermission;
import com.dianrong.common.uniauth.server.data.entity.Audit;
import com.dianrong.common.uniauth.server.data.entity.Cfg;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.ext.PermissionExt;
import com.dianrong.common.uniauth.server.data.entity.ext.RoleExt;
import com.dianrong.common.uniauth.server.data.entity.ext.UrlRoleMappingExt;
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
          .setLastLoginIp(user.getLastLoginIp()).setCreateDate(user.getCreateDate())
          .setStatus(user.getStatus()).setLastUpdate(user.getLastUpdate())
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
}
