package com.dianrong.common.techops.sscustom;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.custom.UserInfoCallBack;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.enm.PermTypeEnum;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TechOpsUserInfoCallBack implements UserInfoCallBack {

  @Autowired
  private UniClientFacade uniClientFacade;

  public TechOpsUserInfoCallBack() {
  }

  @Override
  public void fill(UserExtInfo userExtInfo) {
    TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo) userExtInfo;
    Set<Integer> domainIdSet = new HashSet<Integer>();

    Set<String> domainCodeSet = techOpsUserExtInfo.getPermMap().get(PermTypeEnum.DOMAIN.toString());
    if (domainCodeSet != null && !domainCodeSet.isEmpty()) {
      List<String> domainCodeList = Arrays.asList(domainCodeSet.toArray(new String[0]));
      DomainParam domainParam = new DomainParam();
      domainParam.setDomainCodeList(domainCodeList);
      Response<List<DomainDto>> response =
          uniClientFacade.getDomainResource().getAllLoginDomains(domainParam);
      List<DomainDto> domainDtoList = response.getData();
      if (domainDtoList != null && !domainDtoList.isEmpty()) {
        for (DomainDto domainDto : domainDtoList) {
          domainIdSet.add(domainDto.getId());
        }
      }
    }

    techOpsUserExtInfo.setDomainIdSet(domainIdSet);

  }

}
