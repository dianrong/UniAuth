package org.springframework.security.web.access.expression;

import com.dianrong.common.uniauth.client.custom.SSExpressionSecurityMetadataSource;
import com.dianrong.common.uniauth.client.support.CheckDomainDefine;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.TenancyParam;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.DomainDefine.CasPermissionControlType;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.SwitchControl;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class SSBeanPostProcessor implements BeanPostProcessor, SwitchControl {

  @Autowired
  private UniClientFacade uniClientFacade;

  @Autowired
  private DomainDefine domainDefine;

  private int perQueryTenancyCount = 10;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    if (!isOn()) {
      return bean;
    }
    String currentDomainCode = domainDefine.getDomainCode();
    if (bean instanceof FilterSecurityInterceptor) {
      CheckDomainDefine.checkDomainDefine(currentDomainCode);

      FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) bean;
      // note: access public secure object is not allowed, this is a bit too overkilled if set
      // to be true
      filterSecurityInterceptor
          .setRejectPublicInvocations(domainDefine.isRejectPublicInvocations());
      FilterInvocationSecurityMetadataSource securityMetadataSource = filterSecurityInterceptor
          .getSecurityMetadataSource();
      if (securityMetadataSource instanceof ExpressionBasedFilterInvocationSecurityMetadataSource) {
        ExpressionBasedFilterInvocationSecurityMetadataSource expressionSecurityMetadataSource =
            (ExpressionBasedFilterInvocationSecurityMetadataSource) securityMetadataSource;

        @SuppressWarnings("unchecked")
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> originRequestMap =
            (LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>) ReflectionUtils
                .getField(expressionSecurityMetadataSource, "requestMap", true);

        composeMetadataSource(filterSecurityInterceptor, originRequestMap, currentDomainCode);

        new RefreshDomainResourceThread(filterSecurityInterceptor, originRequestMap,
            currentDomainCode).start();
      }
    }
    return bean;
  }

  private void composeMetadataSource(FilterSecurityInterceptor filterSecurityInterceptor,
      LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> originRequestMap,
      String currentDomainCode) {
    Map<Long, LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> configedReuqestMap = getAppendMap(
        currentDomainCode);
    filterSecurityInterceptor.setSecurityMetadataSource(
        new SSExpressionSecurityMetadataSource(originRequestMap, configedReuqestMap));
  }

  private Map<Long, LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> getAppendMap(
      String currentDomainCode) {
    TenancyParam tenancyParam = new TenancyParam();
    tenancyParam.setStatus(AppConstants.STATUS_ENABLED);
    List<Long> enableTenancyIds = new ArrayList<>();
    while (true) {
      try {
        Response<List<TenancyDto>> enableTenancys = uniClientFacade.getTenancyResource()
            .searchTenancy(tenancyParam);
        if (enableTenancys.getInfo() != null && !enableTenancys.getInfo().isEmpty()) {
          log.error("failed to  query enable tenancy ids");
          break;
        }
        List<TenancyDto> tenancys = enableTenancys.getData();
        Set<Long> tids = new HashSet<>();
        for (TenancyDto tenancy : tenancys) {
          tids.add(tenancy.getId());
        }
        log.info("success query enable tenancys" + tids);
        enableTenancyIds.addAll(tids);
        break;
      } catch (Exception e) {
        log.warn("The uniauth-server[" + uniClientFacade.getUniWsEndpoint()
            + "] not completely started yet, need sleeping for 2 seconds, then retry.", e);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
      }
    }
    int startIndex = 0;
    DomainParam domainParam = new DomainParam();
    domainParam.setCode(currentDomainCode);
    Map<Long, List<UrlRoleMappingDto>> allUrlRoleMappings = new HashMap<>();
    while (true) {
      try {
        if (startIndex >= enableTenancyIds.size()) {
          break;
        }
        int endIndex =
            (startIndex + perQueryTenancyCount) > enableTenancyIds.size() ? (enableTenancyIds.size()
                - startIndex) : perQueryTenancyCount;
        List<Long> includeTenancyIds = enableTenancyIds.subList(startIndex, endIndex);
        domainParam.setIncludeTenancyIds(includeTenancyIds);
        Response<List<UrlRoleMappingDto>> response = uniClientFacade.getPermissionResource()
            .getUrlRoleMapping(domainParam);
        // query error
        if (response.getInfo() != null && !response.getInfo().isEmpty()) {
          throw new UniauthCommonException("failed to getUrlRoleMapping");
        }
        List<UrlRoleMappingDto> urlRoleMappings = response.getData();
        if (urlRoleMappings != null) {
          for (UrlRoleMappingDto urlRoleMaping : urlRoleMappings) {
            Long tenancyId = urlRoleMaping.getTenancyId();
            List<UrlRoleMappingDto> domainUrlRoleMappings = allUrlRoleMappings.get(tenancyId);
            if (domainUrlRoleMappings == null) {
              domainUrlRoleMappings = new ArrayList<>();
              allUrlRoleMappings.put(tenancyId, domainUrlRoleMappings);
            }
            domainUrlRoleMappings.add(urlRoleMaping);
          }
        }
        startIndex = startIndex + includeTenancyIds.size();
      } catch (Exception e) {
        log.warn("The uniauth-server[" + uniClientFacade.getUniWsEndpoint()
            + "] not completely started yet, need sleeping for 2 seconds, then retry.", e);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
      }
    }
    return convert2StandardMap(allUrlRoleMappings);
  }


  private Map<Long, LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> convert2StandardMap(
      Map<Long, List<UrlRoleMappingDto>> allUrlRoleMappings) {
    Map<Long, LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> allMaps = new ConcurrentHashMap<>();
    Set<Long> tenancyIds = allUrlRoleMappings.keySet();
    for (Long tenancyId : tenancyIds) {
      List<UrlRoleMappingDto> urlRoleMappingList = allUrlRoleMappings.get(tenancyId);
      LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> appendMap = new LinkedHashMap<>();
      SpelExpressionParser spelParser = new SpelExpressionParser();
      Map<SSUrlAndMethod, Set<String>> plainMap = new HashMap<>();
      for (UrlRoleMappingDto urlRoleMappingDto : urlRoleMappingList) {
        String permUrl = urlRoleMappingDto.getPermUrl();
        String roleCode = urlRoleMappingDto.getRoleCode();
        String httpMethod = urlRoleMappingDto.getHttpMethod();

        SSUrlAndMethod urlAndMethod = new SSUrlAndMethod();
        urlAndMethod.setHttpMethod(httpMethod);
        urlAndMethod.setPermUrl(permUrl);

        Set<String> roleCodeSet = plainMap.get(urlAndMethod);
        if (roleCodeSet == null) {
          roleCodeSet = new HashSet<>();
          roleCodeSet.add(roleCode);
          plainMap.put(urlAndMethod, roleCodeSet);
        } else {
          roleCodeSet.add(roleCode);
        }
      }

      Iterator<Entry<SSUrlAndMethod, Set<String>>> plainIterator = plainMap.entrySet().iterator();
      while (plainIterator.hasNext()) {
        Entry<SSUrlAndMethod, Set<String>> plainEntry = plainIterator.next();
        SSUrlAndMethod urlAndMethod = plainEntry.getKey();
        String permUrl = urlAndMethod.getPermUrl();
        String httpMethod = urlAndMethod.getHttpMethod();
        Set<String> plainSet = plainEntry.getValue();

        if (httpMethod != null) {
          httpMethod = httpMethod.trim();
          if (AppConstants.HTTP_METHOD_ALL.equals(httpMethod)) {
            httpMethod = null;
          } else {
            try {
              HttpMethod.valueOf(httpMethod);
            } catch (Exception e) {
              log.warn("'" + httpMethod + "' is not a valid http method.", e);
              httpMethod = null;
            }
          }
        }

        // case insensitive for url
        RegexRequestMatcher rrm = new RegexRequestMatcher(permUrl, httpMethod);

        StringBuilder sb = new StringBuilder();

        String[] plainRoleCodes = plainSet.toArray(new String[0]);
        if (plainRoleCodes.length == 1) {
          sb.append("hasRole('" + plainRoleCodes[0] + "')");
        } else {
          for (int i = 0; i < plainRoleCodes.length; i++) {
            if (i == 0) {
              sb.append("hasAnyRole('" + plainRoleCodes[i] + "',");
            } else if (i == plainRoleCodes.length - 1) {
              sb.append("'" + plainRoleCodes[i] + "')");
            } else {
              sb.append("'" + plainRoleCodes[i] + "',");
            }
          }
        }

        ConfigAttribute weca = constructConfigAttribute(spelParser.parseExpression(sb.toString()));
        List<ConfigAttribute> wecaList = new ArrayList<>();
        wecaList.add(weca);

        appendMap.put(rrm, wecaList);
      }
      allMaps.put(tenancyId, appendMap);
    }
    return allMaps;
  }

  private ConfigAttribute constructConfigAttribute(Expression expression) {
    Class<WebExpressionConfigAttribute> clz = WebExpressionConfigAttribute.class;
    Constructor<?>[] constructors = clz.getConstructors();
    for (int i = 0; i < constructors.length; i++) {
      Constructor<?> constructor = constructors[i];
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length == 1 && Expression.class.isAssignableFrom(parameterTypes[0])) {
        log.debug(
            "current sprint security's WebExpressionConfigAttribute support 1 parameter constructor");
        try {
          return (ConfigAttribute) constructor.newInstance(expression);
        } catch (Exception e) {
          log.error("failed to create WebExpressionConfigAttribute", e);
          throw new UniauthCommonException(
              "failed to create WebExpressionConfigAttribute with 1 parameter constructer", e);
        }
      }
      if (parameterTypes.length == 2 && Expression.class.isAssignableFrom(parameterTypes[0])) {
        log.debug(
            "current sprint security's WebExpressionConfigAttribute support 2 parameter constructor");
        try {
          return (ConfigAttribute) constructor.newInstance(expression, null);
        } catch (Exception e) {
          log.error("failed to create WebExpressionConfigAttribute", e);
          throw new UniauthCommonException(
              "failed to create WebExpressionConfigAttribute with 2 parameter constructer", e);
        }
      }
    }
    throw new UniauthCommonException("WebExpressionConfigAttribute can not supported ");
  }

  private class RefreshDomainResourceThread extends Thread {

    private FilterSecurityInterceptor filterSecurityInterceptor;
    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> originRequestMap;
    private String currentDomainCode;

    public RefreshDomainResourceThread(FilterSecurityInterceptor filterSecurityInterceptor,
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> originRequestMap,
        String currentDomainCode) {
      this.filterSecurityInterceptor = filterSecurityInterceptor;
      this.originRequestMap = originRequestMap;
      this.currentDomainCode = currentDomainCode;
    }

    @Override
    public void run() {
      while (true) {
        try {
          sleep(10L * 60 * 1000);
        } catch (InterruptedException e) {
          log.error("RefreshDomainResourceThread error.", e);
          Thread.currentThread().interrupt();
        }
        composeMetadataSource(filterSecurityInterceptor, originRequestMap, currentDomainCode);
        log.info("Refresh domain resource completed at " + new Date() + " .");
      }
    }
  }

  @Override
  public boolean isOn() {
    return domainDefine.controlTypeSupport(CasPermissionControlType.URI_PATTERN);
  }

  public int getPerQueryTenancyCount() {
    return perQueryTenancyCount;
  }

  public void setPerQueryTenancyCount(int perQueryTenancyCount) {
    this.perQueryTenancyCount = perQueryTenancyCount;
  }
}
