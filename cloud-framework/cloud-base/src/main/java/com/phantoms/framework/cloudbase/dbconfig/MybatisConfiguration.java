package com.phantoms.framework.cloudbase.dbconfig;

import java.sql.SQLException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.pagehelper.PageInterceptor;

/**
 * 这里使用默认扫描方式即可，mybatis默认扫描项目下带有@Mapper注解的接口，如果指定basePackages 则代理包下面的所有接口
 * 如果有多个数据源，那么不同mapper对应不同的sqlSessionFactory和sqlSessionTemplate，
 * 多个sqlSessionFactoryBean时，需要多个sqlSessionTemplate指定sqlSessionFactoryBean
 * @MapperScan 应当准确指定sqlSessionTemplate而不是SqlSessionFactoryRef，否则报错，basePackages不支持通配符等表达式
 * @MapperScan(basePackages = { "com.phantoms.helper.user.mapper", "com.phantoms.helper.common.mapper" }, sqlSessionTemplateRef = "test1SqlSessionTemplate")
 * 可以使用多个MapperScannerConfigurer分别绑定多个sqlSessionFactory, 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年11月30日 	Created
 *
 * </pre>
 * @since 1.
 */
@Configuration
@Slf4j
public class MybatisConfiguration {

    @Value("${mybatis.typeAliasesPackage:com.phantoms.helper.model}")
    private String typeAliasesPackage;
    
    /**
     * 使用动态数据源
     * TODO Add comments here.
     * @param clusterDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Autowired DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        // mybatis分页
        PageInterceptor pageHelper = new PageInterceptor();
        Properties props = new Properties();
        props.setProperty("helperDialect", "mysql");// 5.0+ 改用helperDialect
        props.setProperty("reasonable", "true");
        props.setProperty("supportMethodsArguments", "true");
        props.setProperty("returnPageInfo", "check");
        props.setProperty("params", "count=countSql");
        pageHelper.setProperties(props); // 添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[] { pageHelper });
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*Mapper.xml"));
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);// 设置typeAliases 扫描包,多个用逗号或者分号隔开，不支持模糊扫描
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mapper/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }
    

    @Bean(name = "clusterTransactionManager")
    public PlatformTransactionManager transactionManager(@Autowired DynamicDataSource dynamicDataSource) throws SQLException {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
