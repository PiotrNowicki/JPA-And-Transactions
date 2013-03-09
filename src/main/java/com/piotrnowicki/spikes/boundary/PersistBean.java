package com.piotrnowicki.spikes.boundary;

import com.piotrnowicki.spikes.entity.MyEntity;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Stateless
public class PersistBean {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext sctx;

    @EJB
    private UtilsBean utilsBean;

    @EJB
    private PersistBean self;

    public void persistEntity() {
        MyEntity entity = new MyEntity("entityName", "OK", "tooLongContentValue");

        utilsBean.log("----------------------------------------------------");
        utilsBean.log("Persisting entity with invalid values: " + entity);

        self.tryPersistingEntity(entity);
        utilsBean.readAllEntities();
        utilsBean.log("----------------------------------------------------");
    }

    public void tryPersistingEntity(MyEntity entity) {
        utilsBean.log("Trying to persist entity.");

        em.persist(entity);

        try {
            em.flush();
        } catch (PersistenceException e) {
            utilsBean.log("Flushing exception caught while persisting entity: " + entity +  ". Should the transaction be rolled back? " + sctx.getRollbackOnly());
            utilsBean.log("Is the entity managed? " + em.contains(entity));

            entity.setContent("");
            entity.setCode("ERROR");

            utilsBean.log("Setting proper attributes of the entity. " + entity);

            self.retrySavingEntity(entity);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void retrySavingEntity(MyEntity entity) {
        utilsBean.log("Retrying to persist entity: " + entity);

        // 'entity' is in a detached state, so we cannot invoke 'persist' once more on it.
        em.merge(entity);
    }
}
