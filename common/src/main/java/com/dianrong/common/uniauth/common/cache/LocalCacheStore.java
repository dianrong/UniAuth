package com.dianrong.common.uniauth.common.cache;

import java.util.Map;

import com.google.common.collect.Maps;

public class LocalCacheStore<T>  implements CacheStore<T> {
	
	private Map<String,T> caches = Maps.newConcurrentMap();

	@Override
	public void save(String key, T value) {
		caches.put(key, value);
	}

	@Override
	public void save(String key, T value, long expireTime) {
		save(key,value);
	}

	@Override
	public T get(String key) {
		return caches.get(key);
	}

	@Override
	public void remove(String key) {
		caches.remove(key);
	}
	
}
