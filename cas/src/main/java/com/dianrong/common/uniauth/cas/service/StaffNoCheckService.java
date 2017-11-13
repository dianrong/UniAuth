package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.exp.InvalidStaffNoException;
import com.dianrong.common.uniauth.cas.service.support.StaffInfo;
import com.dianrong.common.uniauth.cas.service.support.StaffInfoParser;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处理StaffNo.
 */
@Service
public class StaffNoCheckService {

  private static final String STAFF_INFO_KEY = "UA_STAFF_NO";

  private static final String STAFF_INFO_CHECK_LOG_NAME = "staff.info.check.log.tag";

  private static final Logger CHECK_LOGGER = LoggerFactory.getLogger(STAFF_INFO_CHECK_LOG_NAME);

  @Autowired
  private UniClientFacade uniClientFacade;

  private volatile Map<String, StaffInfo> staffNoMap;

  /**
   * 检查StaffNo是否正确.
   */
  public void checkStaffNo(String account, Long tenancyId, String staffNo)
      throws InvalidStaffNoException {
    if (!StringUtils.hasText(staffNo)) {
      throw new InvalidStaffNoException("Staff No. can't be empty.");
    }
    Assert.notNull(tenancyId, "TenancyId can not be null");
    Assert.notNull(account, "Account can not be null");
    ensureStaffNoMapInit();

    // 确认员工号的格式
    staffNo = staffNo.toUpperCase();
    StaffInfo staffInfo = staffNoMap.get(staffNo);
    if (staffInfo == null) {
      throw new InvalidStaffNoException(staffNo + " not exist.");
    }
    // 确认员工对应的邮箱和name是否一致
    UserQuery userQuery = new UserQuery();
    userQuery.setTenancyId(tenancyId);
    userQuery.setAccount(account);
    Response<PageDto<UserDto>> response = uniClientFacade.getUserResource().searchUser(userQuery);
    List<Info> infoList = response.getInfo();
    if (infoList != null && !infoList.isEmpty()) {
      throw new UniauthCommonException(
          "Failed query user from uniauth server.TenancyId:" + tenancyId + ", Account:" + account);
    }
    if (response.getData() == null || response.getData().getData() == null ||
        response.getData().getData().isEmpty()) {
      throw new UniauthCommonException(
          "Failed query user from uniauth server.TenancyId:" + tenancyId + ", Account:" + account);
    }
    UserDto user = response.getData().getData().get(0);
    // check Name
    String userName = user.getName();
    String staffName = staffInfo.getStaffName();
    if (!checkStaffInfo(userName, staffName)) {
      CHECK_LOGGER
          .warn("TenancyId:{}, Account:{}, 姓名不一致.名单提供的为:{}, Uniauth中的为:{}", tenancyId, account,
              staffName, userName);
    }
    // check Email
    String userEmail = user.getEmail();
    String staffEmail = staffInfo.getEmail();
    if (!StringUtil.isEmailAddress(staffEmail)) {
      // 格式不规范的邮箱,不处理.
      staffEmail = null;
    }
    if (!checkStaffInfo(userEmail, staffEmail)) {
      CHECK_LOGGER
          .warn("TenancyId:{}, Account:{}, 邮箱不一致.名单提供的为:{}, Uniauth中的为:{}", tenancyId, account,
              staffEmail, userEmail);
    }
  }

  private boolean checkStaffInfo(String userInfo, String staffInfo) {
    // 数据不全, 直接放过
    if (!StringUtils.hasText(userInfo) || !StringUtils.hasText(staffInfo)) {
      return true;
    }
    if (userInfo.trim().equals(staffInfo.trim())) {
      return true;
    }
    return false;
  }

  /**
   * 加载检测文件到内存中来.
   */
  private void ensureStaffNoMapInit() {
    if (this.staffNoMap != null) {
      return;
    }
    synchronized (this) {
      if (this.staffNoMap == null) {
        CfgParam param = new CfgParam();
        param.setCfgKey(STAFF_INFO_KEY);
        param.setNeedBLOBs(true);
        Response<PageDto<ConfigDto>> response = uniClientFacade.getConfigResource()
            .queryConfig(param);
        if (response.getInfo() != null && !response.getInfo().isEmpty()) {
          throw new UniauthCommonException("Failed get staff No. file from server.");
        }
        if (response.getData() == null || response.getData().getData() == null ||
            response.getData().getData().isEmpty()) {
          throw new UniauthCommonException(
              "Get staff No. file from uniauth server, but response is null, make sure"
                  + " the staff No. file is uploaded.");
        }
        ConfigDto configDto = response.getData().getData().get(0);
        List<StaffInfo> staffInfoList = StaffInfoParser.parse(configDto.getFile());
        if (staffInfoList.isEmpty()) {
          throw new UniauthCommonException(
              "Make sure the staff No. file is ok, the parse result is empty.");
        }
        Map<String, StaffInfo> map = new HashMap<>(staffInfoList.size());
        for (StaffInfo staffInfo : staffInfoList) {
          String staffNo = staffInfo.getStaffNo();
          if (StringUtils.hasText(staffNo)) {
            map.put(staffNo, staffInfo);
          }
        }
        this.staffNoMap = map;
      }
    }
  }
}
