package com.dianrong.common.uniauth.server.data.entity.transfer;

public class BaseTransferDO {
	
	/**
	 * 迁移状态，(1表示已经迁移，0表示未迁移）
	 */
	private int trStatus;

	public int getTrStatus() {
		return trStatus;
	}

	public void setTrStatus(int trStatus) {
		this.trStatus = trStatus;
	}

}
