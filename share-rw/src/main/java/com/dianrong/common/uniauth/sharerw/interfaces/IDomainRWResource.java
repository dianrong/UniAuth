package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.interfaces.read.IDomainResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface IDomainRWResource extends IDomainResource {

  @POST
  @Path("addnewdomain")
  // scenario: add new domain
  Response<DomainDto> addNewDomain(DomainParam domainParam);

  @POST
  @Path("updatedomain")
  // scenario: update domain
  Response<Void> updateDomain(DomainParam domainParam);

  @POST
  @Path("addnewpstakeholder")
  // scenario: add new stakeholder
  Response<StakeholderDto> addNewStakeholder(StakeholderParam stakeholderParam);

  @POST
  @Path("updatestakeholder")
  // scenario: update stakeholder
  Response<Void> updateStakeholder(StakeholderParam stakeholderParam);

  @POST
  @Path("deletestakeholder")
  // scenario: delete stakeholder
  Response<Void> deleteStakeholder(PrimaryKeyParam primaryKeyParam);
}
