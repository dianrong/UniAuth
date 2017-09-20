package com.dianrong.common.uniauth.cas;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorySalesTest {

  @Data private static final class SalesUserInfo {
    private String aid;
    private String grpId;
    private String email;
  }


  @Data private static final class UniauthUserInfo {
    private String uid;
    private String email;
    private String status;
  }

  /**
   * For some pro data change.
   */
  public static void main(String[] args) throws IOException, ParseException {
    List<SalesUserInfo> salesUserInfos = readSalesFiles("D:\\\\sales_history.csv");
    System.out.println(salesUserInfos.size());
    List<UniauthUserInfo> uniauthUserInfos = readUniauthFiles("D:\\\\email.csv");
    System.out.println(uniauthUserInfos.size());
    Map<String, UniauthUserInfo> maps = new HashMap<>(uniauthUserInfos.size());
    for(UniauthUserInfo uui : uniauthUserInfos) {
      String email = uui.getEmail().trim();
      UniauthUserInfo uniauthUserInfo = maps.get(email);
      if (uniauthUserInfo == null) {
        maps.put(email, uui);
      } else {
        String t_status = uui.getStatus();
        if (t_status.equals("0")) {
          if ("0".equals(uniauthUserInfo.getStatus())) {
            continue;
          }
          maps.put(email, uui);
        }
      }
    }
    List<String> contents = new ArrayList<>(salesUserInfos.size());
    for(SalesUserInfo sui: salesUserInfos) {
      StringBuilder sb = new StringBuilder();
      sb.append(sui.getAid()).append(",").append(sui.getGrpId()).append(",").append(sui.getEmail());
      String email = sui.getEmail();
      UniauthUserInfo uniauthUserInfo = maps.get(email);
      if (uniauthUserInfo != null) {
        sb.append(",").append(uniauthUserInfo.getUid());
      }
      contents.add(sb.toString());
    }
    writeFiles("D:\\\\sales_history_new.csv", contents);
  }

  /**
   * 测试读取文件.
   */
  public static List<SalesUserInfo> readSalesFiles(String path) throws IOException {
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

  public static List<UniauthUserInfo> readUniauthFiles(String path) throws IOException {
    List<UniauthUserInfo> infoList = Lists.newArrayList();
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
            UniauthUserInfo suf = new UniauthUserInfo();
            suf.setUid(items[0]);
            suf.setEmail(items[1]);
            suf.setStatus(items[2]);
            infoList.add(suf);
          }
        }
      } catch (IOException ex) {
        throw ex;
      }
    }
    return infoList;
  }

  /**
   * 测试读取文件.
   */
  public static void writeFiles(String path, List<String> content) throws IOException {
    if (CollectionUtils.isEmpty(content)){
      return;
    }
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
    file.createNewFile();
    try (FileWriter writer = new FileWriter(file);
        BufferedWriter bufferedReader = new BufferedWriter(writer);) {
      for(String line : content) {
        if(!StringUtils.hasText(line)) {
          continue;
        }
        bufferedReader.write(line);
        bufferedReader.write("\r\n");
      }
    } catch (IOException ex) {
      throw ex;
    }
  }
}
