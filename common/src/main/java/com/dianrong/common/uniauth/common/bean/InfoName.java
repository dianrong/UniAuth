package com.dianrong.common.uniauth.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Arc on 14/1/16.
 */
@ApiModel("异常信息分类")
public enum InfoName {
  @ApiModelProperty("一般是由于没有请求的权限，需要重定向到认证页面认证")
  REDIRECT,
  @ApiModelProperty("请求参数不满足要求或者没有权限请求")
  BAD_REQUEST,
  @ApiModelProperty("请求参数验证失败")
  VALIDATE_FAIL,
  @ApiModelProperty("服务器内部错误")
  INTERNAL_ERROR,
  @ApiModelProperty("用于调试的异常栈信息")
  STACKTRACE,
  @ApiModelProperty("登陆错误，用户未找到")
  LOGIN_ERROR_USER_NOT_FOUND,
  @ApiModelProperty("登陆错误，相同账号的多个账户存在")
  LOGIN_ERROR_MULTI_USER_FOUND,
  @ApiModelProperty("登陆错误")
  LOGIN_ERROR,
  @ApiModelProperty("登陆错误，用户处于禁用状态")
  LOGIN_ERROR_STATUS_1,
  @ApiModelProperty("登陆错误，用户登陆失败次数过多被锁定")
  LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT,
  @ApiModelProperty("登陆错误，IPA账号登陆失败次数太多")
  LOGIN_ERROR_IPA_TOO_MANY_FAILED,
  @ApiModelProperty("登陆错误，用户需要初始化密码")
  LOGIN_ERROR_NEW_USER,
  @ApiModelProperty("登陆错误，用户密码过期需要重新设定")
  LOGIN_ERROR_EXCEED_MAX_PASSWORD_VALID_MONTH,

  @ApiModelProperty("没有权限操作当前组")
  GRP_NOT_OWNER,
  @ApiModelProperty("请求缺少必要的租户身份信息，租户id或者租户code")
  TENANCY_IDENTITY_REQUIRED,
  @ApiModelProperty("请求中缺少必要的身份信息")
  IDENTITY_REQUIRED,
  @ApiModelProperty("没有权限访问")
  NO_PRIVILEGE,
  ;
}
