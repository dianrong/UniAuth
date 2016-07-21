package com.dianrong.common.uniauth.common.cache;

public interface CacheStore <T>{
	
	/**
	 * 保存
	 * @param key
	 * @param value
	 */
	public void save(String key,T value);
	
	/**
	 * 支持过期策略时实现,如果所用的存储不支持，则可直接调用{@link #save(String, Object)}
	 * @param value
	 * @param expireTime
	 */
	public void save(String key,T value,long expireTime);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public T get(String key);
	
	/**
	 * 删除
	 */
	public void remove(String key);

}
