package com.dianrong.common.techops.sscustom;

import com.dianrong.common.techops.bean.PermType;
import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Linkage;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.OrganizationParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.common.bean.request.TagParam;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.common.bean.request.TagTypeParam;
import com.dianrong.common.uniauth.common.bean.request.TagTypeQuery;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.UserType;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

public class TechOpsPermissionEvaluator extends UniauthPermissionEvaluatorImpl {

  private static final Logger LOGGER = LoggerFactory.getLogger(TechOpsPermissionEvaluator.class);

  @Autowired
  private UniClientFacade uniClientFacade;

  private Set<PermissionCheck> permissionChecks;

  private AtomicBoolean hasInit = new AtomicBoolean(false);

  public TechOpsPermissionEvaluator() {
    this.permissionChecks = new HashSet<>();
  }

  @PostConstruct
  public void springInit() {
    init();
  }

  public void init() {
    // 保护初始化,只执行一次
    if (hasInit.compareAndSet(false, true)) {
      Class innerClazz[] = this.getClass().getDeclaredClasses();
      for (Class cls : innerClazz) {
        if (PermissionCheck.class.isAssignableFrom(cls) && !Modifier.isInterface(cls.getModifiers())
            && !Modifier.isAbstract(cls.getModifiers())) {
          try {
            // 获取默认构造函数
            // 注意: 非静态内部类的构造函数是需要在构造的时候将外部class作为第一个参数传入的
            Constructor constructor = cls.getDeclaredConstructor(new Class<?>[]{this.getClass()});
            constructor.setAccessible(true);
            // 将实现加入到实现列表中
            this.permissionChecks
                .add((PermissionCheck) constructor.newInstance(new Object[]{this}));
          } catch (Exception e) {
            LOGGER.warn("Init failed, failed instance" + cls.getName(), e);
            throw new UniauthCommonException("Init failed, failed instance" + cls.getName(), e);
          }
        }
      }
    }
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetObject,
      Object permission) {
    String perm = (String) permission;
    PermissionCheck supportedPermissionCheck = null;
    for (PermissionCheck permissionCheck : permissionChecks) {
      if (permissionCheck.supportCheck(perm)) {
        supportedPermissionCheck = permissionCheck;
        break;
      }
    }
    if (supportedPermissionCheck != null) {
      try {
        return supportedPermissionCheck.hasPermission(authentication, targetObject, permission);
      } catch (NotSupportedException e) {
        LOGGER.debug(supportedPermissionCheck + " not support perm:" + perm);
      }
    }
    return super.hasPermission(authentication, targetObject, permission);
  }

  /**
   * 一个标识权限Check不支持的异常类型类型.
   */
  private class NotSupportedException extends UniauthCommonException {

  }


  // 定义各种类型权限Check的实现
  private interface PermissionCheck {

    /**
     * Check权限.
     *
     * @return 是否有权限执行操作.
     * @throws NotSupportedException 不支持的权限判断.
     */
    boolean hasPermission(Authentication authentication, Object targetObject, Object permission)
        throws NotSupportedException;

    /**
     * 判断是否支持指定的权限判断.
     */
    boolean supportCheck(String permType);
  }


  private class PermUserIdCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) {
      if (targetObject instanceof UserParam) {
        UserParam userParam = (UserParam) targetObject;
        Long beOperatedUserId = userParam.getId();
        UserDetailDto userDetailDto = uniClientFacade.getUserResource()
            .getUserDetailInfoByUid(new UserParam().setId(beOperatedUserId)).getData();
        List<DomainDto> domainDtoList = userDetailDto.getDomainList();
        if (!CollectionUtils.isEmpty(domainDtoList)) {
          for (DomainDto domainDto : domainDtoList) {
            if (AppConstants.DOMAIN_CODE_TECHOPS.equalsIgnoreCase(domainDto.getCode())) {
              List<RoleDto> roleList = domainDto.getRoleList();
              if (!CollectionUtils.isEmpty(roleList)) {
                for (RoleDto roleDto : roleList) {
                  Map<String, Set<String>> permMap = roleDto.getPermMap();
                  if (permMap != null) {
                    Set<String> domainPerms = permMap.get(AppConstants.PERM_TYPE_DOMAIN);
                    if (domainPerms != null) {
                      if (AppConstants.ROLE_SUPER_ADMIN.equals(roleDto.getRoleCode()) && domainPerms
                          .contains(AppConstants.DOMAIN_CODE_TECHOPS)) {
                        return false;
                      }
                    }
                  }
                }
              }
            }
          }
        }
        return true;
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_USERID_CHECK.equals(permType);
    }
  }


  private class PermGroupOwner implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) {
      GroupParam groupParam = null;
      if (targetObject instanceof GroupParam) {
        groupParam = (GroupParam) targetObject;
      }
      if (targetObject instanceof UserListParam) {
        UserListParam userListParam = (UserListParam) targetObject;
        List<Linkage<Long, Integer>> userIdGroupIdPairs = userListParam.getUserIdGroupIdPairs();
        List<Integer> targetGroupIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userIdGroupIdPairs)) {
          for (Linkage<Long, Integer> userIdGroupIdPair : userIdGroupIdPairs) {
            targetGroupIds.add(userIdGroupIdPair.getEntry2());
          }
        }
        groupParam = new GroupParam().setTargetGroupId(userListParam.getGroupId())
            .setTargetGroupIds(targetGroupIds);
      }
      if (targetObject instanceof Integer) {
        groupParam = new GroupParam().setTargetGroupId((Integer) targetObject);
      }
      if (groupParam != null) {
        Response<Void> response = uniClientFacade.getGroupResource().checkOwner(groupParam);
        List<Info> infoList = response.getInfo();
        if (CollectionUtils.isEmpty(infoList)) {
          return true;
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_GROUP_OWNER.equals(permType);
    }
  }


  private class PermOrganizationOwner implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      OrganizationParam organizationParam = null;
      if (targetObject instanceof OrganizationParam) {
        organizationParam = (OrganizationParam) targetObject;
      }
      if (targetObject instanceof UserListParam) {
        UserListParam userListParam = (UserListParam) targetObject;
        List<Linkage<Long, Integer>> userIdGroupIdPairs = userListParam.getUserIdGroupIdPairs();
        List<Integer> targetGroupIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userIdGroupIdPairs)) {
          for (Linkage<Long, Integer> userIdGroupIdPair : userIdGroupIdPairs) {
            targetGroupIds.add(userIdGroupIdPair.getEntry2());
          }
        }
        organizationParam =
            (OrganizationParam) new OrganizationParam().setTargetGroupId(userListParam.getGroupId())
                .setTargetGroupIds(targetGroupIds);
      }
      if (targetObject instanceof Integer) {
        organizationParam =
            (OrganizationParam) new OrganizationParam().setTargetGroupId((Integer) targetObject);
      }
      if (organizationParam != null) {
        Response<Void> response =
            uniClientFacade.getOrganizationResource().checkOwner(organizationParam);
        List<Info> infoList = response.getInfo();
        if (CollectionUtils.isEmpty(infoList)) {
          return true;
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_ORGANIZATION_OWNER.equals(permType);
    }
  }


  private class PermSystemUserEditCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      if (targetObject instanceof UserParam) {
        UserParam userParam = (UserParam) targetObject;
        Long userId = userParam.getId();
        boolean needCheck = false;
        // Add
        if (userId == null) {
          if (UserType.SYSTEM.equals(userParam.getType())) {
            needCheck = true;
          }
        } else {
          // Update
          UserQuery userQuery = new UserQuery();
          userQuery.setUserId(userId);
          Response<PageDto<UserDto>> response =
              uniClientFacade.getUserResource().searchUser(userQuery);
          List<Info> infoList = response.getInfo();
          if (CollectionUtils.isEmpty(infoList)) {
            List<UserDto> userList = response.getData().getData();
            if (!CollectionUtils.isEmpty(userList)) {
              UserDto userDto = userList.get(0);
              if (UserType.SYSTEM.equals(userDto.getType())) {
                needCheck = true;
              }
            }
          }
        }

        if (needCheck) {
          TechOpsUserExtInfo techOpsUserExtInfo =
              (TechOpsUserExtInfo) authentication.getPrincipal();
          Set<String> domainCodes =
              techOpsUserExtInfo.getPermMap().get(AppConstants.PERM_TYPE_DOMAIN);
          if (!CollectionUtils.isEmpty(domainCodes)) {
            return domainCodes.contains(AppConstants.DOMAIN_CODE_TECHOPS);
          }
          return false;
        }
        return true;
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_SYSTEM_USER_EDIT_CHECK.equals(permType);
    }
  }


  private class PermRoleIdCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      Integer roleId = null;
      if (targetObject instanceof Integer) {
        roleId = (Integer) targetObject;
      }
      if (targetObject instanceof RoleParam) {
        roleId = ((RoleParam) targetObject).getId();
      }
      if (roleId == null) {
        return true;
      }
      RoleQuery roleQuery = new RoleQuery();
      roleQuery.setId(roleId);
      Response<PageDto<RoleDto>> pageDtoResponse =
          uniClientFacade.getRoleResource().searchRole(roleQuery);
      List<Info> infoList = pageDtoResponse.getInfo();
      if (CollectionUtils.isEmpty(infoList)) {
        PageDto<RoleDto> roleDtoPageDto = pageDtoResponse.getData();
        if (roleDtoPageDto != null && !CollectionUtils.isEmpty(roleDtoPageDto.getData())) {
          Integer domainId = roleDtoPageDto.getData().get(0).getDomainId();
          if (queryDomainIdSet(authentication).contains(domainId)) {
            return true;
          }
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_ROLEID_CHECK.equals(permType);
    }
  }


  private class PermRoleIdsCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      List<Integer> roleIds = null;
      if (targetObject instanceof GroupParam) {
        roleIds = ((GroupParam) targetObject).getRoleIds();
      }
      if (targetObject instanceof UserParam) {
        roleIds = ((UserParam) targetObject).getRoleIds();
      }
      if (targetObject instanceof PermissionParam) {
        roleIds = ((PermissionParam) targetObject).getRoleIds();
      }
      if (CollectionUtils.isEmpty(roleIds)) {
        return true;
      }
      RoleQuery roleQuery = new RoleQuery();
      roleQuery.setRoleIds(roleIds);
      Response<PageDto<RoleDto>> pageRoleDtoResponse =
          uniClientFacade.getRoleResource().searchRole(roleQuery);
      List<Info> infoList = pageRoleDtoResponse.getInfo();
      if (CollectionUtils.isEmpty(infoList)) {
        PageDto<RoleDto> roleDtoPageDto = pageRoleDtoResponse.getData();
        if (roleDtoPageDto != null && !CollectionUtils.isEmpty(roleDtoPageDto.getData())) {
          for (RoleDto roleDto : roleDtoPageDto.getData()) {
            if (!queryDomainIdSet(authentication).contains(roleDto.getDomainId())) {
              return false;
            }
          }
          return true;
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_ROLEIDS_CHECK.equals(permType);
    }
  }


  private class PermPermIdCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      PermissionParam permissionParam = (PermissionParam) targetObject;
      Integer permId = permissionParam.getId();
      PermissionQuery permissionQuery = new PermissionQuery();
      permissionQuery.setId(permId);
      Response<PageDto<PermissionDto>> pageDtoPermissionResponse =
          uniClientFacade.getPermissionResource().searchPerm(permissionQuery);
      List<Info> infoList = pageDtoPermissionResponse.getInfo();
      if (CollectionUtils.isEmpty(infoList)) {
        PageDto<PermissionDto> permissionDtoPageDto = pageDtoPermissionResponse.getData();
        if (permissionDtoPageDto != null && !CollectionUtils
            .isEmpty(permissionDtoPageDto.getData())) {
          Integer domainId = permissionDtoPageDto.getData().get(0).getDomainId();
          if (queryDomainIdSet(authentication).contains(domainId)) {
            return true;
          }
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_PERMID_CHECK.equals(permType);
    }
  }


  private class PermPermIdsCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      if (targetObject instanceof RoleParam) {
        RoleParam roleParam = (RoleParam) targetObject;
        List<Integer> permIds = roleParam.getPermIds();
        PermissionQuery permissionQuery = new PermissionQuery();
        permissionQuery.setPermIds(permIds);
        Response<PageDto<PermissionDto>> pageDtoPermissionResponse =
            uniClientFacade.getPermissionResource().searchPerm(permissionQuery);
        List<Info> infoList = pageDtoPermissionResponse.getInfo();
        if (CollectionUtils.isEmpty(infoList)) {
          PageDto<PermissionDto> permissionDtoPageDto = pageDtoPermissionResponse.getData();
          if (permissionDtoPageDto != null && !CollectionUtils
              .isEmpty(permissionDtoPageDto.getData())) {
            for (PermissionDto permissionDto : permissionDtoPageDto.getData()) {
              if (!queryDomainIdSet(authentication).contains(permissionDto.getDomainId())) {
                return false;
              }
            }
            return true;
          }
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_PERMIDS_CHECK.equals(permType);
    }
  }


  private class PermTagTypeIdCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      if (targetObject instanceof TagTypeParam) {
        TagTypeParam tagTypeParam = (TagTypeParam) targetObject;
        Set<Integer> domainIdSet = queryDomainIdSet(authentication);
        if (CollectionUtils.isEmpty(domainIdSet)) {
          return false;
        }
        TagTypeQuery tagTypeQuery = new TagTypeQuery();
        tagTypeQuery.setDomainIds(new ArrayList<Integer>(domainIdSet));
        Response<List<TagTypeDto>> response =
            uniClientFacade.getTagResource().searchTagTypes(tagTypeQuery);
        List<Info> infoList = response.getInfo();
        if (CollectionUtils.isEmpty(infoList)) {
          List<TagTypeDto> tagTypeDtoList = response.getData();
          Set<Integer> tagTypeIdSet = new HashSet<Integer>();
          if (!CollectionUtils.isEmpty(tagTypeDtoList)) {
            for (TagTypeDto tagTypeDto : tagTypeDtoList) {
              tagTypeIdSet.add(tagTypeDto.getId());
            }
          }
          if (tagTypeIdSet.contains(tagTypeParam.getId())) {
            return true;
          }
        }
      }
      throw new NotSupportedException();
    }

    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_TAGTYPEID_CHECK.equals(permType);
    }
  }


  private class PermTagIdCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      Integer tagId = null;
      if (targetObject instanceof Integer) {
        tagId = (Integer) targetObject;
      }
      if (targetObject instanceof TagParam) {
        tagId = ((TagParam) targetObject).getId();
      }
      if (tagId == null) {
        return true;
      }
      TagQuery tagQuery = new TagQuery();
      tagQuery.setId(tagId);
      Response<PageDto<TagDto>> response = uniClientFacade.getTagResource().searchTags(tagQuery);
      List<Info> infoList = response.getInfo();
      if (CollectionUtils.isEmpty(infoList)) {
        List<TagDto> tagDtoList = response.getData().getData();
        if (CollectionUtils.isEmpty(tagDtoList)) {
          return false;
        }
        Integer tagTypeId = tagDtoList.get(0).getTagTypeId();
        TagTypeQuery tagTypeQuery = new TagTypeQuery();
        tagTypeQuery.setId(tagTypeId);
        Integer domainId =
            uniClientFacade.getTagResource().searchTagTypes(tagTypeQuery).getData().get(0)
                .getDomainId();
        if (queryDomainIdSet(authentication).contains(domainId)) {
          return true;
        }
      }
      throw new NotSupportedException();
    }


    @Override
    public boolean supportCheck(String permType) {
      return PermType.PERM_TAGID_CHECK.equals(permType);
    }
  }

  private class SystemAccountCheck implements PermissionCheck {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject,
        Object permission) throws NotSupportedException {
      TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo) authentication.getPrincipal();
      Byte userType = techOpsUserExtInfo.getUserDto().getType();
      // 必须是系统账号才有权限
      if (UserType.SYSTEM.equals(userType)) {
        return true;
      }
      return false;
    }


    @Override
    public boolean supportCheck(String permType) {
      return PermType.SYSTEM_ACCOUNT_CHECK.equals(permType);
    }
  }

  /**
   * 从当前登录用户信息中获取其对应的域Id的集合.
   */
  private Set<Integer> queryDomainIdSet(Authentication authentication) {
    TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo) authentication.getPrincipal();
    return techOpsUserExtInfo.getDomainIdSet();
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return super.hasPermission(authentication, targetId, targetType, permission);
  }
}
