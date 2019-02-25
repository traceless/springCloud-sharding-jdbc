package com.phantoms.framework.cloudbase.dbconfig.sharding;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import com.phantoms.framework.cloudbase.dbconfig.model.ModuleConfig;
import com.phantoms.framework.cloudbase.util.SnowflakeIdFactory;

@Slf4j
public class TableShardingAlgorithm<T extends Comparable<?>> extends AbstractShardingAlgorithm<T> {

    private Collection<String> columnSet;
    
    private ModuleConfig       moduleConfig; // 备用，理论上可以去掉
   
    public int TABLE_MOD = 0;

    public TableShardingAlgorithm(Collection<String> columnSet, ModuleConfig moduleConfig){
        this.columnSet = columnSet;
        this.moduleConfig = moduleConfig;
        TABLE_MOD = moduleConfig.getTableNum();
    }

    @Override
    public Collection<String> getShardingColumns() {
        return columnSet;
    }

    /**
     * 分表算法
     */
    @Override
    public String doShardingByAlgorithm(Collection<String> availableTargetNames, PreciseShardingValue<T> shardingValue) {
        Long value = 0L;
        try{
            value = Long.parseLong(shardingValue.getValue().toString());
        } catch (NumberFormatException e) {
            String val = shardingValue.getValue().toString();
            // 单个字母字符的范围是0-127，中文就更大了
            value = Long.valueOf(val.charAt(0)) ; // 向上取整
        }
        value = executePreciseAlgorithm(value);
        for (String name : availableTargetNames) {
            if (name.endsWith("_" + value)) {
                log.info("doSharding table name: {}- {} ", name, value);
                return name;
            }
        }
        throw new UnsupportedOperationException("error 无法定位到表");
    }

    
    /**
     * 精确分片，默认10张表。这里的参数不要随意改变，扩容直接新增机器就可以了，不扩容表 
     * 获取long型二进制的后10位，转int，进行相除
     */
    public long executePreciseAlgorithm(Long value) {
        // 取二进制的最后10位,在1024以内
        return SnowflakeIdFactory.getIntFormLastBit(value, 10) % TABLE_MOD;
    }

}
