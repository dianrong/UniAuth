package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

/**
 * . 用于存储某些有时间要求的model
 * 
 * @author R9GBP97
 *
 */
public class DateSessionObjModel<T> implements Serializable {
	/**
	 */
	private static final long serialVersionUID = -5592978188308898593L;

	/**
	 * . 时间戳
	 */
	private long startMilles;

	/**
	 * . 存活时间milles
	 */
	private long lifeMilles;

	/**
	 * . 内容
	 */
	private T content;

	/**
	 * . 构造函数
	 * 
	 * @param content
	 */
	public DateSessionObjModel(T content, long lifeMilles) {
		this.content = content;
		this.startMilles = System.currentTimeMillis();
		this.lifeMilles = lifeMilles < 0 ? 0 : lifeMilles;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	/**
	 * . 判断当前对象是否已过期
	 * 
	 * @param expireMilles
	 * @return
	 */
	public boolean isExpired() {
		long nowMilles = System.currentTimeMillis();
		return nowMilles - startMilles > lifeMilles;
	}
}
