package com.dianrong.common.techops.action;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;

/**
 * eav 管理相关action
 */
@RestController
@RequestMapping("eav")
public class EavAction {

    @Resource
    private UARWFacade uARWFacade;

    /**.
     * 查询eav-code信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/code/query", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<PageDto<UserExtendDto>> queryEavCodes(@RequestBody UserExtendPageParam param) {
//        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    	return null;
    }
    
    /**.
     * 新增eav-code
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/code/insert", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<Integer> addEavCode(@RequestBody UserExtendParam param) {
    	return null;
//        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
    
    /**.
     * 更新eav-code信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/code/modify", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<Integer> modifyEavCode(@RequestBody UserExtendParam param) {
    	return null;
//        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
    
    /**.
     * 查询user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/query", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<PageDto<TagDto>> queryUserEavCodes(@RequestBody TagQuery tagQuery) {
        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
    
    /**.
     * 修改user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/modify", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<PageDto<TagDto>> modifyUserEavCode(@RequestBody TagQuery tagQuery) {
        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
    
    /**.
     * 新增user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/insert", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<PageDto<TagDto>> addUserEavCode(@RequestBody TagQuery tagQuery) {
        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
    
    /**.
     * 禁用user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/disable", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<PageDto<TagDto>> disableUserEavCode(@RequestBody TagQuery tagQuery) {
        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
    
    /**.
     * 器哟个user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/enable", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_ADMIN')")
    public Response<PageDto<TagDto>> enableUserEavCode(@RequestBody TagQuery tagQuery) {
        return uARWFacade.getTagRWResource().searchTags(tagQuery);
    }
}
