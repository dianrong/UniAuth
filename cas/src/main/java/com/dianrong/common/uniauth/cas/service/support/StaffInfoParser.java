package com.dianrong.common.uniauth.cas.service.support;

import com.google.common.collect.Lists;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.StringUtils;

@Slf4j
public final class StaffInfoParser {

  public static List<StaffInfo> parse(byte[] data) {
    List<StaffInfo> infoList = Lists.newArrayList();
    if (data == null) {
      log.warn("Staff info is empty");
      return infoList;
    }
    String content;
    try {
      content = new String(data, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.error("Unsupported UTF-8", e);
      return infoList;
    }
    String[] cItems = content.split("\r|\n|\n\r|\r\n");
    for (String cItem : cItems) {
      if (!StringUtils.hasText(cItem.trim())) {
        continue;
      }
      String[] items = cItem.split(",");
      if (items.length != 5) {
        log.warn("Invalid line: {}", cItem);
        continue;
      }
      StaffInfo staffInfo = new StaffInfo();
      staffInfo.setStaffNo(safeTrim(items[0]));
      staffInfo.setStaffName(safeTrim(items[1]));
      staffInfo.setUseEmail(safeTrim(items[2]));
      staffInfo.setCrmEmail(safeTrim(items[3]));
      staffInfo.setEmail(safeTrim(items[4]));
      infoList.add(staffInfo);
    }
    return infoList;
  }

  private static String safeTrim(String str) {
    if (str == null) {
      return null;
    }
    return str.trim();
  }
}
