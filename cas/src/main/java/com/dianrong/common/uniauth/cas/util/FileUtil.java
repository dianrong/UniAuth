package com.dianrong.common.uniauth.cas.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * . 文件处理相关util.主要用于cas的子系统
 * 
 * @author wanglin
 *
 */
public final class FileUtil {

    /** Logger instance. */
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * . 返回webApp文件夹所在路径
     * 
     * @return
     */
    public static String getWebAppLocation() {
        String classesLocation = FileUtil.class.getClassLoader().getResource("").getPath();
        File tclasses = new File(classesLocation);

        // ../../classes
        return tclasses.getParentFile().getParent();
    }

    /**
     * .
     * 
     * @param relativePathForWebApp 相对于webapp的相对路径
     * @return 读取到的文件流数据
     * @throws IOException 文件读取io异常
     */
    public static byte[] readFiles(String relativePathForWebApp) throws IOException {
        if (relativePathForWebApp == null) {
            relativePathForWebApp = "";
        }
        if (relativePathForWebApp.startsWith(File.separator)) {
            relativePathForWebApp = relativePathForWebApp.substring(1);
        }
        String realFilePath = getWebAppLocation() + File.separator + relativePathForWebApp;
        File file = new File(realFilePath);
        if (file.exists() && file.isFile()) {
            byte[] bytes = new byte[(int) file.length()];
            BufferedInputStream bufferedInputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                int r = bufferedInputStream.read(bytes);
                if (r != file.length())
                    throw new IOException("读取文件不正确");
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            }
            return bytes;
        }
        return null;
    }

    
    /**
     * . it equals loadProperties(filePath , null)
     * @param filePath filePath
     * @return LinkedHashMap<String, String>
     */
    public static Map<String, String> loadProperties(String filePath) {
        return loadProperties(filePath, null);
    }
    
    /**
     * . load map from properties file. if defaultMap is null, new a LinkedHashMap
     * 
     * @param filePath filePath
     * @param defaultMap
     * @return Map<String, String>
     */
    public static Map<String, String> loadProperties(String filePath, Map<String, String> defaultMap) {
        Assert.notNull(filePath, "properties filePath");
        // 读取属性文件a.properties
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            prop.load(in);
            Map<String, String> values = defaultMap;
            if (values == null) {
                values = new HashMap<String, String>();
            }
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                values.put(key, prop.getProperty(key));
            }
            return values;
        } catch (IOException e) {
            logger.warn("failed to read properties from " + filePath, e);
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("failed to close FileInputStream  " + in, e);
                }
            }
        }
    }
}
