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
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
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
     * @param pageParam
     * @return
     */
    @RequestMapping(value = "/code/query", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<PageDto<UserExtendDto>> queryEavCodes(@RequestBody UserExtendPageParam pageParam) {
    	return uARWFacade.getUserExtendRWResource().searchUserExtend(pageParam);
    }
    
    /**.
     * 新增eav-code
     * @param param
     * @return
     */
    @RequestMapping(value = "/code/insert", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops') ")
    public Response<UserExtendDto> addEavCode(@RequestBody UserExtendParam param) {
    	return uARWFacade.getUserExtendRWResource().addUserExtend(param);
    }
    
    /**.
     * 更新eav-code信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/code/modify", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops') ")
    public Response<Integer> modifyEavCode(@RequestBody UserExtendParam param) {
    	return uARWFacade.getUserExtendRWResource().updateUserExtend(param);
    }
    
    /**.
     * 查询user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/query", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<PageDto<UserExtendValDto>> queryUserEavCodes(@RequestBody UserExtendValParam param) {
       return uARWFacade.getUserExtendValRWResource().searchByUserIdAndCode(param);
    }
    
    /**.
     * 修改user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/modify", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public  Response<Integer> modifyUserEavCode(@RequestBody UserExtendValParam param) {
    	return uARWFacade.getUserExtendValRWResource().updateById(param);
    }
    
    /**.
     * 新增user-eav信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/insert", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<UserExtendValDto> addUserEavCode(@RequestBody UserExtendValParam param) {
         return uARWFacade.getUserExtendValRWResource().add(param);
    }
    
    /**.
     * 禁用user-eav信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/disable", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public  Response<Integer> disableUserEavCode(@RequestBody UserExtendValParam param) {
        return uARWFacade.getUserExtendValRWResource().delById(param);
    }
    
    /**.
     * 启用user-eav信息
     * @param tagQuery
     * @return
     */
    @RequestMapping(value = "/user/enable", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<Integer> enableUserEavCode(@RequestBody UserExtendValParam  param) {
    	UserExtendValParam tcondtion = new UserExtendValParam();
    	tcondtion.setId(param.getId());
    	tcondtion.setStatus((byte)0);
    	return uARWFacade.getUserExtendValRWResource().updateById(tcondtion);
    }
}
