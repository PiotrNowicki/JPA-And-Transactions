package com.piotrnowicki.spikes.boundary;

import com.piotrnowicki.spikes.entity.MyEntity;

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
        em.flush();

        self.tryMergingEntity(entity);
        utilsBean.readAllEntities();
        utilsBean.log("----------------------------------------------------");
    }

    public void tryMergingEntity(MyEntity entity) {
        entity.setContent("tooLongContentValue");

        utilsBean.log("Trying to merge entity: " + entity);

        em.merge(entity);

        try {
            em.flush();
        } catch (PersistenceException e) {
            utilsBean.log("Flushing exception caught while merging entity: " + entity + ". Should the transaction be rolled back? " + sctx.getRollbackOnly());
            utilsBean.log("Is the entity managed? " + em.contains(entity));

            entity.setContent("");
            entity.setCode("ERROR");

            utilsBean.log("Setting proper attributes of the entity. " + entity);

            self.retrySavingEntity(entity);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void retrySavingEntity(MyEntity entity) {
        utilsBean.log("Retrying to merge entity: " + entity);

        // 'entity' is in a detached state, so we cannot invoke 'persist' once more on it.
        em.merge(entity);
    }
}
