package com.dianrong.common.uniauth.server.test.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
@SuppressWarnings("unchecked")
public class SearchTest extends AbstractJUnit4SpringContextTests {

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

        private <T> T getObjectIfNotNull(Attribute attribute, Class<T> cls) throws NamingException {
            if (attribute == null) {
                return null;
            }
            return (T) attribute.get();
        }
    }

    @Test
    public void getAllPersonsTest() {
        List<Person> lists = ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper());
        System.out.println(lists);
    }

    @Test
    public void getAllPersonsTest2() {
        System.out.println(ldapTemplate.findAll(Person.class));
    }

    @Test
    public void addPersonsTest() {
        Person copyOne = ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper()).get(0);
        copyOne.setDn(buildPersonDn("wangwang"));
        ldapTemplate.create(copyOne);
    }

    @Test
    public void deletePersonsTest() {
        Person p = new Person();
        p.setDn(buildPersonDn("wanglin"));
        ldapTemplate.delete(p);
    }

    @Test
    public void updatePersonsTest() {
        Person copyOne = ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper()).get(0);
        copyOne.setDn(buildPersonDn("he"));
        copyOne.setCn("wang lin two");
        copyOne.setUid("he");
        ldapTemplate.update(copyOne);
    }

    private Name buildPersonDn(String fullname) {
        return LdapNameBuilder.newInstance("uid=" + fullname).build();
    }
}
