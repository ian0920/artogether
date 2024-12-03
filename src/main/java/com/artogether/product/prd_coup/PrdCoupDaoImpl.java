package com.artogether.product.prd_coup;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PrdCoupDaoImpl implements PrdCoupDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int add(PrdCoup prdCoup) {
        try {
            entityManager.persist(prdCoup);
            return prdCoup.getId(); // 返回新增記錄的主鍵
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update(PrdCoup prdCoup) {
        try {
            entityManager.merge(prdCoup); // 使用 merge 更新
            return 1; // 成功返回 1
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // 發生錯誤返回 -1
    }

    @Override
    public int delete(Integer id) {
        try {
            PrdCoup prdCoup = entityManager.find(PrdCoup.class, id);
            if (prdCoup != null) {
                entityManager.remove(prdCoup); // 刪除記錄
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public PrdCoup findByPK(Integer id) {
        try {
            return entityManager.find(PrdCoup.class, id); // 查找單條記錄
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PrdCoup> getAll() {
        try {
            String hql = "FROM PrdCoup";
            return entityManager.createQuery(hql, PrdCoup.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findByBusinessId(Integer businessId) {
        try {
            String hql = "FROM PrdCoup WHERE businessMember.id = :businessId";
            return entityManager.createQuery(hql, PrdCoup.class)
                                .setParameter("businessId", businessId)
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findValidCoupons(LocalDateTime now) {
        try {
            String hql = "FROM PrdCoup WHERE startDate <= :now AND endDate >= :now AND status = 1";
            return entityManager.createQuery(hql, PrdCoup.class)
                                .setParameter("now", now)
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold) {
        try {
            StringBuilder hql = new StringBuilder("FROM PrdCoup WHERE 1=1");
            if (name != null) {
                hql.append(" AND name LIKE :name");
            }
            if (type != null) {
                hql.append(" AND type = :type");
            }
            if (status != null) {
                hql.append(" AND status = :status");
            }
            if (threshold != null) {
                hql.append(" AND threshold >= :threshold");
            }
            Query query = entityManager.createQuery(hql.toString(), PrdCoup.class);
            if (name != null) {
                query.setParameter("name", "%" + name + "%");
            }
            if (type != null) {
                query.setParameter("type", type);
            }
            if (status != null) {
                query.setParameter("status", status);
            }
            if (threshold != null) {
                query.setParameter("threshold", threshold);
            }
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findCouponsExpiringSoon(LocalDateTime now, Integer days) {
        try {
            String hql = "FROM PrdCoup WHERE endDate BETWEEN :now AND :end";
            return entityManager.createQuery(hql, PrdCoup.class)
                                .setParameter("now", now)
                                .setParameter("end", now.plusDays(days))
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findCouponsByStatus(Integer status) {
        try {
            String hql = "FROM PrdCoup WHERE status = :status";
            return entityManager.createQuery(hql, PrdCoup.class)
                                .setParameter("status", status)
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findCouponsByType(Integer type) {
        try {
            String hql = "FROM PrdCoup WHERE type = :type";
            return entityManager.createQuery(hql, PrdCoup.class)
                                .setParameter("type", type)
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold) {
        try {
            StringBuilder hql = new StringBuilder("FROM PrdCoup WHERE 1=1");
            if (status != null) {
                hql.append(" AND status = :status");
            }
            if (type != null) {
                hql.append(" AND type = :type");
            }
            if (threshold != null) {
                hql.append(" AND threshold <= :threshold");
            }
            Query query = entityManager.createQuery(hql.toString(), PrdCoup.class);
            if (status != null) {
                query.setParameter("status", status);
            }
            if (type != null) {
                query.setParameter("type", type);
            }
            if (threshold != null) {
                query.setParameter("threshold", threshold);
            }
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<PrdCoup> findActiveCoupons(LocalDateTime currentDate) {
        try {
            String hql = "FROM PrdCoup WHERE startDate <= :currentDate AND endDate >= :currentDate AND status = 1";
            return entityManager.createQuery(hql, PrdCoup.class)
                                .setParameter("currentDate", currentDate)
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

	
}
