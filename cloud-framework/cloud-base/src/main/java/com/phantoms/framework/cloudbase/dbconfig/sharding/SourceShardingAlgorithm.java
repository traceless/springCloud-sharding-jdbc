package com.phantoms.framework.cloudbase.dbconfig.sharding;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import com.phantoms.framework.cloudbase.dbconfig.model.DbConfig;
import com.phantoms.framework.cloudbase.dbconfig.model.ModuleConfig;
import com.phantoms.framework.cloudbase.util.BinarySearch;
import com.phantoms.framework.cloudbase.util.SnowflakeIdFactory;

/**
 * 根据表规则路由到具体的数据库名 由于涉及到垂直分库的问题，
 * A1库-user，A2库-user，B1库-order，B2库-order，不同的库不能进行join
 *  根据column值定位具体的库  
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月6日 	Created
 *
 * </pre>
 * @since 1.
 */
@Slf4j
public class SourceShardingAlgorithm<T extends Comparable<?>> extends AbstractShardingAlgorithm<T> {

    private int[]              positions;

    private ModuleConfig       moduleConfig; // 备用

    private Collection<String> columnSet;

    /**
     * 当前表对应的分布式数据库列表
     */
    private List<DbConfig>     dataSourcelist;

    public SourceShardingAlgorithm(Collection<String> columnSet, ModuleConfig moduleConfig,
                                   List<DbConfig> dataSourcelist){
        positions = new int[dataSourcelist.size()];
        int index = 0;
        // 请确保dataSourcelist 是按positions排序的。
        dataSourcelist = dataSourcelist.stream()
            .sorted(Comparator.comparing(DbConfig::getPosition))
            .collect(Collectors.toList());
        for (DbConfig config : dataSourcelist) {
            positions[index++] = config.getPosition();
        }
        this.dataSourcelist = dataSourcelist;
        this.columnSet = columnSet;
    }

    @Override
    public Collection<String> getShardingColumns() {
        return columnSet;
    }

    /**
     * 进行分片处理
     */
    @Override
    public String doShardingByAlgorithm(Collection<String> availableTargetNames, PreciseShardingValue<T> shardingValue) {
        Long value = 0L;
        try {
            value = Long.parseLong(shardingValue.getValue().toString());
        } catch (NumberFormatException e) {
            String val = shardingValue.getValue().toString();
            value = stringPreciseAlgorithm(val);
        }
        value = executePreciseAlgorithm(value);
        int positton = BinarySearch.searchRegion(positions, value);// 定位在哪个区域数据库上
        String name = dataSourcelist.get(positton).getName();
        log.debug("doSharding dataSource name: {} - {}", name, value);
        return name;
    }
    
    /**
     * 假如传进的字段值是字符串，那么需要实现这部分逻辑，这里简单的区第一个字符作为计算
     * 数据存进去的时候也要跟这里一致，否则无法定位。建议重写这部分逻辑
     * @param colVal
     * @return
     */
    public static Long stringPreciseAlgorithm(String colVal) {
    	// 假如传入用户名，userName = 'phantom' 要注意和存进去的时候逻辑一直，否则重写这一部分的算法
        // 单个字母字符的范围是0-127所以要乘以8，刚好在1000范围内；SOURCE_MOD / 127 + 1 = 8，中文就更大了
    	return Long.valueOf(colVal.charAt(0)) * 8; // 向上取整
    }

    /**
     * 分片算法
     */
    private static final int SOURCE_MOD = 1000;

    public long executePreciseAlgorithm(Long value) {
        // 取二进制的最后10位,刚好在1024以内, 这里请结合用户唯一键的生产规则来进行处理。
        return SnowflakeIdFactory.getIntFormLastBit(value, 10) % SOURCE_MOD;
    }

    public static void main(String[] args) {
        String dd = "";
        System.out.println((int) dd.charAt(0));
    }

}
