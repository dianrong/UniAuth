package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.model.CasGetServiceTicketModel;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("cfgService")
public class CfgService extends BaseService {

  private static final String DEFAULT_CAS_URL = "https://passport.dianrong.com";
  /**
   * . 远程调用对象
   */
  @Autowired
  private UniClientFacade uniClientFacade;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  /**
   * 根据配置Key获取配置对象,主要用于Cas的页面自定义.
   */
  public List<ConfigDto> queryConfigDtoByCfgKeys(List<String> cfgkeys) throws Exception {
    if (cfgkeys == null || cfgkeys.isEmpty()) {
      return new ArrayList<ConfigDto>();
    }
    // 配置查询入参
    CfgParam cfgParam = new CfgParam();
    cfgParam.setCfgKeys(cfgkeys).setNeedBLOBs(true);
    // 长度 * 2
    cfgParam.setPageNumber(0).setPageSize(cfgkeys.size() * 2);
    Response<PageDto<ConfigDto>> reponseInfo = uniClientFacade.getConfigResource()
        .queryConfig(cfgParam);
    checkInfoList(reponseInfo.getInfo());
    PageDto<ConfigDto> pinfo = reponseInfo.getData();
    if (pinfo != null) {
      return pinfo.getData();
    }
    return null;
  }

  /**
   * 根据配置Key模糊匹配返回配置信息列表.
   */
  public List<ConfigDto> queryConfigDtoByLikeCfgKeys(String likeCfgKey) throws Exception {
    if (StringUtil.strIsNullOrEmpty(likeCfgKey)) {
      return new ArrayList<ConfigDto>();
    }
    // 配置查询入参
    CfgParam cfgParam = new CfgParam();
    cfgParam.setCfgKeyLike(likeCfgKey).setNeedBLOBs(true);
    cfgParam.setPageNumber(0).setPageSize(20);
    Response<PageDto<ConfigDto>> reponseInfo = uniClientFacade.getConfigResource()
        .queryConfig(cfgParam);
    checkInfoList(reponseInfo.getInfo());
    PageDto<ConfigDto> pinfo = reponseInfo.getData();
    if (pinfo != null) {
      return pinfo.getData();
    }
    return null;
  }

  /**
   * . 获取zk配置项目 验证码的绝对路径
   */
  @SuppressWarnings("deprecation")
  public String getCaptchaAbsolutePath() {
    String casUrl = allZkNodeMap.get("cas_server");
    if (!StringUtils.hasLength(casUrl)) {
      casUrl = DEFAULT_CAS_URL;
    }
    return URLEncoder.encode(casUrl + "/" + CasGetServiceTicketModel.DEFAULT_CATCHA_RELATIVE_PATH);
  }
}
