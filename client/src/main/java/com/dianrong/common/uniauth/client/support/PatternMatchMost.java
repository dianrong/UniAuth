package com.dianrong.common.uniauth.client.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class PatternMatchMost {
	private static final Pattern EXCLUDE_CHARS_PATTERN = Pattern.compile("[(\\)(/)(\\|)(\\,)(\\[)(\\])(\\{)(\\})(\\()(\\))(\\^)(\\$)(\\.)(\\-)(\\&)(\\?)(\\*)(\\+)(\\\\s)(\\\\S)(\\\\d)(\\\\D)(\\\\w)(\\\\W)]");
	
	private PatternMatchMost(){
		
	}
	
	//not perfect
	public static Pattern findMachMost(Iterator<Pattern> patterns, String requestUrl){
		Pattern matchMostPattern = null;
		int matchLength = -1;
		
		while (patterns.hasNext()) {
			Pattern uriPattern = patterns.next();
			if (uriPattern.matcher(requestUrl).matches()) {
				String pure = EXCLUDE_CHARS_PATTERN.matcher(uriPattern.pattern()).replaceAll("");
				int pureLength = pure.length();
				if(pure.length() > matchLength){
					matchMostPattern = uriPattern;
					matchLength = pureLength;
				}
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
