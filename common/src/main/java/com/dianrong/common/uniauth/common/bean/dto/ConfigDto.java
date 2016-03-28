package com.dianrong.common.uniauth.common.bean.dto;

/**
 * Created by Arc on 25/3/2016.
 */
public class ConfigDto {

    private Integer id;
    private String key;
    private String type;
    private String value;
    private byte[] file;

    public Integer getId() {
        return id;
    }

    public ConfigDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ConfigDto setKey(String key) {
        this.key = key;
        return this;
    }

    public String getType() {
        return type;
    }

    public ConfigDto setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ConfigDto setValue(String value) {
        this.value = value;
        return this;
    }

    public byte[] getFile() {
        return file;
    }

    public ConfigDto setFile(byte[] file) {
        this.file = file;
        return this;
    }
}
