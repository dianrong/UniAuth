package com.dianrong.common.uniauth.server.datafilter;

/**
 * . uniauth-server 所有数据过滤的接口
 * 
 * @author wanglin
 */
public interface DataFilter {
    /**
     * . 添加数据的时候，进行单一字段的check(addFieldsCheck的单一字段版)
     * 
     * @param fieldName 过滤的字段名
     * @param fieldValue 对应字段的值
     * @param ftype 过滤的方式
     */
    void addFieldCheck(FilterType ftype, FieldType type, Object fieldValue);

    /**
     * . 添加数据的时候 进行多字段的check
     * 
     * @param ftype
     * @param equalsField
     */
    void addFieldsCheck(FilterType ftype, FilterData... equalsField);

    /**
     * . 根据primaryid 更新记录的时候进行单字段的check(updateFieldsCheck的单字段版)
     * 
     * @param type 字段
     * @param id keyid
     * @param fieldValue 需要新加入的值
     */
    void updateFieldCheck(Integer id, FieldType type, Object fieldValue);

    /**
     * . 根据primaryid 更新记录的时候进行多个字段的check
     * 
     * @param type 字段
     * @param id keyid
     * @param fieldValue 需要新加入的值
     */
    void updateFieldsCheck(Integer id, FilterData... equalsField);
}
