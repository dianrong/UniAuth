package com.dianrong.common.uniauth.server.support.apicontrl;

import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import java.util.Set;

public class ApiCaller implements CallerCredential<ApiCtlPermission> {

  private static final long serialVersionUID = 5827793533927122822L;

  private final String domainName;

  private final String domainCode;

  private final ApiCtlPermission permission;

  private final Long createTime;

  private final Long expiredTime;

  public ApiCaller(String domainName, String domainCode, Set<ApiCtlPermissionItem> permissions) {
    this(domainName, domainCode, permissions, null);
  }

  /**
   * 构造ApiCaller.
   */
  public ApiCaller(String domainName, String domainCode, Set<ApiCtlPermissionItem> permissions,
      Long expiredTime) {
    Assert.notNull(domainCode);
    this.createTime = System.currentTimeMillis();
    if (expiredTime == null) {
      this.expiredTime =
          this.createTime + AppConstants.DEFAULT_API_CALL_TOKEN_AVAILABLE_MILLiSECONDS;
    } else {
      if (expiredTime < this.createTime) {
        throw new IllegalArgumentException(
            expiredTime + " is not a valid expired time, because it is less than current time");
      }
      this.expiredTime = expiredTime;
    }
    ApiCtlPermission permission = new ApiCtlPermission();
    if (permissions != null && !permissions.isEmpty()) {
      for (ApiCtlPermissionItem item : permissions) {
        permission.add(item);
      }
    }
    Assert.notNull(permission);
    this.domainName = domainName;
    this.domainCode = domainCode;
    this.permission = permission;
  }

  public ApiCaller(String domainName, String domainCode, ApiCtlPermission permission) {
    this(domainName, domainCode, permission, null, null);
  }

  /**
   * 构造一个ApiCaller.
   */
  public ApiCaller(String domainName, String domainCode, ApiCtlPermission permission,
      Long createTime, Long expiredTime) {
    super();
    Assert.notNull(domainCode);
    Assert.notNull(permission);
    if (createTime == null) {
      this.createTime = System.currentTimeMillis();
    } else {
      if (createTime < 0) {
        throw new IllegalArgumentException(
            expiredTime + " is not a valid create time, because it is a negative number");
      }
      this.createTime = createTime;
    }
    if (expiredTime == null) {
      this.expiredTime =
          this.createTime + AppConstants.DEFAULT_API_CALL_TOKEN_AVAILABLE_MILLiSECONDS;
    } else {
      if (expiredTime < this.createTime) {
        throw new IllegalArgumentException(
            expiredTime + " is not a valid expired time, because it is less than current time");
      }
      this.expiredTime = expiredTime;
    }
    this.domainName = domainName;
    this.domainCode = domainCode;
    this.permission = permission;
  }

  @Override
  public String getCallerName() {
    return this.domainName;
  }

  @Override
  public String getAccount() {
    return this.domainCode;
  }

  @Override
  public ApiCtlPermission getPermissionInfo() {
    return this.permission;
  }

  @Override
  public long getCreateTime() {
    return this.createTime;
  }

  @Override
  public long getExpireTime() {
    return this.expiredTime;
  }
}
