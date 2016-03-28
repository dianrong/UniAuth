package com.dianrong.common.uniauth.common.bean.request;

/**
 * Created by Arc on 25/3/2016.
 */
public class CfgParam extends PageParam{

    private Integer id;
    private String key;
    private String type;
    private String value;
    private byte[] file;

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

    public String getKey() {
        return key;
    }

    public CfgParam setKey(String key) {
        this.key = key;
        return this;
    }

    public String getType() {
        return type;
    }

    public CfgParam setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CfgParam setValue(String value) {
        this.value = value;
        return this;
    }
}
