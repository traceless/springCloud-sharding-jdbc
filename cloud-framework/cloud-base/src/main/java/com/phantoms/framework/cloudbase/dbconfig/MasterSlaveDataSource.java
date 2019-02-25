package com.phantoms.framework.cloudbase.dbconfig;

import javax.sql.DataSource;

/**
 * 动态数据源接口
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月5日 	Created
 *
 * </pre>
 * @since 1.
 */
public interface MasterSlaveDataSource {
    
    /**
     * 获取 masterDataSource
     * @return
     */
    DataSource getMasterDataSource();
    
    /**
     * 获取slaveDataSource
     * @return
     */
    DataSource getSlaveDataSource();
}
