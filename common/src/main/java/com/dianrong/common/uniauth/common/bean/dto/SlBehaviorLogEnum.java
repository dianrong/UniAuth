package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;

/**.
 * SlBehavior 相关的枚举类型定义
 * @author wanglin
 */
public class SlBehaviorLogEnum {
	
	/**
	   * BehaviorLog操作类型
	   * 
	   * @version 2015年9月24日 下午5:06:48
	   * @author huaming.zhang
	   * */
	  public enum Type {

	    /**
	     * 注册时获取短信校验码
	     * */
	    FETCH_CODE_FOR_REGISTER((short) 1, 2 * 60 * 1000l, 2),

	    /** 登录时根据用户ID控制 , 5分钟内连续5次 */
	    LOGIN_USER_ID((short) 2, 5 * 60 * 1000l, 5),

	    /** 登录时根据用户IP控制 , 和LOGIN_USER_ID使用同样的value , 30分钟内累计尝试失败20次 */
	    LOGIN_IP((short) 2, 30 * 60 * 1000l, 20), ;

	    private short value; // 用于标识
	    private int limitInTimeUnit = 5; // 在每个时间单位内的请求限制,默认5次
	    private long timeUnit = 60 * 1000l; // 时间单位,单位为毫秒,默认60秒

	    Type(short value) {
	      this.value = value;
	    }

	    Type(short value, long timeUnit, int limitInTimeUnit) {
	      this.value = value;
	      this.timeUnit = timeUnit;
	      this.limitInTimeUnit = limitInTimeUnit;
	    }

	    public short getValue() {
	      return value;
	    }

	    public int getLimitInTimeUnit() {
	      return limitInTimeUnit;
	    }

	    public long getTimeUnit() {
	      return timeUnit;
	    }

	    /**
	     * 获取起始时间，当前时间-timeunit;
	     * */
	    public Date getStartDate() {
	      return new Date(System.currentTimeMillis() - this.getTimeUnit());
	    }

	    /**
	     * 判断是否超过限制<br/>
	     * 
	     * @return true 超过，false 未超过
	     * */
	    public boolean judgeIsOverLimit(long times) {
	      return times >= this.getLimitInTimeUnit();
	    }
	  }
	  
	
	/**.
	 * 返回结果枚举
	 * @author wanglin
	 */
	public static enum Result {
	    /**
	     * 失败
	     * */
	    FAIL((short) 1),

	    /**
	     * 成功
	     * */
	    SUCC((short) 2),

	    ;

	    private short value;

	    Result(short value) {
	      this.value = value;
	    }

	    public short getValue() {
	      return value;
	    }
	  }
}
