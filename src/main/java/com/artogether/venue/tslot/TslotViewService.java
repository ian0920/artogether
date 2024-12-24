package com.artogether.venue.tslot;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class TslotViewService {
    //JPA 特定的，專門用於注入 EntityManager。
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTslotScheduleView() {

        String dropSql = "DROP VIEW IF EXISTS tslot_schedule";
        String sql = """
    CREATE VIEW tslot_schedule AS
    SELECT id AS tslot_id, vne_id, 'Monday' AS weekday, hour_of_mon AS work_hours, 
           effective_time, expiration_time FROM tslot
    UNION ALL
    SELECT id AS tslot_id, vne_id, 'Tuesday' AS weekday, hour_of_tue AS work_hours,
           effective_time, expiration_time FROM tslot
    UNION ALL
    SELECT id AS tslot_id, vne_id, 'Wednesday' AS weekday, hour_of_wed AS work_hours,
           effective_time, expiration_time FROM tslot
    UNION ALL
    SELECT id AS tslot_id, vne_id, 'Thursday' AS weekday, hour_of_thu AS work_hours, 
           effective_time, expiration_time FROM tslot
    UNION ALL
    SELECT id AS tslot_id, vne_id, 'Friday' AS weekday, hour_of_fri AS work_hours, 
           effective_time, expiration_time FROM tslot
    UNION ALL
    SELECT id AS tslot_id, vne_id, 'Saturday' AS weekday, hour_of_sat AS work_hours,
           effective_time, expiration_time FROM tslot
    UNION ALL
    SELECT id AS tslot_id, vne_id, 'Sunday' AS weekday, hour_of_sun AS work_hours, 
           effective_time, expiration_time FROM tslot;
""";

        entityManager.createNativeQuery(dropSql).executeUpdate();
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
