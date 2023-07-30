package site.syksy.workflow.configuration;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author ifreeky
 */
@Component
@Order(Ordering.DEFAULT_ORDER + 1)
public class CamundaConfiguration extends AbstractCamundaConfiguration {

    @Override
    public void preInit(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        springProcessEngineConfiguration.setHistory(ProcessEngineConfiguration.HISTORY_FULL);

    }

}
