package com.dianrong.common.uniauth.client.support;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class PatternMatchMost {

  private static final Pattern EXCLUDE_CHARS_PATTERN =
      Pattern.compile(
          "[(\\)(\\|)(\\,)(\\[)(\\])(\\{)(\\})(\\()(\\))(\\^)(\\$)(\\.)(\\-)(\\&)"
          + "(\\?)(\\*)(\\+)(\\\\s)(\\\\S)(\\\\d)(\\\\D)(\\\\w)(\\\\W)]");

  private PatternMatchMost() {

  }

  /**
   * 找到最匹配当前请求的权限.
   */
  public static RequestMatcher findMachMostRequestMatcher(HttpServletRequest request,
      Map<RequestMatcher, Collection<ConfigAttribute>> allMatchedMap) {
    String url = ExtractRequestUrl.extractRequestUrl(request);
    int questionPos = url.indexOf('?');

    RequestMatcher matchMostRequestMatcher = null;
    RequestMatcher matchedAnyRequestMatcher = null;
    int matchLength = -1;
    int originLength = -1;

    Iterator<Entry<RequestMatcher, Collection<ConfigAttribute>>> allMatchedIterator = allMatchedMap
        .entrySet().iterator();
    while (allMatchedIterator.hasNext()) {
      Entry<RequestMatcher, Collection<ConfigAttribute>> matchedEntry = allMatchedIterator.next();
      RequestMatcher requestMatcher = matchedEntry.getKey();
      if (requestMatcher instanceof AntPathRequestMatcher) {
        AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
        String pattern = antPathRequestMatcher.getPattern();

        // ant pattern exclude the ? and querystring parts when checking request url
        if (pattern.equals(url.subSequence(0, questionPos == -1 ? url.length() : questionPos))) {
          matchMostRequestMatcher = antPathRequestMatcher;
          break;
        }

        int baseOriginLength = pattern.length();
        String baseOfPattern = EXCLUDE_CHARS_PATTERN.matcher(pattern).replaceAll("");
        int length = baseOfPattern.length();

        if (length == matchLength) {
          if (baseOriginLength < originLength) {
            matchMostRequestMatcher = antPathRequestMatcher;
            originLength = baseOriginLength;
          }
        } else if (length > matchLength) {
          matchMostRequestMatcher = antPathRequestMatcher;
          matchLength = length;
          originLength = baseOriginLength;
        }
      } else if (requestMatcher instanceof RegexRequestMatcher) {
        RegexRequestMatcher regexRequestMatcher = (RegexRequestMatcher) requestMatcher;
        String pattern = ((Pattern) (ReflectionUtils
            .getField(regexRequestMatcher, "pattern", false))).pattern();

        if (pattern.equals(url)) {
          matchMostRequestMatcher = regexRequestMatcher;
          break;
        }

        int baseOriginLength = pattern.length();
        // 过滤掉正则表达式的模糊匹配字符串，比如/w,*,.,+这些字符串,把pattern过滤成一个正则无关的字符串
        String baseOfPattern = EXCLUDE_CHARS_PATTERN.matcher(pattern).replaceAll("");
        int length = baseOfPattern.length();

        if (length == matchLength) {
          if (baseOriginLength < originLength) {
            matchMostRequestMatcher = regexRequestMatcher;
            originLength = baseOriginLength;
          }
        } else if (length > matchLength) {
          matchMostRequestMatcher = regexRequestMatcher;
          matchLength = length;
          originLength = baseOriginLength;
        }
      } else if (requestMatcher instanceof AnyRequestMatcher) {
        matchedAnyRequestMatcher = (AnyRequestMatcher) requestMatcher;
      }
    }
    matchMostRequestMatcher =
        matchMostRequestMatcher == null ? matchedAnyRequestMatcher : matchMostRequestMatcher;

    int mapSize = allMatchedMap.size();

    if (mapSize >= 1) {
      if (log.isWarnEnabled()) {
        String pattern = "";
        if (matchMostRequestMatcher instanceof RegexRequestMatcher) {
          pattern = ((Pattern) (ReflectionUtils
              .getField(matchMostRequestMatcher, "pattern", false))).pattern();
          pattern = "(" + pattern + ")";
        }
        log.info("Found " + mapSize + " patterns <" + allMatchedMap + "> matching <" + url
            + ">, choose <" + matchMostRequestMatcher + pattern + ">");
      }
    }

    return matchMostRequestMatcher;
  }
}
