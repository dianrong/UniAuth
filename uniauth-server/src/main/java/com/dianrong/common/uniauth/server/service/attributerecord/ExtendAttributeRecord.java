package com.dianrong.common.uniauth.server.service.attributerecord;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 处理扩展属性的记录的注解.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExtendAttributeRecord {

  /**
   * 属性值类型.
   */
  RecordType type() ;

  /**
   * 是添加还是删除操作.
   */
  RecordOperate operate();

  /**
   * 主键Id所在的参数位置. 如果要需要使用该参数,需要主动指定<br>
   * 当没有对应的EL表达式(primaryId),该参数才起作用.
   */
  int primaryIdIndex() default -1;
  
  /**
   * 用户id或者组id所在参数列表的位置, 从0开始.<br>
   * 当没有指定EL表达式,该参数才起作用.
   */
  int identityIndex() default 0;
  
  /**
   * 扩展属性id所在参数列表的位置.<br>
   * 当没有指定EL表达式该参数才起作用.
   */
  int extendIdIndex() default 1;
  
  
  /**
   * EL表达式计算primaryId的值.
   */
  String primaryId() default "";
  
  /**
   * EL表达式计算identity的值.
   */
  String identity() default "";
  
  /**
   * EL表达式计算extendId的值.
   */
  String extendId() default "";
  /**
   * 针对哪一种扩展属性的记录操作.
   */
  enum RecordType {
    /**
     * 用户扩展属性值的操作.
     */
    USER, 
    
    /**
     * 组的扩展属性值操作.
     */
    GROUP;
  }

  /**
   * 针对哪一种扩展属性的记录操作.
   */
  enum RecordOperate {
    /**
     * 添加新的属性值.
     */
    ADD, 
    
    /**
     * 更新属性值.
     */
    UPDATE, 
    
    /**
     * 删除属性值.
     */
    DELETE;
  }
}
