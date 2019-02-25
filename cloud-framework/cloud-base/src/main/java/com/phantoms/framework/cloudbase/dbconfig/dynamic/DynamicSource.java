package com.phantoms.framework.cloudbase.dbconfig.dynamic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.phantoms.framework.cloudbase.dbconfig.DynamicDataSource;

/**
 * 动态数据库注解
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年4月28日 	Created
 *
 * </pre>
 * @since 1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicSource {

    /**
     * 描述
     * 
     * @return
     */
    public String description() default "DynamicSource description";

    /**
     * DynamicDataSource默认是master
     * 所以这里默认是slave数据库，在确定只有查询的service上注解此类即可
     * 
     * @return
     */
    public String value() default DynamicDataSource.SLAVE;
}
