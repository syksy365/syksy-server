package site.syksy.workflow.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import site.syksy.workflow.properties.H2Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author ifreeky
 * @date 2023/7/25
 */
@Configuration
@ComponentScan(basePackages = "site.syksy.workflow")
public class WorkFlowConfiguration {

    @Resource
    private H2Properties h2Properties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        return DataSourceBuilder.create().driverClassName(h2Properties.getDriverClassName())
                .url(h2Properties.getUrl())
                .username(h2Properties.getUsername())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


}
