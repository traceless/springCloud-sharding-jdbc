package com.phantoms.framework.cloudbase.dbconfig.dynamic;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.phantoms.framework.cloudbase.dbconfig.DynamicDataSource;

/**
 * 数据源切面
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年4月28日 	Created
 *
 * </pre>
 * @since 1.
 */
@Component
@Aspect
public class DataSourceAspect {

    /** service层切面 */
    private final String POINT_CUT = DynamicDataSource.POINT_CUT;

    @Pointcut(POINT_CUT)
    private void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint point) {
        // 获取到当前执行的方法名
        String methodName = point.getSignature().getName();
        // 首先根据名字判断,
        boolean isSlave = isSlave(methodName);
        // 然后根据注解DataSource为准,如果有注解那么就是使用DataSource配置的数据库名字
        DynamicSource dataSource = ((MethodSignature) point.getSignature()).getMethod()
            .getAnnotation(DynamicSource.class);
        if (dataSource != null) {
            isSlave = DynamicDataSource.SLAVE.equals(dataSource.value()) ? true : false;
        }
 
        if (isSlave) {
            // 标记为读库
            DynamicDataSource.markSlave();
        } else {
            // 标记为写库
            DynamicDataSource.markMaster();
        }
    }

    /**
     * 判断是否为读库，方法名以query、find、get开头的方法名走从库
     * 
     * @param methodName
     * @return
     */
    private Boolean isSlave(String methodName) {
        return StringUtils.startsWithAny(methodName, "query", "find", "select");
    }

}
