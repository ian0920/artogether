package com.artogether.event.evt_coup;

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
public class EvtCoupDAO {

    @Autowired
    EntityManager em;


    public List<EvtCoup> getEvtCoupsByCriteria(Integer eventId, String couponName, byte type, Timestamp startDate, Timestamp endDate, byte status) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EvtCoup> cq = cb.createQuery(EvtCoup.class);
        Root<EvtCoup> root = cq.from(EvtCoup.class);

        List<Predicate> predicates = new ArrayList<>();


        if (eventId != -1) {
            predicates.add(cb.equal(root.get("event").get("id"), eventId));
        }
        if (couponName != null && !couponName.isEmpty()) {
            predicates.add(cb.like(root.get("evtCoupName"), "%"+couponName+"%"));
        }
        if (type != -1) {
            predicates.add(cb.equal(root.get("type"), type));
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

        List<EvtCoup> result = em.createQuery(cq).getResultList();


        return result;
    }

}
