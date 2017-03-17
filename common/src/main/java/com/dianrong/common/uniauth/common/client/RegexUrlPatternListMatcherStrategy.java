package com.dianrong.common.uniauth.common.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

/**
 * . 多个正则表达式过滤条件
 * 
 * @author wanglin
 */
public class RegexUrlPatternListMatcherStrategy implements UrlPatternMatcherStrategy {

    /**
     * . 多个正则表达式进行过滤
     */
    private List<Pattern> patterns = new ArrayList<Pattern>();

    /**
     * . 多个正则的分隔符
     */
    private String spilt = ";";

    public RegexUrlPatternListMatcherStrategy() {}

    public RegexUrlPatternListMatcherStrategy(final String pattern) {
        this.setPattern(pattern);
    }

    public boolean matches(final String url) {
        for (Pattern tp : this.patterns) {
            if (tp.matcher(url).find()) {
                return true;
            }
        }
        return false;
    }

    public void setPattern(final String pattern) {
        if (pattern == null) {
            return;
        }
        String[] patternStrs = pattern.split(spilt);
        for (String tpattern : patternStrs) {
            this.patterns.add(Pattern.compile(tpattern));
        }
    }

    public String getSpilt() {
        return spilt;
    }

    public void setSpilt(String spilt) {
        this.spilt = spilt;
    }
}
