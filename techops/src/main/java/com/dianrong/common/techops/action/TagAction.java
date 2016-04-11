package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.common.bean.request.TagTypeParam;
import com.dianrong.common.uniauth.common.bean.request.TagTypeQuery;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by Arc on 8/4/2016.
 */
@RestController
@RequestMapping("tag")
public class TagAction {

    @Resource
    private UARWFacade uARWFacade;

    @RequestMapping(value = "/query", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.hasDomain('techops')) or " +
            "(hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#tagQuery.domainId))")
    public Response<PageDto<TagDto>> queryTags(@RequestBody TagQuery tagQuery) {
        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }

    @RequestMapping(value = "/types", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.hasDomain('techops')) or " +
            "(hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#tagTypeQuery.domainId))")
    public Response<List<TagTypeDto>> queryTagTypes(@RequestBody TagTypeQuery tagTypeQuery) {
        return uARWFacade.getTagRWResource().getTagTypes(tagTypeQuery);
    }

    @RequestMapping(value = "add-new-tag-type", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.hasDomain('techops')) or " +
            "(hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#tagTypeParam.domainId))")
    public Response<TagTypeDto> addNewTagType(@RequestBody TagTypeParam tagTypeParam) {
        return uARWFacade.getTagRWResource().addNewTagType(tagTypeParam);
    }

    @RequestMapping(value = "update-tag-type", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.hasDomain('techops')) or " +
            "(hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#tagTypeParam.domainId))")
    public Response<TagTypeDto> updateTagType(@RequestBody TagTypeParam tagTypeParam) {
        return uARWFacade.getTagRWResource().updateTagType(tagTypeParam);
    }

    @RequestMapping(value = "delete-tag-type", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.hasDomain('techops')) or " +
            "(hasRole('ROLE_ADMIN') and hasPermission(#tagTypeParam, 'PERM_TAGTYPEID_CHECK'))")
    public Response<Void> deleteTagType(@RequestBody TagTypeParam tagTypeParam) {
        return uARWFacade.getTagRWResource().deleteTagType(tagTypeParam);
    }

}
