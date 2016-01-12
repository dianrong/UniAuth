package com.dianrong.common.uniauth.client.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternMatchMost {
	private static final Pattern EXCLUDE_CHARS_PATTERN = Pattern.compile("[(\\)(/)(\\|)(\\,)(\\[)(\\])(\\{)(\\})(\\()(\\))(\\^)(\\$)(\\.)(\\-)(\\&)(\\?)(\\*)(\\+)(\\\\s)(\\\\S)(\\\\d)(\\\\D)(\\\\w)(\\\\W)]");
	private static Logger LOGGER = LoggerFactory.getLogger(PatternMatchMost.class);
	
	private PatternMatchMost(){
		
	}
	
	//not perfect
	public static Pattern findMachMost(Iterator<Pattern> patterns, String requestUrl){
		Pattern matchMostPattern = null;
		int matchLength = -1;
		List<String> conflicts = new ArrayList<String>();
		
		while (patterns.hasNext()) {
			Pattern uriPattern = patterns.next();
			if (uriPattern.matcher(requestUrl).matches()) {
				String defPattern = uriPattern.pattern();
				String pure = EXCLUDE_CHARS_PATTERN.matcher(defPattern).replaceAll("");
				int pureLength = pure.length();
				if(pure.length() > matchLength){
					matchMostPattern = uriPattern;
					matchLength = pureLength;
					conflicts.add(defPattern);
				}
			}
		}
		
		if(conflicts.size() > 1){
			if(LOGGER.isWarnEnabled()){
				LOGGER.warn("Found more than one pattern<" + Arrays.asList(conflicts) + "> matcing <" + requestUrl +">,choose " + matchMostPattern.pattern());
			}
		}
		
		return matchMostPattern;
	}
	
	public static void main(String[] args) {
		List<Pattern> list = new ArrayList<Pattern>();
		Pattern p1 = Pattern.compile("^/abc/123/def\\d*");
		Pattern p2 = Pattern.compile("^/.*");
		Pattern p3 = Pattern.compile("^/abc/\\d+");
		list.add(p1);
		list.add(p2);
		list.add(p3);
		
		System.out.println(findMachMost(list.iterator(),"/abc/123"));
	}
}
