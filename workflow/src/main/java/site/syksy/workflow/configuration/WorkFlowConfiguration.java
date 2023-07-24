package site.syksy.workflow.configuration;

import org.flowable.common.spring.SpringTransactionInterceptor;
import org.flowable.engine.ProcessEngine;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import site.syksy.workflow.datasorce.FlowDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author teng.dang
 * @date 2023/7/25
 */
@Configuration
@ComponentScan(basePackages = "site.syksy.workflow")
public class WorkFlowConfiguration {

    @Resource
    private FlowDataSource flowDataSource;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        return flowDataSource.customDataSource();
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource());
        configuration.setTransactionManager(transactionManager());
//        configuration.setConfigurators();
        return configuration;
    }

    @Bean
    public ProcessEngine processEngine() {
        return processEngineConfiguration().buildProcessEngine();
    }
}
