package com.dianrong.common.uniauth.cas.extend;


import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.CookieValueManager;
import org.jasig.cas.web.support.NoOpCookieValueManager;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.dianrong.common.uniauth.cas.extend.filter.TenancyCodeHolder;

/**.
 * 多租户的tgt的cookie管理工具
 * @author wanglin
 */
public class MultiTenancyTgtCookieGenerator extends CookieRetrievingCookieGenerator{
	/**.
	 * 日志对象
	 */
	private static final Logger logger =Logger.getLogger(MultiTenancyTgtCookieGenerator.class);
	
    private static final int DEFAULT_REMEMBER_ME_MAX_AGE = 7889231;

    private int rememberMeMaxAge = DEFAULT_REMEMBER_ME_MAX_AGE;
	
	private final  ConcurrentHashMap<String, TenancyTgtCookieGenerator> cookieGenerators;
	
	private final CookieValueManager casCookieValueManager;
	
    public MultiTenancyTgtCookieGenerator() {
        this(new NoOpCookieValueManager());
    }

    /**
     * Instantiates a new Cookie retrieving cookie generator.
     * @param casCookieValueManager the cookie manager
     */
    public MultiTenancyTgtCookieGenerator(final CookieValueManager casCookieValueManager) {
        cookieGenerators = new ConcurrentHashMap<String, TenancyTgtCookieGenerator>();
        final Method setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
        if(setHttpOnlyMethod != null) {
            super.setCookieHttpOnly(true);
        } else {
            logger.debug("Cookie cannot be marked as HttpOnly; container is not using servlet 3.0.");
        }
        this.casCookieValueManager = casCookieValueManager;
    }
    
    public void setRememberMeMaxAge(final int maxAge) {
        this.rememberMeMaxAge = maxAge;
    }
    
    @Override
    public void addCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieValue) {
    	getCookieGenerator(TenancyCodeHolder.get()).addCookie(request, response, cookieValue);
    }

    @Override
    public void removeCookie(final HttpServletResponse response) {
    	 getCookieGenerator(TenancyCodeHolder.get()).removeCookie(response);
    }

    @Override
    public String retrieveCookieValue(final HttpServletRequest request) {
        return getCookieGenerator(TenancyCodeHolder.get()).retrieveCookieValue(request);
    }

    private TenancyTgtCookieGenerator getCookieGenerator(String tenancyCode) {
    	Assert.notNull(tenancyCode);
    	TenancyTgtCookieGenerator generator =  cookieGenerators.get(tenancyCode);
    	if (generator == null) {
    		cookieGenerators.putIfAbsent(tenancyCode, constructNewGenerator(tenancyCode));
    		generator = cookieGenerators.get(tenancyCode);
    	}
    	return generator;
    }
    
    /**.
     * 构造新的tenancy的cookie generator
     * @param tenacyCode
     * @return
     */
    private TenancyTgtCookieGenerator constructNewGenerator(final String tenacyCode) {
    	TenancyTgtCookieGenerator tgtcookieGenerator = new TenancyTgtCookieGenerator(this.casCookieValueManager, tenacyCode);
    	tgtcookieGenerator.setRememberMeMaxAge(this.rememberMeMaxAge);
    	tgtcookieGenerator.setCookieName(this.getCookieName());
    	tgtcookieGenerator.setCookieDomain(this.getCookieDomain());
    	tgtcookieGenerator.setCookieHttpOnly(this.isCookieHttpOnly());
    	tgtcookieGenerator.setCookiePath(this.getCookiePath());
    	tgtcookieGenerator.setCookieSecure(this.isCookieSecure());
    	tgtcookieGenerator.setCookieMaxAge(this.getCookieMaxAge());
    	return tgtcookieGenerator;
    }
    
    class TenancyTgtCookieGenerator extends CookieRetrievingCookieGenerator {
    	private final String tenancyCode;
        public TenancyTgtCookieGenerator(final CookieValueManager casCookieValueManager, final String tenancyCode) {
        	super(casCookieValueManager);
        	Assert.notNull(tenancyCode, "set tenancyCode can not be null");
        	this.tenancyCode = tenancyCode;
        }
		public String getTenancyCode() {
			return tenancyCode;
		}
		@Override
		public String getCookieName() {
			return super.getCookieName() + "_" + this.tenancyCode;
		}
    }
}
