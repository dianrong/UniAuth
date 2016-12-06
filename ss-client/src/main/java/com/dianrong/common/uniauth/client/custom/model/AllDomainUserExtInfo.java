package com.dianrong.common.uniauth.client.custom.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

/**
 * 当前登陆用户的所有域下的权限集合的信息
 * @author wanglin
 */
public class AllDomainUserExtInfo implements Serializable {
	private static final long serialVersionUID = 8347558918889027136L;
	// Map<DomainCode, userExtInfo>
	private Map<String, SingleDomainUserExtInfo> userExtInfoMap = new ConcurrentHashMap<>();
	
	/**
	 * get userExtInfo by domainCode
	 * @param domainCode not null
	 * @return UserDetails in domain[domainCode]
	 * @throws IllegalArgumentException if the domainCode is null
	 */
	public SingleDomainUserExtInfo getUserDetail(String domainCode) {
	    Assert.notNull(domainCode);
	    return userExtInfoMap.get(domainCode);
	}
	
	   /**
     * get userExtInfo by domainCode
     * @param domainCode not null
     * @return UserDetails in domain[domainCode]
     * @throws IllegalArgumentException if the domainCode or userDetails is null
     */
    public void addUserDetail(String domainCode, SingleDomainUserExtInfo userDetails) {
        Assert.notNull(domainCode);
        Assert.notNull(userDetails);
        userExtInfoMap.put(domainCode, userDetails);
    }
}
