package org.springframework.security.web.access.regular;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**
 * regular pattern model
 * @author wanglin
 */
public  class SSRegularPattern {
	private final String method;
	private final Pattern pattern;
	private SSRegularPattern(String method, Pattern pattern) {
		Assert.notNull(method);
		Assert.notNull(pattern);
		this.method = method.trim();
		this.pattern = pattern;
	}
	
	/**
	 * check the specified request with specified request method and requestUrl  can pass 
	 * @param method request method, PUT,GET...
	 * @param requestUrl  requestUrl, /user/add,/domain/search...
	 * @return true or false
	 */
	public boolean permissonCheck(String method, String requestUrl) {
		boolean methodCheck = false;
		if (this.method.equalsIgnoreCase(AppConstants.HTTP_METHOD_ALL)) {
			methodCheck = true;
		} else {
			methodCheck = this.method.equalsIgnoreCase(method);
		}
		if (!methodCheck) {
			return false;
		}
		// check request URL
		return this.pattern.matcher(requestUrl).matches();
	}
	
	/**
	 * build method, return a new RegularPattern instance
	 * @param method can not be null
	 * @param pattern  can not be null
	 * @return
	 * @throws IllegalArgumentException - if the parameter is null
	 */
	public static SSRegularPattern build(String method, Pattern pattern) {
		return new SSRegularPattern(method, pattern);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SSRegularPattern other = (SSRegularPattern) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equalsIgnoreCase(other.method))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (other.pattern == null) {
			return false;
		} else if (!pattern.pattern().equalsIgnoreCase(other.pattern.pattern())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SSRegularPattern [method=" + method + ", pattern=" + pattern + "]";
	}
}