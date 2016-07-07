package com.dianrong.common.uniauth.cas.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**.
 * 定义i18n所有支持的语言
 * @author wanglin
 */
public final class I18nLanguageConstantUtil {
    
    public static final LinkedHashMap<String, String> localeMap = new LinkedHashMap<String, String>(){
        private static final long serialVersionUID = 3608107004937239778L;
        {
            put("zh_CN", "中文");
            put("en_US", "English");
        }
    };
    
    /**.
     * 辅助方法  返回当前系统支持的国际化语言
     * @return
     */
    public static List<I18nContent> getAllI18nLanguages(){
        List<I18nContent>  data = new ArrayList<I18nContent>();
        for(String localeStr : localeMap.keySet()) {
            data.add(new I18nContent(localeStr, localeMap.get(localeStr), UniBundleUtil.isSelected(localeStr)));
        }
        return data;
    }
    
    public static void main(String[] args) {
        System.out.println(getAllI18nLanguages());
    }
    
    public static class I18nContent {
        private String localeStr;
        private String language;
        private boolean isSelected;
        
        public I18nContent(String localeStr, String language, boolean isSelected){
            this.localeStr = localeStr;
            this.language = language;
            this.isSelected = isSelected;
        }
        
        public String getLocaleStr() {
            return localeStr;
        }
        public void setLocaleStr(String localeStr) {
            this.localeStr = localeStr;
        }
        public String getLanguage() {
            return language;
        }
        public void setLanguage(String language) {
            this.language = language;
        }
        public boolean isSelected() {
            return isSelected;
        }
        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
        
        public String toString(){
            return this.localeStr+ ":" + this.language+ ":" +this.isSelected;
        }
    }
}
