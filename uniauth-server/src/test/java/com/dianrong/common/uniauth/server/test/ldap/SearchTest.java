package com.dianrong.common.uniauth.server.test.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.dianrong.common.uniauth.server.ldap.ipa.entity.User;
import com.dianrong.common.uniauth.server.test.BaseTest;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.OperationNotSupportedException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class SearchTest extends BaseTest {

  @Autowired
  private LdapTemplate ldapTemplate;

  private class PersonAttributesMapper implements AttributesMapper<Person> {

    @Override
    public Person mapFromAttributes(Attributes attributes) throws NamingException {
      Person person = new Person();
      person.setUid(getObjectIfNotNull(attributes.get("uid"), String.class));
      person.setCn(getObjectIfNotNull(attributes.get("cn"), String.class));
      person.setGivenName(getObjectIfNotNull(attributes.get("givenName"), String.class));
      person.setSn(getObjectIfNotNull(attributes.get("sn"), String.class));
      person.setMail(getObjectIfNotNull(attributes.get("mail"), String.class));
      person.setEntryuuid(getObjectIfNotNull(attributes.get("entryuuid"), String.class));
      person.setDisplayName(getObjectIfNotNull(attributes.get("displayName"), String.class));
      return person;
    }

    @SuppressWarnings("unchecked")
    private <T> T getObjectIfNotNull(Attribute attribute, Class<T> cls) throws NamingException {
      if (attribute == null) {
        return null;
      }
      return (T) attribute.get();
    }
  }

  @Test
  public void getAllPersonsTest() {
    List<Person> lists =
        ldapTemplate.search(query().where("cn").is("admin"), new PersonAttributesMapper());
    System.out.println(lists);
  }

  @Test
  public void getAllPersonsTest2() {
    System.out.println(ldapTemplate.findAll(Person.class));
  }

  @Test
  public void testAuthentication() {
    String account = "wangxxxxx";
    String password = "xxxxxxx!";
    for (int i = 0; i < 16; i++) {
      try {
        authentication(account, password);
        System.out.println("success");
      } catch (AuthenticationException ex) {
        ex.printStackTrace();
      } catch (EmptyResultDataAccessException e2) {
        e2.printStackTrace();
      } catch (OperationNotSupportedException ose) {
        ose.printStackTrace();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  private void authentication(String account, String password) {
    ldapTemplate.authenticate(query().base("cn=users").where("uid").is(account), password);
  }

  @Test
  public void getUserByAccount() {
    User u = ldapTemplate.findOne(query().base("cn=users").where("uid").is("wangxxxx"), User.class);
    System.out.println(u);
  }
}
