package com.dianrong.common.uniauth.server.ldap.ipa.entity;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.dianrong.common.uniauth.server.ldap.ipa.support.LdapDate;

import lombok.Data;

@Data
@Entry(objectClasses = { "posixaccount"})
public class User {
    @Id
    private Name dn;
    
    private String cn;
    
    private Long gidNumber;
    
    private String sn;
    
    private String uid;
    
    private Long uidNumber;
    
    private String displayName;
    
    private String givenName;
    
    @Attribute(name="mail")
    private String email;
    
    @Attribute(name="mobile")
    private String phone;
    
    @Attribute(name="memberOf")
    private List<String> groups;
    
    private String homeDirectory;
    
    private String loginShell;
    
    private String krbPrincipalName;
    
    private LdapDate krbPasswordExpiration;
    
    private LdapDate krbLastPwdChange;
    
    private String gecos;
}
