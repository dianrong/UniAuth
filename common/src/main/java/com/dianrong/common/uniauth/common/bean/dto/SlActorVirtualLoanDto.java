package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SlActorVirtualLoanDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6717453033208252177L;

	/**
     *column SL$ACTOR_VIRTUAL_LOAN.AID
     */
    private BigDecimal aid;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.REFERRALS
     */
    private BigDecimal referrals;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.TOTAL_PROFIT
     */
    private BigDecimal totalProfit;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.TOTAL_PROFIT_DATE
     */
    private Date totalProfitDate;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.YESTERDAY_PROFIT
     */
    private BigDecimal yesterdayProfit;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.YESTERDAY_PROFIT_DATE
     */
    private Date yesterdayProfitDate;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.TYPE
     */
    private Short type;

    /**
     *column SL$ACTOR_VIRTUAL_LOAN.VIRTUAL_LOAN_ID
     */
    private BigDecimal virtualLoanId;

	/**
	 * @return the aid
	 */
	public BigDecimal getAid() {
		return aid;
	}

	/**
	 * @param aid the aid to set
	 */
	public SlActorVirtualLoanDto setAid(BigDecimal aid) {
		this.aid = aid;
		return this;
	}

	/**
	 * @return the referrals
	 */
	public BigDecimal getReferrals() {
		return referrals;
	}

	/**
	 * @param referrals the referrals to set
	 */
	public SlActorVirtualLoanDto setReferrals(BigDecimal referrals) {
		this.referrals = referrals;
		return this;
	}

	/**
	 * @return the totalProfit
	 */
	public BigDecimal getTotalProfit() {
		return totalProfit;
	}

	/**
	 * @param totalProfit the totalProfit to set
	 */
	public SlActorVirtualLoanDto setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
		return this;
	}

	/**
	 * @return the totalProfitDate
	 */
	public Date getTotalProfitDate() {
		return totalProfitDate;
	}

	/**
	 * @param totalProfitDate the totalProfitDate to set
	 */
	public SlActorVirtualLoanDto setTotalProfitDate(Date totalProfitDate) {
		this.totalProfitDate = totalProfitDate;
		return this;
	}

	/**
	 * @return the yesterdayProfit
	 */
	public BigDecimal getYesterdayProfit() {
		return yesterdayProfit;
	}

	/**
	 * @param yesterdayProfit the yesterdayProfit to set
	 */
	public SlActorVirtualLoanDto setYesterdayProfit(BigDecimal yesterdayProfit) {
		this.yesterdayProfit = yesterdayProfit;
		return this;
	}

	/**
	 * @return the yesterdayProfitDate
	 */
	public Date getYesterdayProfitDate() {
		return yesterdayProfitDate;
	}

	/**
	 * @param yesterdayProfitDate the yesterdayProfitDate to set
	 */
	public SlActorVirtualLoanDto setYesterdayProfitDate(Date yesterdayProfitDate) {
		this.yesterdayProfitDate = yesterdayProfitDate;
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
	public SlActorVirtualLoanDto setType(Short type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the virtualLoanId
	 */
	public BigDecimal getVirtualLoanId() {
		return virtualLoanId;
	}

	/**
	 * @param virtualLoanId the virtualLoanId to set
	 */
	public SlActorVirtualLoanDto setVirtualLoanId(BigDecimal virtualLoanId) {
		this.virtualLoanId = virtualLoanId;
		return this;
	}
}