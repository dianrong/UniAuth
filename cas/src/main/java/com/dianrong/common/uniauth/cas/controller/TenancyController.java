package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.model.HttpResponseModel;
import com.dianrong.common.uniauth.cas.service.TenancyService;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * . tenancy处理相关
 *
 * @author wanglin
 */
@Controller
@RequestMapping("/tenancy")
public class TenancyController {

  @Autowired
  private TenancyService tenancyService;

  @Autowired
  private MessageSource messageSource;

  /**
   * 获取默认的tenancy的信息.
   */
  @RequestMapping(value = "/getDefault", method = RequestMethod.GET)
  public void getDefaultTenancyInfo(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    WebScopeUtil.sendJsonToResponse(response,
        HttpResponseModel.buildSuccessResponse().setContent(tenancyService.getDefaultTenancy()));
  }

  /**
   * 校验tenancyCode是否有效.
   */
  @RequestMapping(value = "/check/{tenancyCode}", method = {RequestMethod.GET, RequestMethod.POST})
  public void checkTenancyCode(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("tenancyCode") String tenancyCode) throws IOException {
    TenancyDto dto = tenancyService.getTenancyByCode(tenancyCode);
    if (dto != null) {
      WebScopeUtil
          .sendJsonToResponse(response, HttpResponseModel.buildSuccessResponse().setContent(dto));
    } else {
      // return default tenancy
      HttpResponseModel<TenancyDto> result = new HttpResponseModel<TenancyDto>();
      result.setSuccess(false).setContent(tenancyService.getDefaultTenancy())
          .setMsg(UniBundleUtil
              .getMsg(messageSource, "screen.main.tenancy.msg.tenancy.notexist", tenancyCode));
      WebScopeUtil.sendJsonToResponse(response, result);
    }
  }
}
