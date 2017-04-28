package com.dianrong.common.uniauth.server.service.support;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.TenancyService;
import com.dianrong.common.uniauth.server.util.UniBundle;

@Component
public class TenancyIdentityService {

    @Resource(name = "uniauthConfig")
    private Map<String, String> allZkNodeMap;
    
    @Autowired
    private TenancyService tanancyService;
    
    /**
     * . 获取一个可用的租户id
     * 
     * @return tenancyId for current thread
     */
    public Long getOneCanUsedTenancyId() {
        return getTenancyId(false);
    }

    /**
     * . 获取一个租户id，并且必须是有效值
     * 
     * @return tenancyId for current thread
     */
    public Long getTenancyIdWithCheck() {
        return getTenancyId(true);
    }

    /**
     * . 获取一个可用的租户id 优先级为：tenancyId -> tenancyCode
     * 
     * @return tenancyId for current thread
     */
    private Long getTenancyId(boolean check) {
        Long _id = (Long) CxfHeaderHolder.TENANCYID.get();
        if (_id != null) {
            return _id;
        }
        String tenancyCode = (String) CxfHeaderHolder.TENANCYCODE.get();
        if (StringUtils.hasText(tenancyCode)) {
            TenancyDto dto = tanancyService.getEnableTenancyByCode(tenancyCode);
            if (dto != null) {
                return dto.getId();
            }
        }
        if (check) {
            if (checkTenancyIdentity()) {
                throw new AppException(InfoName.TENANCY_IDENTITY_REQUIRED, UniBundle.getMsg("common.parameter.tenancyidentity.required"));
            } else {
                return tanancyService.getEnableTenancyByCode(AppConstants.DEFAULT_TANANCY_CODE).getId();
            }
        }
        return AppConstants.TENANCY_UNRELATED_TENANCY_ID;
    }
    
    /**
     * A switch to control check tenancyIdentity forcibly
     * 
     * @return true or false
     */
    private boolean checkTenancyIdentity() {
        return "true".equalsIgnoreCase(allZkNodeMap.get(AppConstants.CHECK_TENANCY_IDENTITY_FORCIBLY));
    }
}
