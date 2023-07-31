package site.syksy.workflow.transaction;

import org.springframework.transaction.TransactionManager;

/**
 * @author ifreeky
 */
public interface CustomTransactionManger {

    TransactionManager getTransactionManager();
}
