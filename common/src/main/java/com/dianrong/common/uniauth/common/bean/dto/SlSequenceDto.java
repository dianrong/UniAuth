package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SlSequenceDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -240906274199570984L;

	/**
     *column SL$SEQUENCE.NAME
     */
    private String name;

    /**
     * column SL$SEQUENCE.CNT
     */
    private BigDecimal cnt;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public SlSequenceDto setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the cnt
	 */
	public BigDecimal getCnt() {
		return cnt;
	}

	/**
	 * @param cnt the cnt to set
	 */
	public SlSequenceDto setCnt(BigDecimal cnt) {
		this.cnt = cnt;
		return this;
	}
}