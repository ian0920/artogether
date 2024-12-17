package com.artogether.event.evt_order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EvtOrderDAO {


    @Autowired
    EntityManager em;


    public List<EvtOrder> getEvtOrderByCriteria(Integer businessId, Integer eventId, Timestamp startDate, Timestamp endDate, Integer status, int page, int size) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EvtOrder> cq = cb.createQuery(EvtOrder.class);
        Root<EvtOrder> root = cq.from(EvtOrder.class);

        List<Predicate> predicates = new ArrayList<>();


        predicates.add(cb.equal(root.get("event").get("businessMember").get("id"), businessId));

        if (eventId != null && eventId != -1) {
            predicates.add(cb.equal(root.get("event").get("id"), eventId));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
        }
        if (status != -1) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        // Pagination
//        TypedQuery<EvtOrder> query = em.createQuery(cq);
//        query.setFirstResult(page * size); // Offset
//        query.setMaxResults(size); // Limit
//
//        List<EvtOrder> result = em.createQuery(cq).getResultList();
//
//
//        return result;

        int firstResult = page * size; // Calculate offset
        return em.createQuery(cq)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

    public long countEvtOrderByCriteria(Integer businessId, Integer eventId, Timestamp startDate, Timestamp endDate, Integer status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<EvtOrder> root = cq.from(EvtOrder.class);

        List<Predicate> predicates = new ArrayList<>();


        predicates.add(cb.equal(root.get("event").get("businessMember").get("id"), businessId));

        if (eventId != null && eventId != -1) {
            predicates.add(cb.equal(root.get("event").get("id"), eventId));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
        }
        if (status != -1) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getSingleResult();
    }

}
