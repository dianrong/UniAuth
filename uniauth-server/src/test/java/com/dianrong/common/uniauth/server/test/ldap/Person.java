package com.dianrong.common.uniauth.server.test.ldap;

import javax.naming.Name;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Data
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"})
public class Person {

  @Id
  private Name dn;

  private String uid;

  private String cn;

  private String givenName;

  private String sn;

  private String mail;

  private String entryuuid;

  private String displayName;
}
