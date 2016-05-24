package com.dianrong.common.uniauth.common.bean.dto;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SlIpDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5082250132872075687L;
	
	/**.
	 * 构造函数
	 */
	public SlIpDto() {
	    super();
	  }

	  public SlIpDto(String ipAddress, SlIpEnum.Type typ, SlIpEnum.Status s) {
	    super();
	    this.ipAddr = ipAddress;
	    this.type = Short.valueOf(String.valueOf(typ != null ? typ.ordinal() : SlIpEnum.Type.ANY.ordinal()));
	    this.status = Short.valueOf(String.valueOf(s != null ? s.ordinal() : SlIpEnum.Status.ANY.ordinal()));
	  }
	

	/**
     * This field corresponds to the database column SL$IP.ID
     */
    private BigDecimal id;

    /**
     * This field corresponds to the database column SL$IP.USER_ID
     */
    private BigDecimal userId;

    /**
     * This field corresponds to the database column SL$IP.AID
     */
    private BigDecimal aid;

    /**
     * This field corresponds to the database column SL$IP.IP_ADDR
     */
    private String ipAddr;

    /**
     * This field corresponds to the database column SL$IP.CREATE_D
     */
    private Date createD;

    /**
     * This field corresponds to the database column SL$IP.TYPE
     */
    private Short type;

    /**
     * This field corresponds to the database column SL$IP.STATUS
     */
    private Short status;

    /**
     * This field corresponds to the database column SL$IP.FLAGS
     */
    private BigDecimal flags;

    /**
     * This field corresponds to the database column SL$IP.LATITUDE
     */
    private BigDecimal latitude;

    /**
     * This field corresponds to the database column SL$IP.LONGITUDE
     */
    private BigDecimal longitude;

    /**
     * This field corresponds to the database column SL$IP.RISK
     */
    private Short risk;

    /**
     * This field corresponds to the database column SL$IP.CONFIDENCE
     */
    private Short confidence;

    /**
     * This field corresponds to the database column SL$IP.ISP
     */
    private String isp;

    /**
     * This field corresponds to the database column SL$IP.ORG
     */
    private String org;

    /**
     * This field corresponds to the database column SL$IP.DOMAIN
     */
    private String domain;

    /**
     * This field corresponds to the database column SL$IP.NET_TYPE
     */
    private String netType;

    /**
     * This field corresponds to the database column SL$IP.COUNTRY
     */
    private String country;

    /**
     * This field corresponds to the database column SL$IP.REGION
     */
    private String region;

    /**
     * This field corresponds to the database column SL$IP.AREA_CODE
     */
    private String areaCode;

    /**
     * This field corresponds to the database column SL$IP.METRO_CODE
     */
    private String metroCode;

    /**
     * This field corresponds to the database column SL$IP.CITY
     */
    private String city;

    /**
     * This field corresponds to the database column SL$IP.ZIP
     */
    private String zip;

	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public SlIpDto setId(BigDecimal id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the userId
	 */
	public BigDecimal getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public SlIpDto setUserId(BigDecimal userId) {
		this.userId = userId;
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
	public SlIpDto setAid(BigDecimal aid) {
		this.aid = aid;
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
	public SlIpDto setIpAddr(String ipAddr) {
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
	public SlIpDto setCreateD(Date createD) {
		this.createD = createD;
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
	public SlIpDto setType(Short type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the status
	 */
	public Short getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public SlIpDto setStatus(Short status) {
		this.status = status;
		return this;
	}

	/**
	 * @return the flags
	 */
	public BigDecimal getFlags() {
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public SlIpDto setFlags(BigDecimal flags) {
		this.flags = flags;
		return this;
	}

	/**
	 * @return the latitude
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public SlIpDto setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
		return this;
	}

	/**
	 * @return the longitude
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public SlIpDto setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
		return this;
	}

	/**
	 * @return the risk
	 */
	public Short getRisk() {
		return risk;
	}

	/**
	 * @param risk the risk to set
	 */
	public SlIpDto setRisk(Short risk) {
		this.risk = risk;
		return this;
	}

	/**
	 * @return the confidence
	 */
	public Short getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public SlIpDto setConfidence(Short confidence) {
		this.confidence = confidence;
		return this;
	}

	/**
	 * @return the isp
	 */
	public String getIsp() {
		return isp;
	}

	/**
	 * @param isp the isp to set
	 */
	public SlIpDto setIsp(String isp) {
		this.isp = isp;
		return this;
	}

	/**
	 * @return the org
	 */
	public String getOrg() {
		return org;
	}

	/**
	 * @param org the org to set
	 */
	public SlIpDto setOrg(String org) {
		this.org = org;
		return this;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public SlIpDto setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	/**
	 * @return the netType
	 */
	public String getNetType() {
		return netType;
	}

	/**
	 * @param netType the netType to set
	 */
	public SlIpDto setNetType(String netType) {
		this.netType = netType;
		return this;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public SlIpDto setCountry(String country) {
		this.country = country;
		return this;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public SlIpDto setRegion(String region) {
		this.region = region;
		return this;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public SlIpDto setAreaCode(String areaCode) {
		this.areaCode = areaCode;
		return this;
	}

	/**
	 * @return the metroCode
	 */
	public String getMetroCode() {
		return metroCode;
	}

	/**
	 * @param metroCode the metroCode to set
	 */
	public SlIpDto setMetroCode(String metroCode) {
		this.metroCode = metroCode;
		return this;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public SlIpDto setCity(String city) {
		this.city = city;
		return this;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public SlIpDto setZip(String zip) {
		this.zip = zip;
		return this;
	}
	
	// dto中提供的一些工具方法
	
	/**.
	 * 判断两个ip是否一致
	 * @param ip
	 * @return
	 */
	public boolean sameLocationAs(SlIpDto ip) {
	    if (!isEmpty(country) && !isEmpty(ip.getCountry()) && !country.equals(ip.getCountry())) {
	      return false;
	    }
	    if (!isEmpty(region) && !isEmpty(ip.getRegion()) && !region.equals(ip.getRegion())) {
	      return false;
	    }
	    // check city?
	    return true;
	  }
}