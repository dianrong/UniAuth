package com.dianrong.common.uniauth.server.ldap.ipa.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.Name;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.util.StringUtils;


/**
 * IPA账号处理相关的工具方法.
 *
 * @author wanglin
 */
public final class IpaUtil {

  /**
   * 便利方法, 用于生成DN.
   */
  public static Name buildDn(final String base, final String key, final String value) {
    Assert.notNull(key);
    Assert.notNull(value);
    return buildDn(base, new LinkedHashMap<String, String>() {
      private static final long serialVersionUID = 4739618845415969059L;

      {
        put(key, value);
      }
    });
  }

  /**
   * 便利方法, 用于生成DN
   *
   * @param params 用顺序的map. 根据dn从后到前的顺序的LinkedHashMap
   */
  public static Name buildDn(String base, LinkedHashMap<String, String> params) {
    LdapNameBuilder builder;
    if (StringUtils.hasText(base)) {
      builder = LdapNameBuilder.newInstance(base.trim());
    } else {
      builder = LdapNameBuilder.newInstance();
    }
    if (params != null && !params.isEmpty()) {
      Set<Entry<String, String>> entrySet = params.entrySet();
      for (Entry<String, String> entry : entrySet) {
        builder.add(entry.getKey(), entry.getValue());
      }
    }
    return builder.build();
  }

  /**
   * 将用户的memberOf组信息转化成组code.
   * <br>
   * 比如:<br>
   * cn=ipausers,cn=groups,cn=accounts,dc=ipa,dc=dianrong,dc=com -> ipausers <br>
   * cn=ipausers+name=wwf,cn=groups,cn=accounts,dc=ipa,dc=dianrong,dc=com -> ipausers+name=wwf<br>
   * cn=ipausers,,,,cn=groups,cn=accounts,dc=ipa,dc=dianrong,dc=com -> ipausers<br>
   */
  public static Set<String> translateMemberGroupToGroupName(Collection<String> memberOfGroups) {
    Set<String> groupCodes = Sets.newHashSet();
    if (memberOfGroups == null || memberOfGroups.isEmpty()) {
      return groupCodes;
    }
    for (String memberOf : memberOfGroups) {
      if (!StringUtils.hasText(memberOf)) {
        continue;
      }
      int groupIndex = memberOf.indexOf(IpaConstants.GROUP_URL);
      if (groupIndex == -1) {
        continue;
      }
      String groupCnStr = memberOf.substring(0, groupIndex);
      if (!StringUtils.hasText(groupCnStr)) {
        continue;
      }
      int cnEndIndex = groupCnStr.indexOf("=");
      if (cnEndIndex == -1) {
        continue;
      }
      String groupCode = groupCnStr.substring(cnEndIndex + 1);
      if (!StringUtils.hasText(groupCode)) {
        continue;
      }
      while (groupCode.endsWith(",")) {
        groupCode = groupCode.substring(0, groupCode.length() - 1);
      }
      groupCodes.add(groupCode);
    }
    return groupCodes;
  }
}
