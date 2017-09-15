package com.dianrong.common.uniauth.cas;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HistorySalesTest {

  @Data private static final class SalesUserInfo {
    private String aid;
    private String grpId;
    private String email;
    private List<String> uid;
  }

  /**
   * For some pro data change.
   */
  public static void main(String[] args) throws IOException, ParseException {
    List<SalesUserInfo> salesUserInfos = readFiles("D:\\\\sales_history.csv");
    StringBuilder sb = new StringBuilder();
    Set<String> emails = new HashSet<>();
    for (SalesUserInfo ss : salesUserInfos) {
      String email = ss.getEmail();
      if (!StringUtils.hasText(email)) {
        return;
      }
      if (emails.contains(email)) {
        System.out.println(email);
        continue;
      }
      emails.add(email);
      sb.append("'").append(email).append("',");
    }
    System.out.println(salesUserInfos.size());
    System.out.println(emails.size());
    System.out.println(sb);
  }

  /**
   * 测试读取文件.
   */
  public static List<SalesUserInfo> readFiles(String path) throws IOException {
    List<SalesUserInfo> infoList = Lists.newArrayList();
    File file = new File(path);
    if (file.exists() && file.isFile()) {
      try (Reader reader = new FileReader(file);
          BufferedReader breader = new BufferedReader(reader);) {
        while (true) {
          String line = breader.readLine();
          if (!StringUtils.hasText(line)) {
            break;
          }
          String[] items = line.split(",");
          if (items != null && items.length == 3) {
            SalesUserInfo suf = new SalesUserInfo();
            suf.setAid(items[0]);
            suf.setGrpId(items[1]);
            suf.setEmail(items[2]);
            infoList.add(suf);
          }
        }
      } catch (IOException ex) {
        throw ex;
      }
    }
    return infoList;
  }
}
