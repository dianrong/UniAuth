package com.dianrong.common.uniauth.cas.extend.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.cas.service.TenancyService;

/**.
 * 多租户的系统参数拦截
 * @author wanglin
 *
 */
public class MultiTenancyFilter implements Filter {
	
	private static final String TENANCY_PARAMETER_KEY = "tenancyParameter";
	
	@Autowired
	private TenancyService tenancyService;

	private String tenancyParameterKeys = "tenancy, tenancyCode";
	
	private volatile Set<String> keys;
	
	@PostConstruct
	public void init(){
		setTenancyParameterKeys(this.tenancyParameterKeys);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		setTenancyParameterKeys(filterConfig.getInitParameter(TENANCY_PARAMETER_KEY));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		try {
			boolean isSet = false;
			for(String key : this.keys) {
				String tenancyCode = req.getParameter(key);
				if (tenancyCode != null) {
					TenancyCodeHolder.set(tenancyCode);
					isSet = true;
					break;
				}
			}
			if (!isSet) {
				TenancyCodeHolder.set(tenancyService.getDefaultTenancyCode());
			}
		} finally {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {
	}

	public String getTenancyParameterKeys() {
		return tenancyParameterKeys;
	}

	public void setTenancyParameterKeys(String tenancyParameterKeys) {
		this.tenancyParameterKeys = tenancyParameterKeys;
		Set<String> _keys = new HashSet<String>();
		if (StringUtils.hasText(tenancyParameterKeys)) {
			String[] _tkeys = tenancyParameterKeys.split(",");
			for(String _tkey :  _tkeys) {
				_keys.add(_tkey);
			}
		}
		this.keys = Collections.unmodifiableSet(_keys);
	}
}
