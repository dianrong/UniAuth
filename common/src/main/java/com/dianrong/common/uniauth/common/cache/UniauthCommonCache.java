package com.dianrong.common.uniauth.common.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 
 * @author dreamlee
 *
 */
public class UniauthCommonCache {
	
//	private Map<String,CacheLine> caches = Maps.newConcurrentMap();
	
	@SuppressWarnings("rawtypes")
	private CacheStore<CacheLine> cacheStore = new LocalCacheStore<>();
	
	private static final UniauthCommonCache instance = new UniauthCommonCache();
	
	private UniauthCommonCache(){
	}
	
	public static UniauthCommonCache getInstance(){
		return instance;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T get(String key){
		CacheLine cacheLine = cacheStore.get(key);
		if(cacheLine != null){
			return (T)cacheLine.getValue();
		}
		return null;
	}
	
	
	public <T> T get(String key,Callable<T> getter) throws Exception{
		return get(key, -1, getter);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key,long expireTime,Callable<T> getter) throws Exception{
		CacheLine<T> cache = cacheStore.get(key);
		if(cache == null ||(cache.getExpireTime() !=-1 && new Date().getTime()-cache.getCreateTime()>cache.getExpireTime())){
			T value = getter.call();
			cache=new CacheLine<>();
			cache.setKey(key);
			cache.setValue(value);
			cache.setExpireTime(expireTime);
			cache.setCreateTime(new Date().getTime());
			cacheStore.save(key, cache,expireTime);
		}
		
		return cache.getValue();
	}
	
	
	/**
	 * 
	 * @param key
	 * @param c
	 * @return
	 */
	public <T> T get(String key,Class<T> c){
		return get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expiretime
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void put(String key,T value,long expiretime){
		CacheLine cacheLine = cacheStore.get(key);
		if(cacheLine != null){
			cacheLine.setValue(value);
			cacheLine.setExpireTime(expiretime);
			cacheLine.setCreateTime(new Date().getTime());
		}else{
			cacheLine = new CacheLine<>();
			cacheLine.setKey(key);
			cacheLine.setValue(value);
			cacheLine.setExpireTime(expiretime);
			cacheLine.setCreateTime(new Date().getTime());
			cacheStore.save(key, cacheLine,expiretime);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public <T> void put(String key,T value){
		put(key, value,-1);
	}
	
	/**
	 * 删除缓存
	 * @param key
	 */
	public void remove(String key){
		cacheStore.remove(key);
	}
	
	/**
	 * 更新缓存创建时间
	 * @param key
	 */
	@SuppressWarnings("rawtypes")
	public void touch(String key){
		CacheLine cacheLine = cacheStore.get(key);
		if(cacheLine != null && cacheLine.getExpireTime() != -1){
			cacheLine.setCreateTime(new Date().getTime());
		}
	}
	
	
	/**
	 * 
	 * @author dreamlee
	 *
	 * @param <T>
	 */
	public static final class CacheLine <T> implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5694542630508908712L;
		private String key;
		private T value;
		private long expireTime;
		private long createTime;
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public T getValue() {
			return value;
		}
		public void setValue(T value) {
			this.value = value;
		}
		public long getExpireTime() {
			return expireTime;
		}
		public void setExpireTime(long expireTime) {
			this.expireTime = expireTime;
		}
		public long getCreateTime() {
			return createTime;
		}
		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}
	}

}
