package com.dianrong.common.uniauth.cas.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

public class GenerateSql {

    public static void main(String[] args) throws IOException {
        String filePath = "file.txt";
        StringBuilder sql = new StringBuilder("INSERT INTO uniauth.user_grp values");
        String grpId = "100926";
        String type = "0";
        List<String> lists = readFiles(filePath);
        System.out.println(lists);
//        boolean init = true;
//        for (String id : lists) {
//            if (init) {
//                init = false;
//            } else {
//                sql.append(",");
//            }
//            sql.append("(");
//            sql.append(id);
//            sql.append(",");
//            sql.append(grpId);
//            sql.append(",");
//            sql.append(type);
//            sql.append(")");
//        }
//        sql.append(";");
//        System.out.println(sql);
    }

    public static List<String> readFiles(String realFilePath) throws IOException {
        File file = new File(realFilePath);
        List<String> lists = Lists.newArrayList();
        if (file.exists()) {
            BufferedReader bufferedInputStream = null;
            FileReader fileInputStream = null;
            try {
                fileInputStream = new FileReader(file);
                bufferedInputStream = new BufferedReader(fileInputStream);
                while (true) {
                    String id = bufferedInputStream.readLine();
                    if (id == null || id.isEmpty()) {
                        break;
                    }
                    lists.add(id);
                }
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }
        return lists;
    }
}
