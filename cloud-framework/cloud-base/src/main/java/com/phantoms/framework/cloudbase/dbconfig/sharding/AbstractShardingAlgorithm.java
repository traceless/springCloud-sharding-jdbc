package com.phantoms.framework.cloudbase.dbconfig.sharding;

import io.shardingjdbc.core.api.algorithm.sharding.ListShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.ShardingValue;
import io.shardingjdbc.core.routing.strategy.ShardingStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.Nullable;

import com.google.common.collect.Range;

/**
 * 基础算法，通用数据库和表 定位
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月7日 	Created
 *
 * </pre>
 * @since 1.
 */
@Slf4j
public abstract class AbstractShardingAlgorithm<T extends Comparable<?>> implements ShardingStrategy {

    /**
     * 算法实现，根据具体column的值获取 数据库名或者表名 TODO Add comments here.
     * 
     * @param availableTargetNames
     * @param preciseValList
     * @return
     */
    public abstract String doShardingByAlgorithm(Collection<String> availableTargetNames,  PreciseShardingValue<T> preciseValList);

    /**
     * 如果多个字段分库出现，那么这个的shardingValues会是多个 注意处理，比如
     * user表跟order表关联，然后出现了各自表的分库字段，如userid和orderid或者日期id等
     * 应该考虑如何选择路由避免数据不出错
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         Collection<ShardingValue> shardingValues) {
        Set<String> listDbOrTable = new HashSet<>();
        shardingValues.forEach(values -> {
            if (values instanceof ListShardingValue<?>) {
                listDbOrTable.addAll(this.doShardingByListShardingValue(availableTargetNames, (ListShardingValue<T>) values));
            } else if (values instanceof RangeShardingValue) {
                listDbOrTable.addAll(this.doShardingByRangeShardingValue(availableTargetNames, (RangeShardingValue<T>) values));
            }
        });
        if (listDbOrTable.size() == 0) {
            log.debug("----- return all availableTargetNames: {} ", availableTargetNames);
            return availableTargetNames; // 返回所有的数据库名或表名
        }
        return listDbOrTable;
    }

    /**
     * 根据column值（in 查询有多个值），返回多个数据库名或表名
     * 此方法一般不需要重写
     * @param availableTargetNames
     * @param preciseValList
     * @return
     */
    protected Set<String> doShardingByListShardingValue(Collection<String> availableTargetNames,
                                                   @Nullable ListShardingValue<T> values) {
        List<PreciseShardingValue<T>> preciseValList =  this.transferToPreciseShardingValues(values);
        Set<String> listDbOrTable = new HashSet<>();
        preciseValList.forEach(preciseVal -> {
            listDbOrTable.add(this.doShardingByAlgorithm(availableTargetNames, preciseVal));
        });
        return listDbOrTable;
    }
    
    
    /**
     * 根据column 范围值，返回多个数据库名或表名，目前返回所有
     * 请自行实现此方法，或者重写transferToPreciseShardingValues(RangeShardingValue value) 方法
     * @param availableTargetNames
     * @param preciseValList
     * @return
     */
    protected Set<String> doShardingByRangeShardingValue(Collection<String> availableTargetNames,
        @Nullable RangeShardingValue<T> values) {
        List<PreciseShardingValue<T>> preciseValList =  this.transferToPreciseShardingValues(values);
        Set<String> listDbOrTable = new HashSet<>();
        preciseValList.forEach(preciseVal -> {
            listDbOrTable.add(this.doShardingByAlgorithm(availableTargetNames, preciseVal));
        });
        return listDbOrTable;
    }


    /**
     * 根据列表值转精确值返回数据库名或者表名
     * 
     * @param shardingValue
     * @return
     */
    protected List<PreciseShardingValue<T>> transferToPreciseShardingValues(final ListShardingValue<T> shardingValue) {
        List<PreciseShardingValue<T>> result = new ArrayList<>(shardingValue.getValues().size());
        for (T each : shardingValue.getValues()) {
            result.add(new PreciseShardingValue<T>(shardingValue.getLogicTableName(),
                shardingValue.getColumnName(),
                each));
        }
        return result;
    }

    /**
     * 根据范围值转精确值范围分片算法，可能是在日期范围上会用得上
     * 比如范围 2018-10-10 到2018-10-18，那么返回一共8天日期值
     * column来做具体的实现，这里目前返回空，即查询所有的数据库和表
     * 请自行实现此方法
     * 
     * @param shardingValue
     * @return
     */
    protected List<PreciseShardingValue<T>> transferToPreciseShardingValues(final RangeShardingValue<T> shardingValue) {
        List<PreciseShardingValue<T>> result = new ArrayList<>();
        Range<T> valueRange = shardingValue.getValueRange();
        // BETWEEN AND中分片键对应的最大值
        long lowerEndpoint = Long.parseLong(String.valueOf(valueRange.lowerEndpoint()));
        long upperEndpoint = Long.parseLong(String.valueOf(valueRange.upperEndpoint()));
        if (shardingValue.getColumnName().equals("order_data")) {
            // TODO
        }
        return result;// 否则返回空
    }

}
