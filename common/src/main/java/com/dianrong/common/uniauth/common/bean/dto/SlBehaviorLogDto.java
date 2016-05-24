package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SlBehaviorLogDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7870305105404824069L;

	/**
     * column SL$BEHAVIOR_LOG.ID
     */
    private BigDecimal id;

    /**
     * column SL$BEHAVIOR_LOG.AID
     */
    private BigDecimal aid;

    /**
     * column SL$BEHAVIOR_LOG.TYPE
     */
    private Short type;

    /**
     * column SL$BEHAVIOR_LOG.TARGET
     */
    private String target;

    /**
     * column SL$BEHAVIOR_LOG.IP_ADDR
     */
    private String ipAddr;

    /**
     * column SL$BEHAVIOR_LOG.CREATE_D
     */
    private Date createD;

    /**
     * column SL$BEHAVIOR_LOG.RESULT
     */
    private Short result;

	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public SlBehaviorLogDto setId(BigDecimal id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the aid
	 */
	public BigDecimal getAid() {
		return aid;
	}

	/**
	 * @param aid the aid to set
	 */
	public SlBehaviorLogDto setAid(BigDecimal aid) {
		this.aid = aid;
		return this;
	}

	/**
	 * @return the type
	 */
	public Short getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public SlBehaviorLogDto setType(Short type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public SlBehaviorLogDto setTarget(String target) {
		this.target = target;
		return this;
	}

	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * @param ipAddr the ipAddr to set
	 */
	public SlBehaviorLogDto setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
		return this;
	}

	/**
	 * @return the createD
	 */
	public Date getCreateD() {
		return createD;
	}

	/**
	 * @param createD the createD to set
	 */
	public SlBehaviorLogDto setCreateD(Date createD) {
		this.createD = createD;
		return this;
	}

	/**
	 * @return the result
	 */
	public Short getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public SlBehaviorLogDto setResult(Short result) {
		this.result = result;
		return this;
	}
}