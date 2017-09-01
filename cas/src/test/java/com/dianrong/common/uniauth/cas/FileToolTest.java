package com.dianrong.common.uniauth.cas;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

public class FileToolTest {

    private static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT_4 = "yyyy/MM/dd";

    /**
     * For some pro data change.
     */
    public static void main(String[] args) throws IOException, ParseException {
        DateFormat dateFormat1 = new SimpleDateFormat(DATE_FORMAT_1);
        DateFormat dateFormat4 = new SimpleDateFormat(DATE_FORMAT_4);
        Map<String, String> maps = readFiles("D:\\\\magic-user.csv");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entries : maps.entrySet()) {
            String email = entries.getKey();
            Date date = dateFormat4.parse(entries.getValue());
            String sql = "UPDATE user SET create_date = '"
                    + dateFormat1.format(date) + "' where email ='"
                    + email.trim() + "' and tenancy_id = 1";
            sb.append(sql).append(";\n");
        }
        System.out.println(sb);
    }

    /**
     * 测试读取文件.
     */
    public static Map<String, String> readFiles(String path) throws IOException {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            Map<String, String> nameEmailMap = new LinkedHashMap<>();
            Reader reader = null;
            BufferedReader breader = null;
            try {
                reader = new FileReader(file);
                breader = new BufferedReader(reader);
                while (true) {
                    String line = breader.readLine();
                    if (!StringUtils.hasText(line)) {
                        break;
                    }
                    String[] items = line.split(",");
                    if (items != null && items.length == 2) {
                        String key = nameEmailMap.put(items[0].trim(), items[1].trim());
                        if (key != null) {
                            System.out.println(key);
                        }
                    }
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (breader != null) {
                    breader.close();
                }
            }
            return nameEmailMap;
        }
        return null;
    }
}
