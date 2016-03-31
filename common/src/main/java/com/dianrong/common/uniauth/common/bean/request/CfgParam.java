package com.dianrong.common.uniauth.common.bean.request;

/**
 * Created by Arc on 25/3/2016.
 */
public class CfgParam extends PageParam{

    private Integer id;
    private String cfgKey;
    private Integer cfgTypeId;
    private String cfgTypeCode;
    private String value;
    private byte[] file;
    private Boolean needBLOBs;

    public byte[] getFile() {
        return file;
    }

    public CfgParam setFile(byte[] file) {
        this.file = file;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public CfgParam setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCfgTypeId() {
        return cfgTypeId;
    }

    public CfgParam setCfgTypeId(Integer cfgTypeId) {
        this.cfgTypeId = cfgTypeId;
        return this;
    }

    public String getCfgTypeCode() {
        return cfgTypeCode;
    }

    public CfgParam setCfgTypeCode(String cfgTypeCode) {
        this.cfgTypeCode = cfgTypeCode;
        return this;
    }

    public String getCfgKey() {
        return cfgKey;
    }

    public CfgParam setCfgKey(String cfgKey) {
        this.cfgKey = cfgKey;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CfgParam setValue(String value) {
        this.value = value;
        return this;
    }

    public Boolean getNeedBLOBs() {
        return needBLOBs;
    }

    public CfgParam setNeedBLOBs(Boolean needBLOBs) {
        this.needBLOBs = needBLOBs;
        return this;
    }
}
