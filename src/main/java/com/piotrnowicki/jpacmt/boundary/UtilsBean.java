package com.piotrnowicki.jpacmt.boundary;

import com.piotrnowicki.jpacmt.entity.MyEntity;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UtilsBean {

    @PersistenceContext
    private EntityManager em;

    private Logger logger = Logger.getLogger(UtilsBean.class.getName());

    @Resource
    private TransactionSynchronizationRegistry tsr;

    public void readAllEntities() {
        List<MyEntity> resultList = em.createNamedQuery("MyEntity.FIND_ALL", MyEntity.class).getResultList();

        log("Records from the DB: " + resultList);
    }

    public void log(String message) {
        logger.info("[TX: " + tsr.getTransactionKey() + "] " + message);
    }
}
