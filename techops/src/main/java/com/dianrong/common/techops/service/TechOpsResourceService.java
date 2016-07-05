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

import org.apache.log4j.Logger;

import com.dianrong.common.techops.bean.LangDto;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;

/**
 * 
 * @author dreamlee
 *
 */
public class TechOpsResourceService{
	
	private static final Logger logger = Logger.getLogger(TechOpsResourceService.class);
	
	private static final String BASE_PATH="META-INF/resources/";
	
	private static final ResourceBundle.Control CONTROL = new TechOpsResourceControl();
	
	private  List<LangDto> menuCache = Lists.newArrayList();
	private String appName;
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public Map<String,String> getProperties(Locale locale){
		ResourceBundle bundle = ResourceBundle.getBundle(appName, locale,CONTROL);
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
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			if(menuCache.isEmpty()){
				synchronized (menuCache) {
					if(menuCache.isEmpty()){
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
				}
			}
		} catch (IOException e) {
			logger.error("getLanguageList error", e);
		}
		return menuCache;
	}

}
