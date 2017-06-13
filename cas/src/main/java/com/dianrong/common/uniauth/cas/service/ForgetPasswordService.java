package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.service.support.annotation.TenancyIdentity;
import com.dianrong.common.uniauth.cas.service.support.annotation.TenancyIdentity.Type;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgetPasswordService extends BaseService {

  @Autowired
  private UARWFacade uarwFacade;

  @Autowired
  private UniClientFacade uniClientFacade;

  /**
   * Check user exist.
   *
   * @param accountId email or phone number
   */
  @TenancyIdentity(type = Type.CODE, index = 1)
  public UserDto checkUser(String accountId, String tenancyCode) throws Exception {
    LoginParam loginParam = new LoginParam();
    loginParam.setAccount(StringUtil.trimCompatibleNull(accountId));
    loginParam.setTenancyCode(StringUtil.trimCompatibleNull(tenancyCode));
    Response<UserDto> response = uniClientFacade.getUserResource()
        .getUserInfoByUserTag(loginParam);//.getSingleUser(userParam);
    List<Info> infoList = response.getInfo();

    checkInfoList(infoList);
    return response.getData();
  }

  /**
   * Deprecated.
   * @see {@link #resetPasswordByIdentity(String, Long, String)}.
   */
  @TenancyIdentity(index = 1)
  @Deprecated
  public void resetPassword(String email, Long tenancyId, String password) throws Exception {
    UserParam userParam = new UserParam();
    userParam.setEmail(StringUtil.trimCompatibleNull(email));
    userParam.setPassword(password);
    userParam.setTenancyId(tenancyId);
    Response<Void> response = uarwFacade.getUserRWResource().resetPassword(userParam);
    List<Info> infoList = response.getInfo();

    checkInfoList(infoList);
  }

  /**
   * Reset password by identity.
   * @param identity email or phone number
   */
  @TenancyIdentity(index = 1)
  public void resetPasswordByIdentity(String identity, Long tenancyId, String password)
      throws Exception {
    UserParam userParam = new UserParam();
    if (StringUtil.isPhoneNumber(identity)) {
      userParam.setPhone(StringUtil.trimCompatibleNull(identity));
    } else if (StringUtil.isEmailAddress(identity)) {
      userParam.setEmail(StringUtil.trimCompatibleNull(identity));
    }
    userParam.setPassword(password);
    userParam.setTenancyId(tenancyId);
    Response<Void> response = uarwFacade.getUserRWResource().resetPassword(userParam);
    List<Info> infoList = response.getInfo();

    checkInfoList(infoList);
  }
}
