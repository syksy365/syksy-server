package site.syksy.workflow.datasorce;

import javax.sql.DataSource;

/**
 * @author teng.dang
 * @date 2023/7/25
 */
public interface FlowDataSource {

    DataSource customDataSource();
}
