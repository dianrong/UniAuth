package com.dianrong.common.uniauth.common.interfaces.rw;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.interfaces.read.IDomainResource;

public interface IDomainRWResource extends IDomainResource {

    @POST
    @Path("addnewpstakeholder")
    //scenario: add new stakeholder
    Response<StakeholderDto> addNewStakeholder(StakeholderParam stakeholderParam);
    
    @POST
    @Path("updatestakeholder")
    //scenario: update stakeholder
    Response<String> updateStakeholder(StakeholderParam stakeholderParam);
    
    @POST
    @Path("deletestakeholder")
    //scenario: delete stakeholder
    Response<String> deleteStakeholder(PrimaryKeyParam primaryKeyParam);
}
