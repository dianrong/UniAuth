package com.dr.cas.persondir.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

public class PersonAttributeDAOImpl extends StubPersonAttributeDao {
  
  public PersonAttributeDAOImpl(){
    
  }
  public PersonAttributeDAOImpl(Map<String, List<Object>> backingMap) {
    super(backingMap);
  }
  
  @Override
  public IPersonAttributes getPerson(String username) {
    Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
    attributes.put("username", Collections.singletonList((Object) username));
    return new AttributeNamedPersonImpl(attributes);
  }
}
