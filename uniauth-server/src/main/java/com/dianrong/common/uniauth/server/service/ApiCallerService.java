package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ApiCallerInfoDto;
import com.dianrong.common.uniauth.server.data.entity.ApiCallerInfo;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.mapper.ApiCallerInfoMapper;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiCallerService {

  @Autowired
  private ApiCallerInfoMapper apiCallerInfoMapper;

  @Autowired
  private DomainMapper domainMapper;

  /**
   * Get API caller info.
   *
   * @param account account
   * @param password password
   * @return ApiCallerInfoDto or null
   */
  public ApiCallerInfoDto searchApiCaller(String account, String password) {
    CheckEmpty.checkEmpty(account, "account");
    List<ApiCallerInfo> apiCallers = apiCallerInfoMapper.searchApiCaller(account);
    if (apiCallers != null && !apiCallers.isEmpty()) {
      ApiCallerInfo apiCaller = apiCallers.get(0);
      if (apiCaller.getPassword().equals(password)) {
        Domain domain = domainMapper.selectByPrimaryKey(apiCaller.getDomainId());
        if (domain == null) {
          return null;
        }
        ApiCallerInfoDto dto = BeanConverter.convert(apiCaller, ApiCallerInfoDto.class);
        dto.setDomainCode(domain.getCode()).setDomainName(domain.getDisplayName());
        return dto;
      }
    }
    return null;
  }
}
