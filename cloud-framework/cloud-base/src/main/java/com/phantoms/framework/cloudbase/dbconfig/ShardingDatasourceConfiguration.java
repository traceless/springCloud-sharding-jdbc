package com.phantoms.framework.cloudbase.dbconfig;

import groovy.util.logging.Slf4j;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingjdbc.core.rule.ShardingRule;
import io.shardingjdbc.core.rule.TableRule;
import io.shardingjdbc.core.util.StringUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.phantoms.framework.cloudbase.dbconfig.model.DbConfig;
import com.phantoms.framework.cloudbase.dbconfig.model.ModuleConfig;
import com.phantoms.framework.cloudbase.dbconfig.model.TableConfig;
import com.phantoms.framework.cloudbase.dbconfig.sharding.SourceShardingAlgorithm;
import com.phantoms.framework.cloudbase.dbconfig.sharding.TableShardingAlgorithm;

/**
 * ShardingDatasource的配置，使用动态数据源实现读写分离，不使用它内置的方式，主要是懒。 ShardingJDBC
 * 
 * @version <pre>
 * Author   Version     Date        Changes
 * zyj  1.0         2018年11月30日     Created
 *
 * </pre>
 * @since 1.
 */
@Configuration
@Slf4j
@MapperScan(basePackages = {  "com.phantoms.helper.common.mapper1" }, sqlSessionTemplateRef = "test2SqlSessionTemplate")

public class ShardingDatasourceConfiguration implements MasterSlaveDataSource {

    @Autowired
    private ConfigurableEnvironment                     environment;

    private List<DbConfig>                              dataSourcelist;

    private List<TableConfig>                           tableList;

    private Map<String, ModuleConfig>                   codeMapModule;

    private Map<String, List<DbConfig>>                 moduleMapDbList;

    private Map<String, ModuleMasterSlaveDataSourceMap> moduleMapMasterSlaveDataSource;

    private Map<String, DataSource>                     slaveDataSourceMap = new HashMap<>();;

    private Map<String, DataSource>                     masterDataSourceMap = new HashMap<>();

    private DataSource                                  masterDataSource;

    private DataSource                                  slaveDataSource;

    /**
     * 模块服务对应的master和slave 实体类
     * Created
     * 
     * @since 1.
     */
    class ModuleMasterSlaveDataSourceMap {

        private Map<String, DataSource> masterDataSourceMap;
        private Map<String, DataSource> slaveDataSourceMap;
        public static final String      SLAVE_TYPE  = "salve";
        public static final String      MASTER_TYPE = "master";

        public ModuleMasterSlaveDataSourceMap(Map<String, DataSource> masterDataSourceMap,
                                              Map<String, DataSource> slaveDataSourceMap){
            this.masterDataSourceMap = masterDataSourceMap;
            this.slaveDataSourceMap = slaveDataSourceMap;
        }

        public Map<String, DataSource> getDataSourceMap(String type) {
            if (SLAVE_TYPE.equals(type)) {
                return slaveDataSourceMap;
            }
            if (MASTER_TYPE.equals(type)) {
                return masterDataSourceMap;
            }
            return null;
        }
        
    }

    /**
     * 初始化
     * @throws Exception
     */
    @PostConstruct
    private void init() throws Exception {
        initDbConfig();
        masterDataSource = createShardingDataSource(ModuleMasterSlaveDataSourceMap.MASTER_TYPE);
        slaveDataSource = createShardingDataSource(ModuleMasterSlaveDataSourceMap.SLAVE_TYPE);
    }

    /**
     * 根据数据库列表返回对应的数据源列表，区分master，slave
     * 
     * result size: expected 1, actual 4
     * 
     * @return
     * @throws Exception
     */
    private Map<String, DataSource> createMasterOrSlaveDataSource(List<DbConfig> dbList, String dataSourceType) throws Exception {

        Binder binder = Binder.get(environment);
        // 装载数据库map
        Map<String, DataSource> dataSourceMap = dbList.stream().map(config -> {
            String url = config.getMasterUrl();
            if(dataSourceType.equals(ModuleMasterSlaveDataSourceMap.SLAVE_TYPE)){
                url = config.getSlaveUrl();
            }
            DruidDataSource druid = binder.bind("spring.datasource", DruidDataSource.class).get();
            druid.setUrl(url);
            druid.setUsername(config.getUsername());
            druid.setPassword(config.getPassword());
            druid.setName(config.getName());
            return druid;
        }).collect(Collectors.toMap(DruidDataSource::getName, druid -> druid));
        return dataSourceMap;
    }

    /**
     * 根据数据库类型，master 和slave 创建ShardingDataSource
     * 不要ShardingDataSource类型的bean放到容器上
     * 否则健康监查,会出现jdbc连接太多的异常Incorrect
     * @param dataSourceType
     * @return
     * @throws SQLException
     */
    private ShardingDataSource createShardingDataSource(String dataSourceType) throws SQLException {
        // 设置每个表的路由规则
        List<TableRule> tableRuleList = tableList.stream()
            .map(table -> {
                // 找到模块对应的数据库列表
                List<DbConfig> dbList = moduleMapDbList.get(table.getModuleCode());
                // 应该从各自服务的配置信息去获取，if(table.getModuleCode().equals("user-server")); 这里不应该添加进来
                ModuleConfig module = codeMapModule.get(table.getModuleCode());
                // 根据表名字段值进行选表定位算法，一张表可以有多个字段定位的。
                TableShardingAlgorithm<Long> thm = new TableShardingAlgorithm<Long>(StringUtil.splitWithComma(table.getTableColumns()),
                    module);
                // 根据表名和字段值进行选库定位算法
                SourceShardingAlgorithm<Long> dthm = new SourceShardingAlgorithm<Long>(StringUtil.splitWithComma(table.getTableColumns()),
                    module,
                    dbList);
                // 获取模块服务对应的数据源map, dataSourceType = mater or slave;
                Map<String, DataSource> dataSourceMap = moduleMapMasterSlaveDataSource.get(table.getModuleCode())
                    .getDataSourceMap(dataSourceType);
                // 建立虚拟逻辑表对应的真实表, 使用 表的求模数量
                List<String> actualDataNodes = new ArrayList<>();
                int TABLE_MOD = codeMapModule.get(table.getModuleCode()).getTableNum();
                for (int i = 0; i < TABLE_MOD; i++) {
                    int num = i;
                    dataSourceMap.forEach((dbName, val) -> {
                        actualDataNodes.add(dbName + "." + table.getTableName() + "_" + num);
                    });
                }
                return new TableRule(table.getTableName(), actualDataNodes, dataSourceMap, dthm, thm, null, null, null);
            })
            .collect(Collectors.toList());
        // shardingRule需要把逻辑表名 绑定到TableRule上去
        List<String> bindingTableGroups = new ArrayList<>();
        String bindingStr = tableList.stream().map(obj -> {
            return obj.getTableName();
        }).reduce((add, item) -> add + "," + item).get();
        bindingTableGroups.add(bindingStr);
        // 初始化 ShardingRule数据源, masterDataSourceMap是全部总共的，当进行route的时候，会根据表名会匹配到对应的数据源。
        Map<String, DataSource>  shardingRuledataSourceMap = dataSourceType.equals(ModuleMasterSlaveDataSourceMap.SLAVE_TYPE) ? masterDataSourceMap : slaveDataSourceMap;
        ShardingRule shardingRule = new ShardingRule(shardingRuledataSourceMap,
            "default",
            tableRuleList,
            bindingTableGroups,
            null,
            null,
            null);

        return new ShardingDataSource(shardingRule);
    }


    /**
     * 初始化dataSourcelist和tableList，可重新实现这部分
     * 可以在config中心实现，目前垂直分库比较复杂，用的是数据库配置实现
     * 
     * @throws Exception
     */
    public void initDbConfig() throws Exception {
        Binder binder = Binder.get(environment);
        DruidDataSource druidDataSource = binder.bind("spring.datasource", DruidDataSource.class).get();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(druidDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper-config/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.phantoms.framework.cloudbase.dbconfig.model");
        SqlSession sqlSession = sqlSessionFactoryBean.getObject().openSession();
        dataSourcelist = sqlSession.selectList("DbConfigMapper.selectList", null);
        tableList = sqlSession.selectList("TableConfigMapper.selectList", null);
        List<ModuleConfig> moduleList = sqlSession.selectList("ModuleConfigMapper.selectList", null);
        
        moduleMapDbList = new HashMap<>();
        codeMapModule = new HashMap<>();
        // 这里应该添加限制，比如user-server只能添加自己的module服务，可以在 createShardingDataSource 里进行修改
        moduleList.forEach(module -> {
            moduleMapDbList.put(module.getModuleCode(), new ArrayList<DbConfig>());
            codeMapModule.put(module.getModuleCode(), module);
        });
        dataSourcelist.stream().forEach(dbConfig -> {
            // 添加到对应模块服务的数据库列表,根据数据库约束得list不可能为空, 这里不用判断是否为空，为空直接让它报错
            moduleMapDbList.get(dbConfig.getModuleCode()).add(dbConfig);
        });
        moduleMapMasterSlaveDataSource =  new HashMap<>(); 
        // 先装载模块服务的数据库列表对应的 master和slave 数据源，masterDataSourceMap是总共的, 不用forEach，因为要强制捕抓异常，无法抛出去。
        for (Map.Entry<String, List<DbConfig>> entry : moduleMapDbList.entrySet()) {
            String moduleCode = entry.getKey();
            List<DbConfig> dbList = entry.getValue();
            Map<String, DataSource> masterMap = createMasterOrSlaveDataSource(dbList,
                ModuleMasterSlaveDataSourceMap.MASTER_TYPE);
            Map<String, DataSource> slaveMap = createMasterOrSlaveDataSource(dbList,
                ModuleMasterSlaveDataSourceMap.SLAVE_TYPE);
            masterDataSourceMap.putAll(masterMap);
            slaveDataSourceMap.putAll(slaveMap);
            moduleMapMasterSlaveDataSource.put(moduleCode, new ModuleMasterSlaveDataSourceMap(masterMap, slaveMap));
        }

    }

 
    @Override
    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    @Override
    public DataSource getSlaveDataSource() {
        return slaveDataSource;
    }
    
}
