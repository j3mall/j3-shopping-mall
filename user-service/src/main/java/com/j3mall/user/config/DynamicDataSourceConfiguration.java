package com.j3mall.user.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.j3mall.framework.datasource.annotation.DynamicDataSource;
import com.j3mall.framework.datasource.properties.DruidDataSourceProperties;
import com.j3mall.framework.datasource.properties.DynamicDataSourceProperties;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 动态数据源配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceConfiguration {
    @Resource
    private DynamicDataSourceProperties properties;

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSources = getDynamicDataSource();
        dynamicDataSource.setTargetDataSources(dataSources);
        // 设置默认数据源
        Object defaultDataSource = dataSources.get(properties.getPrimary());
        if (null == defaultDataSource && Objects.equals(properties.getStrict(), true)) {
            throw new IllegalArgumentException(properties.getPrimary() + "数据源为空");
        }
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        log.info("Druid数据源已配置, {} 其中主库是{}", dataSources.keySet(), properties.getPrimary());
        return dynamicDataSource;
    }

    private Map<Object, Object> getDynamicDataSource() {
        Map<String, DruidDataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();
        Map<Object, Object> targetDataSources = new LinkedHashMap<>(dataSourcePropertiesMap.size());
        dataSourcePropertiesMap.forEach((k, v) -> {
            DruidDataSource druidDataSource = buildDruidDataSource(v);
            targetDataSources.put(k, druidDataSource);
        });
        return targetDataSources;
    }

    public static DruidDataSource buildDruidDataSource(DruidDataSourceProperties properties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        BeanUtils.copyProperties(properties, druidDataSource);
        try {
            druidDataSource.setFilters(properties.getFilters());
            druidDataSource.init();
        } catch (SQLException e) {
            log.info("数据源初始化异常, ", e);
        }
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
