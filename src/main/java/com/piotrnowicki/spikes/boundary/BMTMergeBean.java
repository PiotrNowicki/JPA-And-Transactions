package com.piotrnowicki.spikes.boundary;

import com.piotrnowicki.spikes.entity.MyEntity;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.*;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BMTMergeBean {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @EJB
    private UtilsBean utilsBean;

    public void mergeEntity() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();

        MyEntity entity = new MyEntity("entityName", "OK", "DEFAULT");

        utilsBean.log("----------------------------------------------------");
        utilsBean.log("Persisting entity with valid values: " + entity);

        em.persist(entity);

        utx.commit();


        utx.begin();

        entity.setContent("tooLongContentValue");

        em.merge(entity);

        utilsBean.log("Trying to merge entity: " + entity);

        try {
            em.flush();
        } catch (PersistenceException e) {
            utilsBean.log("PersistenceException caught while merging entity: " + entity);
            utilsBean.log("Should the transaction be rolled back? " + (utx.getStatus() == Status.STATUS_MARKED_ROLLBACK));
            utilsBean.log("Is the entity managed? " + em.contains(entity));

            utx.rollback();

            utx.begin();
            entity.setContent("");
            entity.setCode("ERROR");

            utilsBean.log("Setting fail-safe attributes of the entity: " + entity);

            em.merge(entity);

            utx.commit();
        }

        utilsBean.readAllEntities();
        utilsBean.log("----------------------------------------------------");
    }
}
