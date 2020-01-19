package com.ebig.mcp.server.api.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


/**
 * @description:jdbc事务管理类
 * @author: wenchao
 * @time: 2019-10-15 14:29
 */
@Component("manager")
public class TransactionManager {

    @Autowired
    private DataSourceTransactionManager manager;
    /**
     * 开启事务
     * @return
     */
    public TransactionStatus begin(){
        return beginTransaction(manager);
    }

    /**
     * 提交事务
     * @param status
     */
    public void commit(TransactionStatus status){
        commitTransaction(manager,status);
    }

    /**
     * 回滚事务
     * @param status
     */
    public void rollback(TransactionStatus status){
        rollbackTransaction(manager,status);
    }
    /**
     * 开启事务
     */
    public TransactionStatus beginTransaction(DataSourceTransactionManager transactionManager){
        //事务定义类
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 返回事务状态
        return transactionManager.getTransaction(def);
    }

    /**
     * 提交事务
     * @param transactionManager
     * @param status
     */
    public void commitTransaction(DataSourceTransactionManager transactionManager,TransactionStatus status){
        transactionManager.commit(status);
    }

    /**
     * 事务回滚
     * @param transactionManager
     * @param status
     */
    public void rollbackTransaction(DataSourceTransactionManager transactionManager,TransactionStatus status){
        transactionManager.rollback(status);
    }

}
