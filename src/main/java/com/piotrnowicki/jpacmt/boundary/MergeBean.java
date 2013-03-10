package com.piotrnowicki.jpacmt.boundary;

import com.piotrnowicki.jpacmt.entity.MyEntity;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Stateless
public class MergeBean {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext sctx;

    @EJB
    private UtilsBean utilsBean;

    @EJB
    private MergeBean self;

    public void mergeEntity() {
        MyEntity entity = new MyEntity("entityName", "OK", "DEFAULT");

        utilsBean.log("----------------------------------------------------");
        utilsBean.log("Persisting entity with valid values: " + entity);

        em.persist(entity);

        try {
            self.tryMergingEntity(entity);
        } catch (UpdateException ex) {
            utilsBean.log("Application Exception caught. Will this exception mark tx for rollback: " + sctx.getRollbackOnly());

            entity.setContent("");
            entity.setCode("ERROR");

            utilsBean.log("Setting fail-safe attributes of the entity: " + entity);
        }

        utilsBean.readAllEntities();
        utilsBean.log("----------------------------------------------------");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void tryMergingEntity(final MyEntity entity) throws UpdateException {
        entity.setContent("tooLongContentValue");

        em.merge(entity);

        utilsBean.log("Trying to merge entity: " + entity);

        try {
            em.flush();
        } catch (PersistenceException e) {
            utilsBean.log("PersistenceException caught while merging entity: " + entity);
            utilsBean.log("Should the transaction be rolled back? " + sctx.getRollbackOnly());
            utilsBean.log("Is the entity managed? " + em.contains(entity));

            throw new UpdateException();
        }
    }
}
