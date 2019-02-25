package com.phantoms.framework.cloudbase.dbconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * 动态数据源 实现类 <Change to the actual description of this class>
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年4月28日 	Created
 *
 * </pre>
 * @since 1.
 */
@Component("dynamicDataSource")
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    // 写库对应的数据源key
    public static final String               MASTER    = "master";

    public static final String               SLAVE     = "slave";

    public static final List<DataSource>     slaveList = new ArrayList<>();

    /// service 切面层
    public static final String               POINT_CUT = "execution(* com.phantoms.helper.*.service..*(..))";

    // 使用ThreadLocal记录当前线程的数据源key
    private static final ThreadLocal<String> holder    = new ThreadLocal<String>();

    private static int                       slaveSize;

    private static long                      count     = 0;

    public DynamicDataSource(@Autowired MasterSlaveDataSource masterSlaveDataSource) throws Exception{
        Map<Object, Object> map = new HashMap<>();
        map.put(MASTER, masterSlaveDataSource.getMasterDataSource());
        // slaveList.addAll(slavelist); // 可以添加一组slave
        slaveList.add(masterSlaveDataSource.getSlaveDataSource());
        slaveSize = slaveList.size();
        for (int i = 0; i < slaveSize; i++) {
            map.put(SLAVE + i, slaveList.get(i));
        }
        super.setTargetDataSources(map);
        super.setDefaultTargetDataSource(map.get(MASTER));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 使用ThreadLocal 保证线程安全，并且得到当前线程中的数据源key
        return DynamicDataSource.getDataSourceKey();
    }

    /**
     * 获取数据源key
     * 
     * @return
     */
    public static String getDataSourceKey() {
        log.debug("---getDataSourceKey:{}", holder.get());
        return holder.get();
    }

    /**
     * 标记写库
     */
    public static void markMaster() {
        holder.set(MASTER);
    }

    /**
     * 标记读库, 如果有多个slave库，可以轮询 
     * count++ 非线程安全
     */
    public static synchronized void markSlave() {
        holder.set(SLAVE + count++ % slaveSize);
    }
}
