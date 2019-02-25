package com.phantoms.framework.cloudbase.dbconfig;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 请自行实现master和slave数据源，目前用的都是master
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年11月30日 	Created
 *
 * </pre>
 * @since 1.
 */
//@Configuration // 目前交给了ShardingDatasourceConfiguration实现
@Slf4j
public class DruidDataSourceConfiguration implements MasterSlaveDataSource {

    @Autowired
    private ConfigurableEnvironment environment;

    private DataSource              masterDataSource;

    private DataSource              slaveDataSource;

    /**
     * 请自行实现 masterDataSource和slaveDataSource的创建方法
     * 这里是通用的实现，即slaveDataSource = masterDataSource
     * 
     * @return
     * @throws SQLException
     */
    @PostConstruct
    private void init() throws Exception {
        masterDataSource = createDataSource();
        slaveDataSource = createDataSource();
    }
    
    /**
     * 数据源创建方法
     * @return
     * @throws SQLException
     */
    private DruidDataSource createDataSource() throws SQLException {
        Binder binder = Binder.get(environment);
        DruidDataSource druidDataSource = binder.bind("spring.datasource", DruidDataSource.class).get();
        log.info("-------druidDataSource.config--" + druidDataSource.getDriverClassName() + "-datasourceUrl:"
                    + druidDataSource.getUrl() + "" + druidDataSource.getUsername() + "-"
                    + druidDataSource.getPassword() + "-" + druidDataSource.getInitialSize() + "-"
                    + druidDataSource.getMinIdle() + "-" + druidDataSource.getMaxActive() + "-"
                    + druidDataSource.getMaxWait() + "-" + druidDataSource.getTimeBetweenEvictionRunsMillis() + "-"
                    + druidDataSource.getMinEvictableIdleTimeMillis() + "-" + druidDataSource.getValidationQuery()
                    + "-" + druidDataSource.isTestWhileIdle() + "-" + druidDataSource.isTestOnBorrow() + "-"
                    + druidDataSource.isTestOnReturn() + "-" + druidDataSource.isPoolPreparedStatements() + "-"
                    + druidDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        return druidDataSource;
    }

    @Override
    public DataSource getMasterDataSource() {
        // TODO Auto-generated method stub
        return masterDataSource;
    }

    @Override
    public DataSource getSlaveDataSource() {
        // TODO Auto-generated method stub
        return slaveDataSource;
    }

}
