package com.dianrong.common.uniauth.server.ldap.ipa.dao;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.ldap.ipa.entity.Group;
import com.dianrong.common.uniauth.server.ldap.ipa.support.IpaConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDao {

  @Autowired
  private LdapTemplate ldapTemplate;

  /**
   * 根据gidNumber获取组的基本信息.
   * 
   * @param gidNumber 不能为空
   */
  public Group getGroupByAccount(String gidNumber) {
    Assert.notNull(gidNumber);
    return ldapTemplate.findOne(
        query().base(IpaConstants.GROUP_BASE).where("gidNumber").is(gidNumber), Group.class);
  }
}
