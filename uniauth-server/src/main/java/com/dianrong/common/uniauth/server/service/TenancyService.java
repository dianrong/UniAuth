package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.AuthUtils;
import com.dianrong.common.uniauth.common.util.Base64;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpPath;
import com.dianrong.common.uniauth.server.data.entity.GrpRoleKey;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RolePermissionKey;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserGrpKey;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpPathMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpRoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RolePermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserGrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.cache.TenancyCache;
import com.dianrong.common.uniauth.server.service.common.CommonService;
import com.dianrong.common.uniauth.server.service.inner.TenancyInnerService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.dianrong.common.uniauth.server.util.UniauthServerConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TenancyService {

  @Autowired
  private TenancyMapper tenancyMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private GrpMapper grpMapper;

  @Autowired
  private GrpPathMapper grpPathMapper;

  @Autowired
  private UserGrpMapper userGrpMapper;

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private PermissionMapper permissionMapper;

  @Autowired
  private RolePermissionMapper rolePermissionMapper;

  @Autowired
  private GrpRoleMapper grpRoleMapper;

  @Autowired
  private DomainMapper domainMapper;

  @Autowired
  private TenancyCache tenancyCache;

  // Data filter
  @Resource(name = "tenancyDataFilter")
  private DataFilter dataFilter;
  
  // Another service
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private TenancyInnerService tenancyInnerService;
  
  /**
   * 根据条件获取租户信息.
   */
  public List<TenancyDto> getAllTenancy(Long id, String code, Byte status, String name,
      String contactName, String phone, String description) {
    TenancyExample example = new TenancyExample();
    TenancyExample.Criteria criteria = example.createCriteria();
    if (id != null) {
      criteria.andIdEqualTo(id);
    }
    if (code != null) {
      criteria.andCodeLike("%" + code + "%");
    }
    if (status != null) {
      criteria.andStatusEqualTo(status);
    }
    if (name != null) {
      criteria.andNameLike("%" + name + "%");
    }
    if (contactName != null) {
      criteria.andContactNameLike("%" + contactName + "%");
    }
    if (phone != null) {
      criteria.andPhoneLike("%" + phone + "%");
    }
    if (description != null) {
      criteria.andPhoneLike("%" + phone + "%");
    }
    List<Tenancy> tenancyList = tenancyMapper.selectByExample(example);
    List<TenancyDto> tenancyDtoList = new ArrayList<TenancyDto>();
    if (tenancyList != null) {
      for (Tenancy tenancy : tenancyList) {
        tenancyDtoList.add(BeanConverter.convert(tenancy));
      }
    }
    return tenancyDtoList;
  }

  /**
   * 获取一个可用的租户id.<br>
   * 如果不能获取, 返回一个默认的非租户相关的租户id -1
   *
   * @return tenancyId for current request
   * @see AppConstants.TENANCY_UNRELATED_TENANCY_ID
   */
  public Long getOneCanUsedTenancyId() {
    return tenancyInnerService.getOneCanUsedTenancyId();
  }

  /**
   * 获取一个租户id，并且必须是有效值.
   *
   * @return tenancyId for current thread
   */
  public Long getTenancyIdWithCheck() {
    return tenancyInnerService.getTenancyIdWithCheck();
  }

  /**
   * 根据租户编码获取一个启用的租户.
   *
   * @param tenancyCode 租户编码
   * @return 租户信息
   */
  public TenancyDto getEnableTenancyByCode(String tenancyCode) {
    return tenancyCache.getEnableTenancyByCode(tenancyCode);
  }

  /**
   * 添加新的租户.
   */
  @Transactional
  public TenancyDto addNewTenancy(String code, String name, String contactName, String phone,
      String description, String adminEmail, String adminPassword) {
    CheckEmpty.checkEmpty(code, "code");
    CheckEmpty.checkEmpty(adminEmail, "adminEmail");
    CheckEmpty.checkEmpty(adminPassword, "adminPassword");
    dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_CODE, code);
    Tenancy tenancy = new Tenancy();
    tenancy.setCode(code);
    tenancy.setName(StringUtils.hasText(name) ? name : code);
    tenancy.setContactName(contactName);
    tenancy.setPhone(phone);
    tenancy.setDescription(description);
    tenancy.setStatus(AppConstants.STATUS_ENABLED);
    Date now = new Date();
    tenancy.setCreateDate(now);
    tenancy.setLastUpdate(now);
    return BeanConverter.convert(insertAndInitTenancy(tenancy, adminEmail, adminPassword));
  }

  /**
   * 新建一个租户，需要初始化相关的组，角色，权限等信息.
   */
  @Transactional(propagation = Propagation.REQUIRED)
  private Tenancy insertAndInitTenancy(Tenancy tenancy, String adminEmail, String adminPassword) {
    Assert.notNull(tenancy);
    // step1: insert tenancy
    tenancyMapper.insert(tenancy);

    Date now = new Date();

    // step2: init super admin
    String setAdminEmail = adminEmail;
    String setAdminPassword = adminPassword;
    User admin = new User();
    admin.setName("admin");
    admin.setEmail(setAdminEmail);
    byte[] salt = AuthUtils.createSalt();
    admin.setPassword(Base64.encode(AuthUtils.digest(setAdminPassword, salt)));
    admin.setPasswordSalt(Base64.encode(salt));
    admin.setFailCount(AppConstants.ZERO_BYTE);
    admin.setStatus(AppConstants.STATUS_ENABLED);
    admin.setCreateDate(now);
    admin.setLastUpdate(now);
    // 如果是使用默认的密码,需要登陆的时候修改密码
    admin.setPasswordDate(StringUtils.hasText(adminEmail) ? now : null);
    admin.setTenancyId(tenancy.getId());
    userMapper.insert(admin);

    // step3: init group
    // root grp
    Grp rootGrp = new Grp();
    rootGrp.setName(tenancy.getName());
    rootGrp.setCode(AppConstants.GRP_ROOT);
    rootGrp.setDescription(UniBundle.getMsg("tenancy.new.root.grp", tenancy.getName()));
    rootGrp.setStatus(AppConstants.STATUS_ENABLED);
    rootGrp.setTenancyId(tenancy.getId());
    rootGrp.setCreateDate(now);
    rootGrp.setLastUpdate(now);
    grpMapper.insert(rootGrp);

    // super admin grp
    Grp superAdminGrp = new Grp();
    superAdminGrp.setName(UniBundle.getMsg("tenancy.new.super.admin.grp"));
    superAdminGrp.setCode(AppConstants.TECHOPS_SUPER_ADMIN_GRP);
    superAdminGrp.setDescription(UniBundle.getMsg("tenancy.new.super.admin.grp.des"));
    superAdminGrp.setStatus(AppConstants.STATUS_ENABLED);
    superAdminGrp.setTenancyId(tenancy.getId());
    superAdminGrp.setCreateDate(now);
    superAdminGrp.setLastUpdate(now);
    grpMapper.insert(superAdminGrp);

    // step4: init grp_path
    // rootGrp - rootGrp - 0
    GrpPath gpathRoot = new GrpPath();
    gpathRoot.setAncestor(rootGrp.getId());
    gpathRoot.setDescendant(rootGrp.getId());
    gpathRoot.setDeepth(AppConstants.ZERO_BYTE);
    grpPathMapper.insert(gpathRoot);

    // rootGrp - superAdminGrp - 1
    GrpPath gpathRootSuperAdmin = new GrpPath();
    gpathRootSuperAdmin.setAncestor(rootGrp.getId());
    gpathRootSuperAdmin.setDescendant(superAdminGrp.getId());
    gpathRootSuperAdmin.setDeepth(AppConstants.ONE_BYTE);
    grpPathMapper.insert(gpathRootSuperAdmin);

    // superAdminGrp - superAdminGrp - 0
    GrpPath gpathSuperAdmin = new GrpPath();
    gpathSuperAdmin.setAncestor(superAdminGrp.getId());
    gpathSuperAdmin.setDescendant(superAdminGrp.getId());
    gpathSuperAdmin.setDeepth(AppConstants.ZERO_BYTE);
    grpPathMapper.insert(gpathSuperAdmin);

    // step5: init user_grp
    // root grp owner
    UserGrpKey userGrpOwner = new UserGrpKey();
    userGrpOwner.setUserId(admin.getId());
    userGrpOwner.setGrpId(rootGrp.getId());
    userGrpOwner.setType(AppConstants.ONE_TYPE);
    userGrpMapper.insert(userGrpOwner);

    // super admin grp member
    UserGrpKey userGrpAdmin = new UserGrpKey();
    userGrpAdmin.setUserId(admin.getId());
    userGrpAdmin.setGrpId(superAdminGrp.getId());
    userGrpAdmin.setType(AppConstants.ZERO_TYPE);
    userGrpMapper.insert(userGrpAdmin);

    // select techops domain info
    DomainExample condition = new DomainExample();
    condition.createCriteria().andCodeEqualTo(AppConstants.DOMAIN_CODE_TECHOPS)
        .andStatusEqualTo(AppConstants.STATUS_ENABLED);
    Domain techopsDomain = domainMapper.selectByExample(condition).get(0);

    // step6: init role
    Role adminRole = new Role();
    adminRole.setName(UniBundle.getMsg("tenancy.new.super.admin.role"));
    adminRole.setDescription(UniBundle.getMsg("tenancy.new.super.admin.role.des"));
    adminRole.setStatus(AppConstants.STATUS_ENABLED);
    adminRole.setDomainId(techopsDomain.getId());
    adminRole
        .setRoleCodeId(commonService.getRoleCodeId(UniauthServerConstant.ROLE_CODE_SUPER_ADMIN));
    adminRole.setTenancyId(tenancy.getId());
    roleMapper.insert(adminRole);

    // step7: init permission
    Permission permission = new Permission();
    permission.setValue(AppConstants.DOMAIN_CODE_TECHOPS);
    permission.setDescription(UniBundle.getMsg("tenancy.new.super.admin.permission.des"));
    permission.setStatus(AppConstants.STATUS_ENABLED);
    permission.setPermTypeId(commonService.getPermTypeId(AppConstants.PERM_TYPE_DOMAIN));
    permission.setDomainId(techopsDomain.getId());
    permission.setTenancyId(tenancy.getId());
    permissionMapper.insert(permission);

    // step8: init role_permission
    RolePermissionKey rolePermission = new RolePermissionKey();
    rolePermission.setRoleId(adminRole.getId());
    rolePermission.setPermissionId(permission.getId());
    rolePermissionMapper.insert(rolePermission);

    // step9: init grp_role
    GrpRoleKey grpRole = new GrpRoleKey();
    grpRole.setGrpId(superAdminGrp.getId());
    grpRole.setRoleId(adminRole.getId());
    grpRoleMapper.insert(grpRole);

    return tenancy;
  }

  /**
   * 更新租户的信息.
   */
  @Transactional
  public TenancyDto updateTenancy(Long id, String code, String name, String contactName,
      String phone, String description, Byte status) {
    CheckEmpty.checkEmpty(id, "tenancy_id");

    Tenancy tenancy = tenancyMapper.selectByPrimaryKey(id);
    if (tenancy == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", id, Tenancy.class.getSimpleName()));
    }

    if (StringUtils.hasText(code)) {
      dataFilter.updateFieldCheck(TypeParseUtil.parseToIntegerFromObject(id),
          FieldType.FIELD_TYPE_CODE, code);
    }
    return tenancyCache.updateTenancy(tenancy.getCode(), id, code, name, contactName, phone,
        description, status);
  }
}
