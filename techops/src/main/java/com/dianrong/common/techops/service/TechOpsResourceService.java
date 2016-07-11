package com.dianrong.common.techops.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.dianrong.common.techops.bean.LangDto;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;

/**
 * 
 * @author dreamlee
 *
 */
public class TechOpsResourceService implements InitializingBean{
	
	private static final Logger logger = Logger.getLogger(TechOpsResourceService.class);
	
	private static final String BASE_PATH="META-INF/resources/";
	
	private static final ResourceBundle.Control CONTROL = new TechOpsResourceControl();
	
	private List<LangDto> menuCache = Lists.newArrayList();
	
	/**
	 * 应用名称，以语言包形式加载时必须设置（此配置与path互斥）
	 */
	private String appName;
	
	/**
	 * 资源文件路径，普通加载时必须设置（此配置与appName互斥）
	 */
	private String path;
	
	
	/**
	 * 菜单配置文件的路径(语言包方式加载不需要设置)
	 */
	private String menuPath;
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}

	public Map<String,String> getProperties(Locale locale){
		ResourceBundle bundle = null;
		if(StringUtils.isBlank(appName)){
			bundle = ResourceBundle.getBundle(path, locale);
		}else{
			bundle = ResourceBundle.getBundle(path, locale, CONTROL);
		}
		if(bundle != null){
			Map<String,String> p = Maps.newHashMap();
			Enumeration<String> keys = bundle.getKeys();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				p.put(key, bundle.getString(key));
			}
			return p;
		}
		return Maps.newHashMap();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<LangDto> getLanguageList(){
		try {
			if(menuCache.isEmpty()){
				synchronized (menuCache) {
					if(menuCache.isEmpty()){
						if(StringUtils.isBlank(menuPath)){
							loadFromSpi();
						}else{
							loadFromLocal();
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("getLanguageList error", e);
		}
		return menuCache;
	}

	/**
	 * 加载本地的菜单配置
	 * @throws IOException
	 */
	private void loadFromLocal() throws IOException {
		InputStream is = TechOpsResourceService.class.getClassLoader().getResourceAsStream(menuPath);
		if(is != null){
			try{
				Properties p = new Properties();
				p.load(is);
				for(Object key : p.keySet()){
					menuCache.add(new LangDto(String.valueOf(key), p.getProperty(String.valueOf(key))));
				}
			}finally{
				is.close();
			}
		}
	}

	/**
	 * 从语言包中加载菜单配置
	 * @throws IOException
	 */
	private void loadFromSpi() throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> resources = loader.getResources(BASE_PATH + "menu.properties");
		while(resources.hasMoreElements()){
			URL url = resources.nextElement();
			InputStream is = url.openStream();
			try{
				Properties p = new Properties();
				p.load(is);
				for(Object key : p.keySet()){
					menuCache.add(new LangDto(String.valueOf(key), p.getProperty(String.valueOf(key))));
				}
			}finally{
				is.close();
			}
		}
	}
	
	
	public void init(){
		try {
			afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(appName == null && path == null || appName !=null && path != null){
			throw new RuntimeException("param error!");
		}
	}
	
}
