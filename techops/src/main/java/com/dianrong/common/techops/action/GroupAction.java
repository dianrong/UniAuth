package com.dianrong.common.techops.action;

import com.dianrong.common.techops.bean.Node;
import com.dianrong.common.techops.helper.CustomizeBeanConverter;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arc on 29/2/2016.
 */
@RestController
@RequestMapping("group")
public class GroupAction {

    @Resource
    private UARWFacade uARWFacade;

    @RequestMapping(value = "/tree" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    Response<List<Node>> getGroupTree(@RequestBody GroupParam groupParam) {
        GroupDto groupDto = uARWFacade.getGroupRWResource().getGroupTree(groupParam).getData();
        if(groupDto != null) {
            return Response.success(CustomizeBeanConverter.convert(Arrays.asList(groupDto)));
        } else {
            return Response.success(null);
        }
    }

    @RequestMapping(value = "/add" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    Response<GroupDto> addNewGroupIntoGroup(@RequestBody GroupParam groupParam) {
        Response<GroupDto> groupDto = uARWFacade.getGroupRWResource().addNewGroupIntoGroup(groupParam);
        return groupDto;
    }

}
