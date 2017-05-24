package com.dianrong.common.uniauth.server.test.ldap;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Data;

@Data
@Entry(objectClasses = { "posixaccount"})
public class User {
    
    @Id
    private Name dn;
    
    private String cn;
    
    private Long gidNumber;
    
    private String homeDirectory;
    
    private String sn;
    
    private String uid;
    
    private Long uidNumber;
    
    private String displayName;
    
    private String givenName;
    
    private String mail;
    
    @Attribute(name="mobile")
    private String phone;
    
    @Attribute(name="memberOf")
    private List<String> groups;
}
