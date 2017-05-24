package com.dianrong.common.uniauth.server.ldap.ipa.entity;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Data;

@Data
@Entry(objectClasses = {"posixgroup"})
public class Group {

    @Id
    private Name dn;

    private String cn;

    private Long gidNumber;
}
