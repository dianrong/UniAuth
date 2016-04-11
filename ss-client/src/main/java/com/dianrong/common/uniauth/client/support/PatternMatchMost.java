package com.dianrong.common.uniauth.client.support;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;

public class PatternMatchMost {
	private static final Pattern EXCLUDE_CHARS_PATTERN = Pattern.compile("[(\\)(/)(\\|)(\\,)(\\[)(\\])(\\{)(\\})(\\()(\\))(\\^)(\\$)(\\.)(\\-)(\\&)(\\?)(\\*)(\\+)(\\\\s)(\\\\S)(\\\\d)(\\\\D)(\\\\w)(\\\\W)]");
	private static Logger LOGGER = LoggerFactory.getLogger(PatternMatchMost.class);
	
	private PatternMatchMost(){
		
	}
	
	//not perfect
	public static RequestMatcher findMachMostRequestMatcher(HttpServletRequest request, Map<RequestMatcher, Collection<ConfigAttribute>> allMatchedMap){
		RequestMatcher matchMostRequestMatcher = null;
		RequestMatcher matchedAnyRequestMatcher = null;
		int matchLength = -1;
		
		Iterator<Entry<RequestMatcher, Collection<ConfigAttribute>>> allMatchedIterator = allMatchedMap.entrySet().iterator();
		while(allMatchedIterator.hasNext()){
			Entry<RequestMatcher, Collection<ConfigAttribute>> matchedEntry = allMatchedIterator.next();
			RequestMatcher requestMatcher = matchedEntry.getKey();
			if(requestMatcher instanceof AntPathRequestMatcher){
				AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher)requestMatcher;
				String pattern = antPathRequestMatcher.getPattern();
				String baseOfPattern = EXCLUDE_CHARS_PATTERN.matcher(pattern).replaceAll("");
				int length = baseOfPattern.length();
				if(length > matchLength){
					matchMostRequestMatcher = antPathRequestMatcher;
					matchLength = length;
				}
			}
			else if(requestMatcher instanceof RegexRequestMatcher){
				RegexRequestMatcher regexRequestMatcher = (RegexRequestMatcher)requestMatcher;
				String pattern = ((Pattern)(ReflectionUtils.getField(regexRequestMatcher, "pattern", false))).pattern();
				String baseOfPattern = EXCLUDE_CHARS_PATTERN.matcher(pattern).replaceAll("");
				int length = baseOfPattern.length();
				if(length > matchLength){
					matchMostRequestMatcher = regexRequestMatcher;
					matchLength = length;
				}
			}
			else if(requestMatcher instanceof AnyRequestMatcher){
				matchedAnyRequestMatcher = (AnyRequestMatcher)requestMatcher;
			}
		}
		matchMostRequestMatcher = matchMostRequestMatcher == null? matchedAnyRequestMatcher : matchMostRequestMatcher;
		
		if(allMatchedMap.size() >= 1){
			String url = ExtractRequestUrl.extractRequestUrl(request);
			if(LOGGER.isWarnEnabled()){
				LOGGER.warn("Found at least one pattern <" + allMatchedMap + "> matching <" + url +">, choose <" + matchMostRequestMatcher + ">");
			}
		}
		
		return matchMostRequestMatcher;
	}
}
