package com.dianrong.common.uniauth.client.custom.model;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.google.common.collect.Sets;

/**
 * 当前登陆用户的所有域下的权限集合的信息
 * @author wanglin
 */
public final class AllDomainUserExtInfo implements Serializable {
	private static final long serialVersionUID = 8347558918889027136L;
	// Map<DomainCode, userExtInfo>
	private ConcurrentHashMap<String, SingleDomainUserExtInfo> userExtInfoMap = new ConcurrentHashMap<>();
	
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
    
    /**
     * get userExtInfo by domainCode
     * @param domainCode not null
     * @return UserDetails in domain[domainCode]
     * @throws IllegalArgumentException if the domainCode or userDetails is null
     */
    public SingleDomainUserExtInfo addUserDetailIfAbsent(String domainCode, SingleDomainUserExtInfo userDetails) {
        Assert.notNull(domainCode);
        Assert.notNull(userDetails);
       return userExtInfoMap.putIfAbsent(domainCode, userDetails);
    }
    
    public SingleDomainUserExtInfo getOneSingleDomainUserExtInfo() {
    	for(String code : userExtInfoMap.keySet()){
    		return userExtInfoMap.get(code);
    	}
    	throw new UniauthCommonException("userExtInfoMap is empty");
    }
    
    /**
     * get all domain code
     * @return Set not null
     */
    public Set<String> getAllDomainCode() {
        Set<String> keySet = Sets.newHashSet();
        Enumeration<String> keys = userExtInfoMap.keys();
        while (keys.hasMoreElements()) {
            keySet.add(keys.nextElement());
        }
        return keySet;
    }
    
    /**
     * replace userExtInfo with the domain code 
     * @param domainCode domainCode
     * @param userDetails SingleDomainUserExtInfo
     * @throws IllegalArgumentException if the domainCode or userDetails is null
     */
    public void replaceUserExtInfo(String domainCode, SingleDomainUserExtInfo userDetails) {
    	 addUserDetail(domainCode, userDetails);
    }
}
